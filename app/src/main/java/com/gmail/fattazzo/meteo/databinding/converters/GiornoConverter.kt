/*
 * Project: meteo
 * File: GiornoConverter.kt
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

package com.gmail.fattazzo.meteo.databinding.converters

import android.annotation.SuppressLint
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Giorni

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/10/19
 */
object GiornoConverter {

    @JvmStatic
    fun giornoTMin(giorno: Giorni?): String {
        return if (giorno?.tMinGiorno != null) {
            String.format("%d°C", giorno.tMinGiorno)
        } else {
            "-"
        }
    }

    @JvmStatic
    fun giornoTMax(giorno: Giorni?): String {
        return if (giorno?.tMaxGiorno != null) {
            String.format("%d°C", giorno.tMaxGiorno)
        } else {
            "-"
        }
    }

    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun giornoTesto(giorno: Giorni?): String = giorno?.testoGiorno.orEmpty().capitalize()

    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun giornoData(giorno: Giorni?): String =
        StringConverter.dateToString(giorno?.data, "EEEE dd/MM/yyyy")
}