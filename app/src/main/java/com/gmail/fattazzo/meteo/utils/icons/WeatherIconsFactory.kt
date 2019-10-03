/*
 * Project: meteo
 * File: WeatherIconsFactory.kt
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

package com.gmail.fattazzo.meteo.utils.icons

import android.content.Context
import android.preference.PreferenceManager
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.utils.icons.retirever.FlatColorIconsRetriever
import com.gmail.fattazzo.meteo.utils.icons.retirever.IconsRetriever
import com.gmail.fattazzo.meteo.utils.icons.retirever.LineIconsRetriever
import com.gmail.fattazzo.meteo.utils.icons.retirever.MeteoTrentinoIconsRetriever

/**
 * @author fattazzo
 *         <p/>
 *         date: 05/06/19
 */
object WeatherIconsFactory {

    private val set1IconsRetriever by lazy { FlatColorIconsRetriever() }
    private val meteoTrentinoRetriever by lazy { MeteoTrentinoIconsRetriever() }
    private val lineIconsRetriever by lazy { LineIconsRetriever() }

    fun getIconsRetriever(context: Context): IconsRetriever {

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        return getIconsRetrieverForThemeKey(prefs.getString(context.resources.getString(R.string.pref_key_iconsTheme), "meteo_trentino")!!)
    }

    fun getIconsRetriever(themeKey: String): IconsRetriever = getIconsRetrieverForThemeKey(themeKey)

    private fun getIconsRetrieverForThemeKey(key: String): IconsRetriever = when (key) {
        meteoTrentinoRetriever.getId() -> meteoTrentinoRetriever
        set1IconsRetriever.getId() -> set1IconsRetriever
        lineIconsRetriever.getId() -> lineIconsRetriever
        else -> meteoTrentinoRetriever
    }

}