/*
 * Project: meteo
 * File: Localita.kt
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

package com.gmail.fattazzo.meteo.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Localita

/**
 * @author fattazzo
 *         <p/>
 *         date: 20/11/17
 */
@Entity(tableName = "localita")
data class Localita(
    @PrimaryKey(autoGenerate = true) var id: Long?, val nome: String?,
    val comune: String?,
    val quota: Int?,
    val latitudine: Double?,
    val longitudine: Double?
) {

    constructor(localita: Localita) : this(
        null,
        localita.nome,
        localita.comune,
        localita.quota,
        localita.latitudine,
        localita.longitudine
    )

    override fun toString(): String = nome.orEmpty()
}