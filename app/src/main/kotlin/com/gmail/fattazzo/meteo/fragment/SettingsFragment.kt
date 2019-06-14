/*
 * Project: meteo
 * File: SettingsFragment.kt
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

package com.gmail.fattazzo.meteo.fragment

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.SharedPreferences
import android.preference.EditTextPreference
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.text.InputType
import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.preferences.imagewidget.ZoneWidgetPreference
import com.gmail.fattazzo.meteo.preferences.numberpicker.NumberPickerDialogPreference
import com.gmail.fattazzo.meteo.utils.dialog.DialogBuilder
import com.gmail.fattazzo.meteo.utils.dialog.DialogType
import org.androidannotations.annotations.AfterPreferences
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.PreferenceByKey
import org.androidannotations.annotations.PreferenceScreen


/**
 * @author fattazzo
 *
 *
 * date: 09/giu/2014
 */
@PreferenceScreen(R.xml.preferences)
@EFragment
open class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    @PreferenceByKey(R.string.pref_key_stazMeteoGraphPointRadius)
    internal lateinit var stazMeteoGraphPointRadiusPref: Preference

    @AfterPreferences
    fun initPrefs() {
        updateAllPrefsSummary()


    }

    override fun onResume() {
        super.onResume()

        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this@SettingsFragment)
    }

    override fun onPause() {
        super.onPause()

        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this@SettingsFragment)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String) {
        updatePreferenceSummary(findPreference(key))

        checkAndThrowWidgetSettingsChange(key)
    }

    private fun checkAndThrowWidgetSettingsChange(key: String) {
        if (activity != null) {
            when {
                key == activity.resources.getString(R.string.pref_key_iconsTheme) -> {
                    DialogBuilder(activity, DialogType.BUTTONS).apply {
                        titleResId = R.string.pref_icons_title
                        messageResId = R.string.pref_icons_message
                        positiveText = android.R.string.ok
                    }.build().show()
                    activity.sendBroadcast(Intent().apply {
                        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    })
                }
                PREF_WIDGETS.contains(key) -> {
                    activity.sendBroadcast(Intent().apply {
                        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    })
                }
            }
        }
    }

    private fun updateAllPrefsSummary() {
        updatePreferenceSummary(stazMeteoGraphPointRadiusPref)
    }

    /**
     * Aggiorna le preference.
     *
     * @param preference preference da aggiornare
     */
    private fun updatePreferenceSummary(preference: Preference?) {
        when (preference) {
            is ListPreference -> {
                val listPreference = preference as ListPreference?
                listPreference!!.summary = listPreference.entry
            }
            is NumberPickerDialogPreference -> {
                val dialogPreference = preference as NumberPickerDialogPreference?
                val value = dialogPreference!!.value
                dialogPreference.summary = (activity
                        .getString(R.string.weather_stations_pref_graph_points_radius_desc)
                        + "("
                        + value.toString()
                        + ")")
            }
            is EditTextPreference -> {
                val textPreference = preference as EditTextPreference?
                var value = textPreference!!.text
                val inputType = textPreference.editText.inputType
                if (inputType and InputType.TYPE_NUMBER_VARIATION_PASSWORD != 0 || inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD != 0) {
                    value = value.replace(".".toRegex(), "*")
                }
                textPreference.summary = value
            }
            is ZoneWidgetPreference -> {
                val zoneWidgetPreference = preference as ZoneWidgetPreference?
                zoneWidgetPreference!!.summary = (activity.getString(R.string.widgets_pref_zone_summary) + " "
                        + zoneWidgetPreference.zoneValue)
            }
        }
    }

    companion object {

        private val PREF_WIDGETS = ArrayList<String>()

        init {
            PREF_WIDGETS.add(Config.WIDGETS_BACKGROUND)
            PREF_WIDGETS.add(Config.WIDGETS_TEXT_COLOR)
            PREF_WIDGETS.add(Config.WIDGETS_UPDATE_INTERVAL)
            PREF_WIDGETS.add(Config.WIDGETS_ZONE)
            PREF_WIDGETS.add(Config.WIDGETS_WEBCAM_UPDATE_INTERVAL)
        }
    }
}
