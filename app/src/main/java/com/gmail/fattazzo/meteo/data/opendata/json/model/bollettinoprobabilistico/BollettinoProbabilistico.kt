/*
 * Project: meteo
 * File: BollettinoProbabilistico.kt
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

package com.gmail.fattazzo.meteo.data.opendata.json.model.bollettinoprobabilistico

import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 27/10/17
 */
class BollettinoProbabilistico {

    var dataPubblicazione: Date? = null

    var giorni: List<Giorno>? = null

    fun fenomeniPresenti(): Set<String> {
        val elencoFenomeni = TreeSet<String>()

        giorni.orEmpty()
                .flatMap { it.fasce.orEmpty() }
                .flatMap { it.fenomeni.orEmpty() }
                .mapTo(elencoFenomeni) { it.nome.orEmpty() }

        return elencoFenomeni
    }
}