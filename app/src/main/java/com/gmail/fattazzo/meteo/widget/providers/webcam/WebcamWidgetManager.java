/*
 * Project: meteo
 * File: WebcamWidgetManager
 *
 * Created by fattazzo
 * Copyright © 2019 Gianluca Fattarsi. All rights reserved.
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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.gmail.fattazzo.meteo.domain.xml.webcam.Webcam;
import com.gmail.fattazzo.meteo.domain.xml.webcam.Webcams;
import com.gmail.fattazzo.meteo.preferences.widget.webcam.WebcamWidgetsSettingsManager;
import com.gmail.fattazzo.meteo.widget.providers.MeteoWidgetProvider;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author fattazzo
 * <p/>
 * date: 24/lug/2015
 */
public class WebcamWidgetManager {

    /**
     * Costruttore.
     */
    private WebcamWidgetManager() {

    }

    /**
     * Webcam attualmente visualizzata dal widget.
     *
     * @param context context
     * @param webcams webcams
     * @return webcam visualizzata
     */
    public static WebcamWidget getCurrentWebcam(Context context, List<Webcam> webcams) {
        SharedPreferences prefs = context.getSharedPreferences(MeteoWidgetProvider.WIDGETS_PREF, 0);
        int idWebcam = prefs.getInt(WebcamWidgetProvider.WEBCAM_CURRENT_ID, 0);

        WebcamWidget webcamWidget = null;

        // cerco la webcam con l'id salvato nelle preference
        Webcam webcam = new Webcam();
        webcam.setId(idWebcam);
        int currentIndex = webcams.indexOf(webcam);
        if (currentIndex != -1) {

            webcam = webcams.get(currentIndex);
            webcamWidget = new WebcamWidget(webcam);

            // il link che è attualmente visualizzato
            int idLink = prefs.getInt(WebcamWidgetProvider.WEBCAM_CURRENT_LINK_INDEX, 0);
            webcamWidget.setLink(webcam.getLink());
            webcamWidget.setIdLink(idLink);
        }

        return webcamWidget;
    }

    /**
     * Restituisce la prima webcam valida da poter utilizzare per la visualizzazione nel widget.
     *
     * @param listaWebcam lista delle webcam presenti
     * @param webcamIds   lista di id configurati per il widget
     * @param fromIindex  indice di inizio ricerca rispetto alla lista degli id
     * @return webcam valida, <code>null</code> se non esiste
     */
    private static
    @Nullable
    Webcam getFirstAvailableWebcam(@NonNull List<Webcam> listaWebcam,
                                   @NonNull List<Integer> webcamIds, @NonNull int fromIindex) {

        if (webcamIds.isEmpty()) {
            return null;
        }

        // cerco la prima webcam dall'indice specificato alla fine
        for (int i = fromIindex + 1; i < webcamIds.size(); i++) {
            Webcam webcam = new Webcam();
            webcam.setId(webcamIds.get(i));
            int index = listaWebcam.indexOf(webcam);
            if (index != -1) {
                return listaWebcam.get(index);
            }
        }

        // se non la trovo prova dall'inizio
        for (int i = 0; i <= fromIindex; i++) {
            Webcam webcam = new Webcam();
            webcam.setId(webcamIds.get(i));
            int index = listaWebcam.indexOf(webcam);
            if (index != -1) {
                return listaWebcam.get(index);
            }
        }

        return null;
    }

    /**
     * Carica la webcam successiva da visualizzare nel widget.
     *
     * @param context context
     * @param webcams webcam presenti
     * @return webcam da caricare
     */
    public static
    @Nullable
    WebcamWidget getNextWebcam(Context context, @NonNull Webcams webcams) {

        List<Webcam> listaWebcam = webcams.getWebcams();

        WebcamWidgetsSettingsManager webcamSettings = new WebcamWidgetsSettingsManager(context);
        List<Integer> webcamWidgetIds = webcamSettings.getWebcamWidgetIds();

        WebcamWidget currentWebcamWidget = getCurrentWebcam(context, listaWebcam);
        WebcamWidget webcamWidget = null;
        if (currentWebcamWidget == null) {
            // se ci sono configurate delle webcam per i widget prendo la prima
            Webcam webcam = getFirstAvailableWebcam(listaWebcam, webcamWidgetIds, 0);
            if (!webcamWidgetIds.isEmpty() && webcam != null) {
                webcamWidget = new WebcamWidget(webcam);
                webcamWidget.setLink(webcam.getLink());
                webcamWidget.setIdLink(0);

                saveWebcam(webcamWidget, context);
            }

            return webcamWidget;
        }

        Webcam webcam = new Webcam();
        webcam.setId(currentWebcamWidget.getId());
        int index = listaWebcam.indexOf(webcam);
        if (index == -1) {
            webcam = null;
        } else {
            webcam = listaWebcam.get(index);
        }

        // verifico se la webcam ha un altro link
        if (webcam != null && 0 > currentWebcamWidget.getIdLink()) {
            webcamWidget = new WebcamWidget(webcam);
            webcamWidget.setLink(webcam.getLink());
            webcamWidget.setIdLink(currentWebcamWidget.getIdLink() + 1);
            saveWebcam(webcamWidget, context);
        } else {
            int indiceIdAttuale = webcamWidgetIds.indexOf(currentWebcamWidget.getId());
            if (indiceIdAttuale == -1) {
                indiceIdAttuale = 0;
            }

            webcam = getFirstAvailableWebcam(listaWebcam, webcamWidgetIds, indiceIdAttuale);
            if (webcam != null) {
                webcamWidget = new WebcamWidget(webcam);
                webcamWidget.setLink(webcam.getLink());
                webcamWidget.setIdLink(0);
                saveWebcam(webcamWidget, context);
            }
        }

        return webcamWidget;
    }

    /**
     * Salva i valori della webcam sull private preference dei widget.
     *
     * @param webcamWidget webcam di riferimento
     * @param context      context
     */
    private static void saveWebcam(WebcamWidget webcamWidget, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MeteoWidgetProvider.WIDGETS_PREF, 0);
        Editor edit = prefs.edit();
        edit.putInt(WebcamWidgetProvider.WEBCAM_CURRENT_ID, webcamWidget.getId());
        edit.putInt(WebcamWidgetProvider.WEBCAM_CURRENT_LINK_INDEX, webcamWidget.getIdLink());
        edit.apply();
    }

}
