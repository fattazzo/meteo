/*
 * Project: meteo
 * File: WebcamWidgetsSettingsManager.kt
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

package com.gmail.fattazzo.meteo.preferences.widget.webcam

import android.content.Context
import android.content.Intent
import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager_
import com.gmail.fattazzo.meteo.preferences.widget.WidgetSettingsManager
import com.gmail.fattazzo.meteo.widget.providers.MeteoAppWidgetProvider
import java.util.*

/**
 * @author fattazzo
 *
 *
 * date: 24/lug/2015
 */
class WebcamWidgetsSettingsManager(private val context: Context) : WidgetSettingsManager {

    private val preferencesManager: ApplicationPreferencesManager

    /**
     * @return widget background
     */
    override val background: Int
        get() {

            val background = preferencesManager.getPrefs().getString(Config.WIDGETS_BACKGROUND, "widget_shape_transparent_40_black")

            return context.resources.getIdentifier(background, "drawable", context.packageName)
        }

    /**
     * @return widget text color
     */
    override val textColor: Int
        get() = preferencesManager.getPrefs().getInt(Config.WIDGETS_TEXT_COLOR, -0x1)

    /**
     * @return widget update interval
     */
    override val updateInterval: Int
        get() = Integer.parseInt(preferencesManager.getPrefs().getString(Config.WIDGETS_WEBCAM_UPDATE_INTERVAL, "1800000"))

    /**
     * @return id delle webcam da visualizzare nel widget
     */
    val webcamWidgetIds: MutableList<Int>
        get() {

            val stringIds = preferencesManager.getPrefs().getString(Config.WIDGETS_WEBCAM_IDS, "")
            val splittedIds = stringIds!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            return splittedIds
                    .filter { "" != it }
                    .mapTo(ArrayList()) { Integer.valueOf(it) }
        }

    init {
        preferencesManager = ApplicationPreferencesManager_.getInstance_(context)
    }

    /**
     * Aggiorna la lista degli id da visualizzare aggiungendo o togliendo il nuovo id a seconda che questo esista già o
     * no.
     *
     * @param idUpdate id da sincronizzare
     */
    fun updateWebcamWidgetIds(idUpdate: Int) {

        var ids = webcamWidgetIds
        /**
        val idx = ids.indexOf(idUpdate)
        if (idx == -1) {
            ids.add(idUpdate)
        } else {
            ids.removeAt(idx)
        }
        **/

        if(idUpdate !in ids) {
            ids.add(idUpdate)
        } else {
            ids.remove(idUpdate)
        }

        preferencesManager.getPrefs().edit().putString(Config.WIDGETS_WEBCAM_IDS, ids.distinct().joinToString(separator = ",")).commit()

        /**
        var stringIds = ""
        var separator = ""

        for (id in ids) {
            stringIds = stringIds + separator + id.toString()
            separator = ","
        }

        preferencesManager.getPrefs().edit().putString(Config.WIDGETS_WEBCAM_IDS, stringIds).commit()
        **/
        val intent = Intent(MeteoAppWidgetProvider.UPDATE)
        context.sendBroadcast(intent)
    }
}
