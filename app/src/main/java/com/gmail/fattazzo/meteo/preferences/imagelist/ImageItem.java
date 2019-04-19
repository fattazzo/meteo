/*
 * Project: meteo
 * File: ImageItem
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

/**
 *
 * @author fattazzo
 *
 *         date: 27/ago/2014
 *
 */
public class ImageItem {

    private String file;
    private boolean checked;
    private String name;

    /**
     * Costruttore.
     *
     * @param name
     *            nome immagine
     * @param file
     *            nome del file
     * @param isChecked
     *            <code>true</code> se selezionata
     */
    public ImageItem(final CharSequence name, final CharSequence file, final boolean isChecked) {
        this(name.toString(), file.toString(), isChecked);
    }

    /**
     * Costruttore.
     *
     * @param name
     *            nome immagine
     * @param file
     *            nome del file
     * @param isChecked
     *            <code>true</code> se selezionata
     */
    public ImageItem(final String name, final String file, final boolean isChecked) {
        this.name = name;
        this.file = file;
        this.checked = isChecked;
    }

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the checked
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * @param checked
     *            the checked to set
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
