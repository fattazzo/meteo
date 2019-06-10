/*
 * Project: meteo
 * File: WeatherIconEnum.kt
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


/**
 * @author fattazzo
 *         <p/>
 *         date: 04/06/19
 */
enum class WeatherIconEnum(private val meteoTrentinoIds: List<Int>) {

    COPERTO_FULMINI(listOf(801, 901)),
    COPERTO_FULMINI_NEVE(listOf(806, 807, 808, 906, 907, 908)),
    COPERTO_FULMINI_NEVE_PIOGGIA(listOf(805, 809, 810, 905, 909, 910)),
    COPERTO_FULMINI_PIOGGIA(listOf(802, 803, 804, 902, 903, 904)),
    COPERTO_NEVE(listOf(6, 106)),
    COPERTO_NEVE_LEGGERA(listOf(7, 8107, 108)),
    COPERTO_NEVE_PIOGGIA(listOf(9, 10, 109, 110)),
    COPERTO_NEVE_PIOGGIA_LEGGERA(listOf(5, 105)),
    COPERTO_NUVOLA(listOf(1, 101)),
    COPERTO_PIOGGIA(listOf(3, 4, 103, 104)),
    COPERTO_PIOGGIA_LEGGERA(listOf(2, 102)),

    NOTTE_LUNA(listOf(118)),
    NOTTE_LUNA_FULMINI(listOf(911, 919, 925, 955)),
    NOTTE_LUNA_FULMINI_NEVE(listOf(915, 916, 922, 924, 952)),
    NOTTE_LUNA_FULMINI_NEVE_PIOGGIA(listOf(914, 917, 920, 923, 953)),
    NOTTE_LUNA_FULMINI_PIOGGIA(listOf(912, 913, 921, 926, 951, 954)),
    NOTTE_LUNA_NEBBIA(listOf(155, 200)),
    NOTTE_LUNA_NEVE(listOf(115, 116, 122, 124, 152)),
    NOTTE_LUNA_NEVE_PIOGGIA(listOf(114, 117, 120, 123, 135, 153)),
    NOTTE_LUNA_NUVOLA(listOf(111, 119, 125)),
    NOTTE_LUNA_PIOGGIA_LEGGERA(listOf(112, 121, 126)),
    NOTTE_LUNA_PIOGGIA(listOf(113, 151, 154)),

    GIORNO_SOLE(listOf(18)),
    GIORNO_SOLE_FULMINI(listOf(811, 819, 825, 855)),
    GIORNO_SOLE_FULMINI_NEVE(listOf(815, 816, 822, 824, 852)),
    GIORNO_SOLE_FULMINI_NEVE_PIOGGIA(listOf(814, 817, 820, 823, 853)),
    GIORNO_SOLE_FULMINI_PIOGGIA(listOf(812, 813, 821, 826, 851, 854)),
    GIORNO_SOLE_NEBBIA(listOf(55)),
    GIORNO_SOLE_NEVE(listOf(15, 16, 22, 24, 52)),
    GIORNO_SOLE_NEVE_PIOGGIA(listOf(14, 17, 20, 23, 35, 53)),
    GIORNO_SOLE_NUVOLA(listOf(11, 19, 25)),
    GIORNO_SOLE_PIOGGIA_LEGGERA(listOf(12, 21, 26)),
    GIORNO_SOLE_PIOGGIA(listOf(13, 51, 54));

    fun handleIconId(iconId: Int?): Boolean = meteoTrentinoIds.contains(iconId ?: 0)
}