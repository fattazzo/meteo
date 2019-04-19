/*
 * Project: meteo
 * File: ImageListPreference
 *
 * Created by fattazzo
 * Copyright © 2018 Gianluca Fattarsi. All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gmail.fattazzo.meteo.preferences.imagelist;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmail.fattazzo.meteo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author fattazzo
 *
 *         date: 13/lug/2015
 *
 */
public class ImageListPreference extends ListPreference {

    /**
     *
     * @author fattazzo
     *
     *         date: 13/lug/2015
     *
     */
    private class CustomListPreferenceAdapter extends ArrayAdapter<ImageItem> {

        private Context context;
        private List<ImageItem> icons;
        private int resource;

        /**
         * Costruttore.
         *
         * @param context
         *            context
         * @param resource
         *            resource
         * @param objects
         *            objects
         */
        public CustomListPreferenceAdapter(final Context context, final int resource, final List<ImageItem> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.icons = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, parent, false);

                holder = new ViewHolder();
                holder.iconName = (TextView) convertView.findViewById(R.id.iconName);
                holder.iconImage = (RelativeLayout) convertView.findViewById(R.id.iconImage);
                holder.radioButton = (RadioButton) convertView.findViewById(R.id.iconRadio);
                holder.position = position;

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.iconName.setText(icons.get(position).getName());

            int identifier = context.getResources().getIdentifier(icons.get(position).getFile(), "drawable",
                    context.getPackageName());
            holder.iconImage.setBackgroundResource(identifier);

            holder.radioButton.setChecked(icons.get(position).isChecked());

            convertView.setOnClickListener(v -> {
                ViewHolder holder1 = (ViewHolder) v.getTag();
                for (int i = 0; i < icons.size(); i++) {
                    icons.get(i).setChecked(i == holder1.position);
                }
                getDialog().dismiss();
            });

            return convertView;
        }

    }

    /**
     *
     * @author fattazzo
     *
     *         date: 13/lug/2015
     *
     */
    private static class ViewHolder {
        protected RelativeLayout iconImage;
        protected TextView iconName;
        protected int position;
        protected RadioButton radioButton;
    }

    private Context context;
    private RelativeLayout icon;

    private CharSequence[] iconName;
    private List<ImageItem> icons;
    private SharedPreferences preferences;
    private Resources resources;
    private String selectedIconFile, defaultImageFile;
    private TextView summary;

    /**
     * Costruttore.
     *
     * @param context
     *            context
     * @param attrs
     *            attributi
     */
    public ImageListPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        resources = context.getResources();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        defaultImageFile = attrs.getAttributeValue(null, "defaultValue");
    }

    /**
     * @param value
     *            value
     * @return entry
     */
    private String getEntry(String value) {
        int index = Arrays.asList(getEntryValues()).indexOf(value);
        return getEntries()[index].toString();
    }

    @Override
    public CharSequence getSummary() {
        return getEntry() != null ? getEntry() : "";
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        selectedIconFile = preferences.getString(getKey(), defaultImageFile);

        icon = (RelativeLayout) view.findViewById(R.id.iconSelected);
        updateIcon();

        summary = (TextView) view.findViewById(R.id.summaryTheme);
        summary.setText(getEntry(selectedIconFile));

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (icons != null) {
            for (int i = 0; i < iconName.length; i++) {
                ImageItem item = icons.get(i);
                if (item.isChecked()) {

                    Editor editor = preferences.edit();
                    editor.putString(getKey(), item.getFile());
                    editor.apply();

                    selectedIconFile = item.getFile();
                    updateIcon();

                    summary.setText(item.getName());

                    break;
                }
            }
        }

    }

    @Override
    protected void onPrepareDialogBuilder(Builder builder) {

        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton(null, null);

        iconName = getEntries();
        CharSequence[] iconFile = getEntryValues();

        if (iconName == null || iconFile == null || iconName.length != iconFile.length) {
            throw new IllegalStateException("ListPreference requires an entries array "
                    + "and an entryValues array which are both the same length");
        }

        String selectedIcon = preferences.getString(getKey(), defaultImageFile);

        icons = new ArrayList<>();

        for (int i = 0; i < iconName.length; i++) {
            boolean isSelected = selectedIcon.equals(iconFile[i]);
            ImageItem item = new ImageItem(iconName[i], iconFile[i], isSelected);
            icons.add(item);
        }

        CustomListPreferenceAdapter customListPreferenceAdapter = new CustomListPreferenceAdapter(context,
                R.layout.preferences_image_list_item_adapter, icons);
        builder.setAdapter(customListPreferenceAdapter, null);

    }

    /**
     * Update the icon.
     */
    private void updateIcon() {
        int identifier = resources.getIdentifier(selectedIconFile, "drawable", context.getPackageName());

        icon.setBackgroundResource(identifier);
        icon.setTag(selectedIconFile);
    }

}
