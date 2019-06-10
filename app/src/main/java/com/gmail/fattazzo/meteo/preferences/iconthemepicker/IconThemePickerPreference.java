package com.gmail.fattazzo.meteo.preferences.iconthemepicker;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.fattazzo.meteo.R;
import com.gmail.fattazzo.meteo.utils.icons.WeatherIconsFactory;
import com.gmail.fattazzo.meteo.utils.icons.retirever.IconsRetriever;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fattazzo
 * <p/>
 * date: 23/02/16
 */
public class IconThemePickerPreference extends ListPreference {

    private static final String TAG = "IconThemePreference";

    private Context context;
    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;

    private CharSequence[] iconName;
    private List<IconThemeItem> icons;
    private SharedPreferences preferences;
    private Resources resources;
    private String selectedIconPath, defaultIconFile;
    private TextView summary;

    public IconThemePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        resources = context.getResources();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        defaultIconFile = "meteo_trentino";
    }

    private String getEntry(String value) {
        String[] entries = resources.getStringArray(R.array.iconThemeName);
        String[] values = resources.getStringArray(R.array.iconThemeKey);
        int index = Arrays.asList(values).indexOf(value);
        try {
            return entries[index];
        } catch (Exception e) {
            return entries[0];
        }

    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        selectedIconPath = preferences.getString(
                context.getResources().getString(R.string.pref_key_iconsTheme), defaultIconFile);

        icon1 = (ImageView) view.findViewById(R.id.iconSelected1);
        icon2 = (ImageView) view.findViewById(R.id.iconSelected2);
        icon3 = (ImageView) view.findViewById(R.id.iconSelected3);
        updateIcon();

        summary = (TextView) view.findViewById(R.id.icon_theme_pref_summary);
        summary.setText(getEntry(selectedIconPath));

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (icons != null) {
            for (int i = 0; i < iconName.length; i++) {
                IconThemeItem item = icons.get(i);
                if (item.isChecked()) {

                    Editor editor = preferences.edit();
                    editor.putString(
                            context.getResources().getString(R.string.pref_key_iconsTheme),
                            item.getPath());
                    editor.apply();

                    selectedIconPath = item.getPath();
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
        CharSequence[] iconPath = getEntryValues();

        if (iconName == null || iconPath == null
                || iconName.length != iconPath.length) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array "
                            + "and an entryValues array which are both the same length");
        }

        String selectedIcon = preferences.getString(
                context.getResources().getString(R.string.pref_key_iconsTheme),
                "meteo_trentino");

        icons = new ArrayList<>();

        for (int i = 0; i < iconName.length; i++) {
            boolean isSelected = selectedIcon.equals(iconPath[i]);
            IconThemeItem item = new IconThemeItem(iconName[i], iconPath[i], isSelected);
            icons.add(item);
        }

        IconThemeListPreferenceAdapter adapter = new IconThemeListPreferenceAdapter(
                context, R.layout.icon_theme_item_picker, icons);
        adapter.setOnThemeSelectedClosure(paramObject -> getDialog().dismiss());
        builder.setAdapter(adapter, null);

    }

    private void updateIcon() {
        IconsRetriever iconsRetriever = WeatherIconsFactory.INSTANCE.getIconsRetriever(selectedIconPath);

        icon1.setImageResource(iconsRetriever.getIconDemo1());
        icon1.setTag(selectedIconPath);

        icon2.setImageResource(iconsRetriever.getIconDemo2());
        icon2.setTag(selectedIconPath);

        icon3.setImageResource(iconsRetriever.getIconDemo3());
        icon3.setTag(selectedIconPath);
    }

}