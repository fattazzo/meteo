/*
 * Project: meteo
 * File: WebcamWidgetManager.kt
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

package com.gmail.fattazzo.meteo.widget.providers.webcam

import android.content.Context
import com.gmail.fattazzo.meteo.data.webcam.Webcam
import com.gmail.fattazzo.meteo.data.webcam.Webcams
import com.gmail.fattazzo.meteo.widget.providers.MeteoAppWidgetProvider

/**
 * @author fattazzo
 *
 *
 * date: 24/lug/2015
 */
object WebcamWidgetManager {

    /**
     * Webcam attualmente visualizzata dal widget.
     *
     * @param context context
     * @param webcams webcams
     * @return webcam visualizzata
     */
    private fun getCurrentWebcam(context: Context, webcams: List<Webcam>): WebcamWidget? {
        val prefs = context.getSharedPreferences(MeteoAppWidgetProvider.WIDGETS_PREF, 0)
        val idWebcam = prefs.getInt(WebcamWidgetProvider.WEBCAM_CURRENT_ID, 0)

        var webcamWidget: WebcamWidget? = null

        // cerco la webcam con l'id salvato nelle preference
        var webcam = Webcam()
        webcam.id = idWebcam
        val currentIndex = webcams.indexOf(webcam)
        if (currentIndex != -1) {

            webcam = webcams[currentIndex]
            webcamWidget = WebcamWidget(webcam)

            // il link che è attualmente visualizzato
            val idLink = prefs.getInt(WebcamWidgetProvider.WEBCAM_CURRENT_LINK_INDEX, 0)
            webcamWidget.link = webcam.link
            webcamWidget.idLink = idLink
        }

        return webcamWidget
    }

    /**
     * Restituisce la prima webcam valida da poter utilizzare per la visualizzazione nel widget.
     *
     * @param listaWebcam lista delle webcam presenti
     * @param webcamIds   lista di id configurati per il widget
     * @param fromIindex  indice di inizio ricerca rispetto alla lista degli id
     * @return webcam valida, `null` se non esiste
     */
    private fun getFirstAvailableWebcam(
        listaWebcam: List<Webcam>,
        webcamIds: List<Int>, fromIindex: Int
    ): Webcam? {

        if (webcamIds.isEmpty()) {
            return null
        }

        // cerco la prima webcam dall'indice specificato alla fine
        for (i in fromIindex + 1 until webcamIds.size) {
            val webcam = Webcam()
            webcam.id = webcamIds[i]
            val index = listaWebcam.indexOf(webcam)
            if (index != -1) {
                return listaWebcam[index]
            }
        }

        // se non la trovo prova dall'inizio
        for (i in 0..fromIindex) {
            val webcam = Webcam()
            webcam.id = webcamIds[i]
            val index = listaWebcam.indexOf(webcam)
            if (index != -1) {
                return listaWebcam[index]
            }
        }

        return null
    }

    /**
     * Carica la webcam successiva da visualizzare nel widget.
     *
     * @param context context
     * @param webcams webcam presenti
     * @return webcam da caricare
     */
    fun getNextWebcam(context: Context, webcams: Webcams, webcamIds: List<Int>): WebcamWidget? {

        val listaWebcam = webcams.webcams

        val currentWebcamWidget = getCurrentWebcam(context, listaWebcam)
        var webcamWidget: WebcamWidget? = null
        if (currentWebcamWidget == null) {
            // se ci sono configurate delle webcam per i widget prendo la prima
            val webcam = getFirstAvailableWebcam(listaWebcam, webcamIds, 0)
            if (webcamIds.isNotEmpty() && webcam != null) {
                webcamWidget = WebcamWidget(webcam)
                webcamWidget.link = webcam.link
                webcamWidget.idLink = 0

                saveWebcam(webcamWidget, context)
            }

            return webcamWidget
        }

        var webcam: Webcam? = Webcam()
        webcam!!.id = currentWebcamWidget.id
        val index = listaWebcam.indexOf(webcam)
        webcam = if (index == -1) {
            null
        } else {
            listaWebcam[index]
        }

        // verifico se la webcam ha un altro link
        if (webcam != null && 0 > currentWebcamWidget.idLink) {
            webcamWidget = WebcamWidget(webcam)
            webcamWidget.link = webcam.link
            webcamWidget.idLink = currentWebcamWidget.idLink + 1
            saveWebcam(webcamWidget, context)
        } else {
            var indiceIdAttuale = webcamIds.indexOf(currentWebcamWidget.id)
            if (indiceIdAttuale == -1) {
                indiceIdAttuale = 0
            }

            webcam = getFirstAvailableWebcam(listaWebcam, webcamIds, indiceIdAttuale)
            if (webcam != null) {
                webcamWidget = WebcamWidget(webcam)
                webcamWidget.link = webcam.link
                webcamWidget.idLink = 0
                saveWebcam(webcamWidget, context)
            }
        }

        return webcamWidget
    }

    /**
     * Salva i valori della webcam sull private preference dei widget.
     *
     * @param webcamWidget webcam di riferimento
     * @param context      context
     */
    private fun saveWebcam(webcamWidget: WebcamWidget, context: Context) {
        val prefs = context.getSharedPreferences(MeteoAppWidgetProvider.WIDGETS_PREF, 0)
        val edit = prefs.edit()
        edit.putInt(WebcamWidgetProvider.WEBCAM_CURRENT_ID, webcamWidget.id)
        edit.putInt(WebcamWidgetProvider.WEBCAM_CURRENT_LINK_INDEX, webcamWidget.idLink)
        edit.apply()
    }

}
/**
 * Costruttore.
 */
