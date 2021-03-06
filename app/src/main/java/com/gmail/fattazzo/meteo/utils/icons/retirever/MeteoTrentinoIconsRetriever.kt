/*
 * Project: meteo
 * File: MeteoTrentinoIconsRetriever.kt
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

import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.utils.icons.WeatherIconEnum

/**
 * @author fattazzo
 *         <p/>
 *         date: 05/06/19
 */
class MeteoTrentinoIconsRetriever : IconsRetriever() {

    override fun getIconForType(weatherIconEnum: WeatherIconEnum): Int? = null

    override fun getId(): String = "meteo_trentino"

    override fun getIconDemo1(): Int = R.drawable.ico_018

    override fun getIconDemo2(): Int = R.drawable.ico_017

    override fun getIconDemo3(): Int = R.drawable.ico_021

    override fun getIcon(iconId: Int?): Int? = null
}