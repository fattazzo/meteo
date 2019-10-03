/*
 * Project: meteo
 * File: WebcamService.kt
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

package com.gmail.fattazzo.meteo.data

import android.content.Context
import android.util.Log
import com.gmail.fattazzo.meteo.data.webcam.Webcams
import com.gmail.fattazzo.meteo.preferences.PreferencesService
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * @author fattazzo
 *
 *
 * date: 13/lug/2015
 */
class WebcamService @Inject constructor(
    private val context: Context,
    private val preferencesService: PreferencesService
) {

    /**
     * Carica le webcam presenti.
     *
     * @return webcam caricate
     */
    fun caricaWebcam(): Webcams {

        var webcams: Webcams

        var inputReader: InputStreamReader? = null
        try {
            inputReader = InputStreamReader(context.assets.open("webcam.xml"))
            val strategy = AnnotationStrategy()
            val serializer = Persister(strategy)
            webcams = serializer.read(Webcams::class.java, inputReader, false)

            val webcamWidgetIds = preferencesService.getWidgetWebcamIds()
            webcams.webcams.forEach { webcam ->
                webcam.showInWidget = webcamWidgetIds.indexOf(webcam.id) != -1
            }
        } catch (e: Exception) {
            Log.e(TAG, "Errore durante la letture del file delle webcam")
            webcams = Webcams()
        } finally {
            inputReader?.close()
        }

        return webcams
    }

    companion object {

        private const val TAG = "WebcamXmlParser"
    }
}
