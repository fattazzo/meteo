/*
 * Project: meteo
 * File: PreferencesService.kt
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

package com.gmail.fattazzo.meteo.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.widget.providers.MeteoAppWidgetProvider
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/09/19
 */
class PreferencesService(private val context: Context) {

    private fun getPrefs(): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * @return restituisce l'ultimo nome della versione utilizzata
     */
    fun getLastRunVersionName(): String =
        getPrefs().getString(Config.KEY_LAST_RUN_VERSION_NAME, null) ?: "0"

    // ---------------- Località preferita -------------------------------

    fun getNomeLocalitaPreferita(): String =
        getPrefs().getString(Config.KEY_LOCALITA, null) ?: "TRENTO"

    fun setNomeLocalitaPreferita(value: String) =
        getPrefs().edit().putString(Config.KEY_LOCALITA, value).apply()

    // ---------------- Stationi meteo ---------------------------------
    fun showStazMeteoGraphShowSeriesBackground(): Boolean =
        getPrefs().getBoolean("stazMeteoGraphShowSeriesBg", true)

    fun showStazMeteoGraphPoint(): Boolean =
        getPrefs().getBoolean(context.getString(R.string.pref_key_stazMeteoGraphShowPoint), true)

    // -2 dovuto alla libreria precedente dove i punti avevano il range 1-10. Il punto 5 corrisponde al 3 per l'attuale libreria
    fun getStazMeteoGraphPointRadius(): Int =
        getPrefs().getInt(context.getString(R.string.pref_key_stazMeteoGraphPointRadius), 5) - 2

    fun getCodiceStazioneMeteoWidget(): String =
        getPrefs().getString("codiceStazioneMeteoWidget", null) ?: ""

    fun setCodiceStazioneMeteoWidget(codice: String) =
        getPrefs().edit().putString("codiceStazioneMeteoWidget", codice).commit()

    // ----------- Widget ----------------------------------------------
    fun getWidgetsBackground(): Int {
        val background = getPrefs().getString(
            context.getString(R.string.pref_key_widgets_background),
            "widget_shape_transparent_40_black"
        )

        return context.resources.getIdentifier(background, "drawable", context.packageName)
    }

    fun getWidgetsTextColor(): Int =
        getPrefs().getInt(context.getString(R.string.pref_key_widgets_text_color), -0x1)

    /**
     * @return id delle webcam da visualizzare nel widget
     */
    fun getWidgetWebcamIds(): MutableList<Int> {
        val stringIds = getPrefs().getString(Config.WIDGETS_WEBCAM_IDS, "")
        val splittedIds =
            stringIds!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        return splittedIds
            .filter { "" != it }
            .mapTo(ArrayList()) { Integer.valueOf(it) }
    }

    /**
     * Aggiorna la lista degli id da visualizzare aggiungendo o togliendo il nuovo id a seconda che questo esista già o
     * no.
     *
     * @param idUpdate id da sincronizzare
     */
    @SuppressLint("ApplySharedPref")
    fun updateWebcamWidgetIds(idUpdate: Int) {

        var ids = getWidgetWebcamIds()

        if (idUpdate !in ids) {
            ids.add(idUpdate)
        } else {
            ids.remove(idUpdate)
        }

        getPrefs().edit()
            .putString(Config.WIDGETS_WEBCAM_IDS, ids.distinct().joinToString(separator = ","))
            .commit()

        val intent = Intent(MeteoAppWidgetProvider.UPDATE)
        context.sendBroadcast(intent)
    }
}