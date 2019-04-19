/*
 * Project: meteo
 * File: ApplicationPreferencesManager.kt
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

import android.content.SharedPreferences
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.sharedpreferences.Pref

/**
 * @author fattazzo
 *         <p/>
 *         date: 27/10/17
 */
@EBean(scope = EBean.Scope.Singleton)
open class ApplicationPreferencesManager {

    @Pref
    lateinit var pref: ApplicationPreferences_


    /**
     * @return restituisce l'ultimo nome della versione utilizzata
     */
    fun getLastRunVersionName(): String = pref.lastRunVersionName().getOr("0")

    fun getLocalita(): String = pref.localita().getOr("TRENTO")

    // ---------------- Stationi meteo ---------------------------------
    fun showStazMeteoGraphShowSeriesBackground(): Boolean =
            pref.stazMeteoGraphShowSeriesBg().getOr(true)

    fun showStazMeteoGraphPoint(): Boolean = pref.stazMeteoGraphShowPoint().getOr(true)

    // -2 dovuto alla libreria precedente dove i punti avevano il range 1-10. Il punto 5 corrisponde al 3 per l'attuale libreria
    fun getStazMeteoGraphPointRadius(): Int = pref.stazMeteoGraphPointRadius().getOr(5) - 2

    fun getCodiceStazioneMeteoWidget(): String = pref.codiceStazioneMeteoWidget().getOr("")

    fun setCodiceStazioneMeteoWidget(codice: String) = getPrefs().edit().putString("codiceStazioneMeteoWidget", codice).commit()

    // ----------- Widget ----------------------------------------------
    fun getPrefs(): SharedPreferences = pref.sharedPreferences
}