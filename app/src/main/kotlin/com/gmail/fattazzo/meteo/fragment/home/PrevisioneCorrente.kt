/*
 * Project: meteo
 * File: PrevisioneCorrente.kt
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

package com.gmail.fattazzo.meteo.fragment.home

import com.gmail.fattazzo.meteo.domain.json.previsione.PrevisioneLocalita
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author fattazzo
 *         <p/>
 *         date: 31/05/18
 */
object PrevisioneCorrente {

    var time: Long = 0
        private set

    var previsioneLocalita: PrevisioneLocalita? = null
        private set

    var localita: String? = null

    fun setPrevisione(previsioneLocalita: PrevisioneLocalita?, localita: String) {
        time = Calendar.getInstance().timeInMillis

        this.previsioneLocalita = previsioneLocalita
        this.localita = localita

        println("Previsione aggiornata per $localita")
    }

    fun clear() {
        this.previsioneLocalita = null
        this.localita = null
    }

    fun needToUpdate(localita: String, timeUnit: TimeUnit, timeValue: Long): Boolean {
        if (previsioneLocalita == null) {
            return true
        }

        if (localita != this.localita.orEmpty()) {
            return true
        }

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, (timeUnit.toSeconds(timeValue) * -1).toInt())
        if (time < calendar.timeInMillis) {
            return true
        }


        return false
    }
}