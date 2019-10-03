/*
 * Project: meteo
 * File: SettingsActivity.kt
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

package com.gmail.fattazzo.meteo.activity.settings

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.settings.handlers.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 01/10/19
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private val preferencesList = mutableListOf<PreferenceHandler>()

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            initPreferencesHandler()

            // Update all summary
            context?.let { preferencesList.forEach { pref -> pref.updatesummary(it) } }

            PREF_WIDGETS_KEY.forEach {
                findPreference<Preference>(resources.getString(it))?.setOnPreferenceChangeListener { pref, _ ->
                    pref.context.sendBroadcast(Intent().apply {
                        action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    })
                    true
                }
            }
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            context?.let {
                preferencesList.firstOrNull { handler -> handler.getKey() == preference?.key }
                    ?.handle(it)
            }
            return super.onPreferenceTreeClick(preference)
        }

        private fun initPreferencesHandler() {
            val prefPrivacy: Preference? =
                findPreference(resources.getString(R.string.pref_key_privacy_policy))
            prefPrivacy?.let { preferencesList.add(PrefPrivacyHandler(it)) }

            val prefVersion: Preference? =
                findPreference(resources.getString(R.string.pref_key_app_version))
            prefVersion?.let { preferencesList.add(PrefAppVersionHandler(it)) }

            val prefStazMeteoGraphPointRadius: Preference? =
                findPreference(resources.getString(R.string.pref_key_stazMeteoGraphPointRadius))
            prefStazMeteoGraphPointRadius?.let {
                preferencesList.add(
                    PrefStazMeteoGraphPointRadiusHandler(it)
                )
            }

            val prefIconTheme: Preference? =
                findPreference(resources.getString(R.string.pref_key_iconsTheme))
            prefIconTheme?.let { preferencesList.add(PrefIconThemeHandler(it)) }
        }
    }

    companion object {
        private val PREF_WIDGETS_KEY =
            intArrayOf(R.string.pref_key_widgets_text_color, R.string.pref_key_widgets_background)
    }
}