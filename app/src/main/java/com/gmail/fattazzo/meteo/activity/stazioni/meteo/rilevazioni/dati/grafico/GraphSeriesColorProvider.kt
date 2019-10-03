/*
 * Project: meteo
 * File: GraphSeriesColorProvider.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.meteo.rilevazioni.dati.grafico

import android.graphics.Color
import android.util.SparseIntArray

import com.github.mikephil.charting.utils.ColorTemplate

/**
 *
 * @author fattazzo
 *
 * date: 28/lug/2014
 */
internal object GraphSeriesColorProvider {

    private val COLORS: SparseIntArray = SparseIntArray(7)

    /**
     * Restituisce il colore per la serie richiesta.
     *
     * @param serieNumber
     * numero della serie
     * @return colore corrispondente
     */
    fun getSeriesColor(serieNumber: Int): Int {
        return if (serieNumber > COLORS.size()) {
            COLORS.get(0)
        } else {
            COLORS.get(serieNumber)
        }
    }

    init {
        COLORS.append(0, ColorTemplate.MATERIAL_COLORS[3])
        COLORS.append(1, ColorTemplate.MATERIAL_COLORS[2])
        COLORS.append(2, ColorTemplate.MATERIAL_COLORS[1])
        COLORS.append(3, ColorTemplate.MATERIAL_COLORS[0])
        COLORS.append(4, Color.MAGENTA)
        COLORS.append(5, Color.GRAY)
        COLORS.append(6, Color.BLACK)
    }
}
/**
 * Costruttore.
 */
