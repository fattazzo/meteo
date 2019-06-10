package com.gmail.fattazzo.meteo.preferences.iconthemepicker;

import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/02/16
 */
public class IconThemeViewHolder {

    private TextView themeName;

    private ImageView image1;
    private ImageView image2;
    private ImageView image3;

    private RadioButton radioButton;

    /**
     * @return the radioButton
     */
    public RadioButton getRadioButton() {
        return radioButton;
    }

    /**
     * Setter of radioButton
     *
     * @param radioButton the radioButton to set
     */
    public void setRadioButton(RadioButton radioButton) {
        this.radioButton = radioButton;
    }

    /**
     * @return the themeName
     */
    public TextView getThemeName() {
        return themeName;
    }

    /**
     * Setter of themeName
     *
     * @param themeName the themeName to set
     */
    public void setThemeName(TextView themeName) {
        this.themeName = themeName;
    }

    /**
     * @return the image1
     */
    public ImageView getImage1() {
        return image1;
    }

    /**
     * Setter of image1
     *
     * @param image1 the image1 to set
     */
    public void setImage1(ImageView image1) {
        this.image1 = image1;
    }

    /**
     * @return the image2
     */
    public ImageView getImage2() {
        return image2;
    }

    /**
     * Setter of image2
     *
     * @param image2 the image2 to set
     */
    public void setImage2(ImageView image2) {
        this.image2 = image2;
    }

    /**
     * @return the image3
     */
    public ImageView getImage3() {
        return image3;
    }

    /**
     * Setter of image3
     *
     * @param image3 the image3 to set
     */
    public void setImage3(ImageView image3) {
        this.image3 = image3;
    }
}