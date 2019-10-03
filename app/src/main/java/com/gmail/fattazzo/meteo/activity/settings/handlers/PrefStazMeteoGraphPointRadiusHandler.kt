/*
 * Project: meteo
 * File: PrefStazMeteoGraphPointRadiusHandler.kt
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

package com.gmail.fattazzo.meteo.activity.settings.handlers

import android.content.Context
import androidx.preference.Preference
import androidx.preference.SeekBarPreference
import com.gmail.fattazzo.meteo.R

/**
 * @author fattazzo
 *         <p/>
 *         date: 01/10/19
 */
class PrefStazMeteoGraphPointRadiusHandler(preference: Preference) : PreferenceHandler(preference) {

    init {
        (preference as SeekBarPreference).min = 1
        preference.setOnPreferenceChangeListener { pref, newValue ->
            val progress = newValue as Int
            pref.summary = pref.context.getString(
                R.string.weather_stations_pref_graph_points_radius_desc,
                progress
            )
            true
        }
    }

    override fun handle(context: Context) {
        // No action to handle
    }

    override fun updatesummary(context: Context) {

        val progress = (preference as SeekBarPreference).value

        preference.summary =
            context.getString(R.string.weather_stations_pref_graph_points_radius_desc, progress)
    }
}