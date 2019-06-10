package com.gmail.fattazzo.meteo.preferences.iconthemepicker;

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/02/16
 */
public class IconThemeItem {

    private String name;

    private String path;

    private boolean isChecked;

    /**
     * Costruttore.
     *
     * @param name      nome del tema
     * @param path      path del tema
     * @param isChecked <code>true</code> se selezionato
     */
    public IconThemeItem(CharSequence name, CharSequence path, boolean isChecked) {
        this(name.toString(), path.toString(), isChecked);
    }

    /**
     * Costruttore.
     *
     * @param name      nome del tema
     * @param path      path del tema
     * @param isChecked <code>true</code> se selezionato
     */
    public IconThemeItem(String name, String path, boolean isChecked) {
        this.name = name;
        this.path = path;
        this.isChecked = isChecked;
    }

    /**
     * @return the isChecked
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Setter of isChecked
     *
     * @param checked the isChecked to set
     */
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


}