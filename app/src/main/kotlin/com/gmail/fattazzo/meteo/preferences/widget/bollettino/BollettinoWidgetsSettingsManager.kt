/*
 * Project: meteo
 * File: BollettinoWidgetsSettingsManager.kt
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

package com.gmail.fattazzo.meteo.preferences.widget.bollettino

import android.content.Context

import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager_
import com.gmail.fattazzo.meteo.preferences.imagewidget.ZoneWidgetPreference
import com.gmail.fattazzo.meteo.preferences.widget.WidgetSettingsManager

/**
 * @author fattazzo
 *
 *
 * date: 26/ago/2014
 */
class BollettinoWidgetsSettingsManager
/**
 * Costruttore.
 *
 * @param context context
 */
(private val context: Context) : WidgetSettingsManager {

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
     * @return widget text color
     */
    val zone: String
        get() = preferencesManager.getPrefs().getString(Config.WIDGETS_ZONE, ZoneWidgetPreference.DEFAULT_VALUE)

    init {
        preferencesManager = ApplicationPreferencesManager_.getInstance_(context)
    }
}
