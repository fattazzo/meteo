/*
 * Project: meteo
 * File: LineIconsRetriever.kt
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
 *         date: 04/06/19
 */
class LineIconsRetriever : IconsRetriever() {

    override fun getId(): String = "line"

    override fun getIconForType(weatherIconEnum: WeatherIconEnum): Int? =
            when (weatherIconEnum) {
                WeatherIconEnum.COPERTO_FULMINI -> R.drawable.ic_w_line_coperto_fulmini
                WeatherIconEnum.COPERTO_FULMINI_NEVE -> R.drawable.ic_w_line_coperto_fulmini_neve
                WeatherIconEnum.COPERTO_FULMINI_NEVE_PIOGGIA -> R.drawable.ic_w_line_coperto_fulmini_neve_pioggia
                WeatherIconEnum.COPERTO_FULMINI_PIOGGIA -> R.drawable.ic_w_line_coperto_fulmini_pioggia
                WeatherIconEnum.COPERTO_NEVE -> R.drawable.ic_w_line_coperto_neve
                WeatherIconEnum.COPERTO_NEVE_LEGGERA -> R.drawable.ic_w_line_coperto_neve_leggera
                WeatherIconEnum.COPERTO_NEVE_PIOGGIA -> R.drawable.ic_w_line_coperto_neve_pioggia
                WeatherIconEnum.COPERTO_NEVE_PIOGGIA_LEGGERA -> R.drawable.ic_w_line_coperto_neve_pioggia_leggera
                WeatherIconEnum.COPERTO_NUVOLA -> R.drawable.ic_w_line_coperto_nuvola
                WeatherIconEnum.COPERTO_PIOGGIA -> R.drawable.ic_w_line_coperto_nuvola_pioggia
                WeatherIconEnum.COPERTO_PIOGGIA_LEGGERA -> R.drawable.ic_w_line_coperto_pioggia_leggera
                WeatherIconEnum.NOTTE_LUNA -> R.drawable.ic_w_line_luna
                WeatherIconEnum.NOTTE_LUNA_FULMINI -> R.drawable.ic_w_line_luna_fulmini
                WeatherIconEnum.NOTTE_LUNA_FULMINI_NEVE -> R.drawable.ic_w_line_luna_fulmini_neve
                WeatherIconEnum.NOTTE_LUNA_FULMINI_NEVE_PIOGGIA -> R.drawable.ic_w_line_luna_fulmini_neve_pioggia
                WeatherIconEnum.NOTTE_LUNA_FULMINI_PIOGGIA -> R.drawable.ic_w_line_luna_fulmini_pioggia
                WeatherIconEnum.NOTTE_LUNA_NEBBIA -> R.drawable.ic_w_line_luna_nebbia
                WeatherIconEnum.NOTTE_LUNA_NEVE -> R.drawable.ic_w_line_luna_neve
                WeatherIconEnum.NOTTE_LUNA_NEVE_PIOGGIA -> R.drawable.ic_w_line_luna_neve_pioggia
                WeatherIconEnum.NOTTE_LUNA_NUVOLA -> R.drawable.ic_w_line_luna_nuvola
                WeatherIconEnum.NOTTE_LUNA_PIOGGIA_LEGGERA -> R.drawable.ic_w_line_luna_pioggia_leggera
                WeatherIconEnum.NOTTE_LUNA_PIOGGIA -> R.drawable.ic_w_line_luna_pioggia
                WeatherIconEnum.GIORNO_SOLE -> R.drawable.ic_w_line_sole
                WeatherIconEnum.GIORNO_SOLE_FULMINI -> R.drawable.ic_w_line_sole_fulmini
                WeatherIconEnum.GIORNO_SOLE_FULMINI_NEVE -> R.drawable.ic_w_line_sole_fulmini_neve
                WeatherIconEnum.GIORNO_SOLE_FULMINI_NEVE_PIOGGIA -> R.drawable.ic_w_line_sole_fulmini_neve_pioggia
                WeatherIconEnum.GIORNO_SOLE_FULMINI_PIOGGIA -> R.drawable.ic_w_line_sole_fulmini_pioggia
                WeatherIconEnum.GIORNO_SOLE_NEBBIA -> R.drawable.ic_w_line_sole_nebbia
                WeatherIconEnum.GIORNO_SOLE_NEVE -> R.drawable.ic_w_line_sole_neve
                WeatherIconEnum.GIORNO_SOLE_NEVE_PIOGGIA -> R.drawable.ic_w_line_sole_neve_pioggia
                WeatherIconEnum.GIORNO_SOLE_NUVOLA -> R.drawable.ic_w_line_sole_nuvola
                WeatherIconEnum.GIORNO_SOLE_PIOGGIA_LEGGERA -> R.drawable.ic_w_line_sole_pioggia_leggera
                WeatherIconEnum.GIORNO_SOLE_PIOGGIA -> R.drawable.ic_w_line_sole_pioggia
            }

    override fun overrideColorForWidget(): Boolean = true
}