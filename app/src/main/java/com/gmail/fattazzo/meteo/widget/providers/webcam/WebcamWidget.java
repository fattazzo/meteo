/*
 * Project: meteo
 * File: WebcamWidget
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

package com.gmail.fattazzo.meteo.widget.providers.webcam;

import com.gmail.fattazzo.meteo.domain.xml.webcam.Webcam;

/**
 *
 * @author fattazzo
 *
 *         date: 24/lug/2015
 *
 */
public class WebcamWidget {

    private int id;

    private String descrizione;

    private int idLink;

    private String link;

    /**
     * Costruttore.
     *
     * @param webcam
     *            webcam
     */
    public WebcamWidget(final Webcam webcam) {
        super();
        this.id = webcam.getId();
        this.descrizione = webcam.getDescrizione();
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the idLink
     */
    public int getIdLink() {
        return idLink;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param idLink
     *            the idLink to set
     */
    public void setIdLink(int idLink) {
        this.idLink = idLink;
    }

    /**
     * @param link
     *            the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }
}
