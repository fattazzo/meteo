package com.gmail.fattazzo.meteo.preferences.iconthemepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gmail.fattazzo.meteo.R;
import com.gmail.fattazzo.meteo.utils.Closure;
import com.gmail.fattazzo.meteo.utils.icons.WeatherIconsFactory;
import com.gmail.fattazzo.meteo.utils.icons.retirever.IconsRetriever;

import java.util.List;

/**
 * @author fattazzo
 * <p/>
 * date: 23/02/16
 */
public class IconThemeListPreferenceAdapter extends ArrayAdapter<IconThemeItem> {

    private static final String TAG = "IconThemeAdapter";

    private Context context;
    private List<IconThemeItem> icons;
    private int resource;
    private Closure<Void> onThemeSelectedClosure;

    public IconThemeListPreferenceAdapter(Context context, int resource,
                                          List<IconThemeItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.icons = objects;
    }

    /**
     * Setter of onThemeSelectedClosure
     *
     * @param onThemeSelectedClosure the onThemeSelectedClosure to set
     */
    public void setOnThemeSelectedClosure(Closure<Void> onThemeSelectedClosure) {
        this.onThemeSelectedClosure = onThemeSelectedClosure;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        IconThemeViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);

            holder = new IconThemeViewHolder();
            holder.setThemeName((TextView) convertView
                    .findViewById(R.id.icon_theme_name));
            holder.setImage1((ImageView) convertView
                    .findViewById(R.id.icon_theme_image1));
            holder.setImage2((ImageView) convertView
                    .findViewById(R.id.icon_theme_image2));
            holder.setImage3((ImageView) convertView
                    .findViewById(R.id.icon_theme_image3));
            holder.setRadioButton((RadioButton) convertView
                    .findViewById(R.id.icon_theme_radio));

            convertView.setTag(holder);
        } else {
            holder = (IconThemeViewHolder) convertView.getTag();
        }

        holder.getThemeName().setText(icons.get(position).getName());

        IconsRetriever iconsRetriever = WeatherIconsFactory.INSTANCE.getIconsRetriever(icons.get(position).getPath());

        holder.getImage1().setImageResource(iconsRetriever.getIconDemo1());
        holder.getImage2().setImageResource(iconsRetriever.getIconDemo2());
        holder.getImage3().setImageResource(iconsRetriever.getIconDemo3());

        holder.getRadioButton().setChecked(icons.get(position).isChecked());

        convertView.setOnClickListener(v -> {
            for (int i = 0; i < icons.size(); i++) {
                if (i == position)
                    icons.get(i).setChecked(true);
                else
                    icons.get(i).setChecked(false);
            }
            if (onThemeSelectedClosure != null) {
                onThemeSelectedClosure.execute(null);
            }
        });

        return convertView;
    }
}