/*
 * Project: meteo
 * File: IconsRetriever.kt
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

package com.gmail.fattazzo.meteo.utils.icons.retirever

import android.util.Log
import com.gmail.fattazzo.meteo.utils.icons.WeatherIconEnum

/**
 * @author fattazzo
 *         <p/>
 *         date: 05/06/19
 */
abstract class IconsRetriever {

    open fun getIcon(iconId: Int?): Int? {
        var icona: Int? = null

        WeatherIconEnum.values().forEach {
            if (it.handleIconId(iconId)) {
                icona = getIconForType(it)
                return@forEach
            }
        }
        if (icona == null) {
            Log.d(this::class.java.name, "Incona non trovata, id: $iconId")
        }
        return icona
    }

    open fun overrideColorForWidget(): Boolean = false

    abstract fun getIconForType(weatherIconEnum: WeatherIconEnum): Int?

    abstract fun getId(): String

    /**
     * Icon demo per la selezione del tema nelle preference
     */
    open fun getIconDemo1(): Int = getIconForType(WeatherIconEnum.GIORNO_SOLE)!!
    open fun getIconDemo2(): Int = getIconForType(WeatherIconEnum.GIORNO_SOLE_PIOGGIA)!!
    open fun getIconDemo3(): Int = getIconForType(WeatherIconEnum.GIORNO_SOLE_FULMINI_PIOGGIA)!!
}