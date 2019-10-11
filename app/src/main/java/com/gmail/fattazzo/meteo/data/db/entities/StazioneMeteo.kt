/*
 * Project: meteo
 * File: StazioneMeteo.kt
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
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/11/17
 */
@Entity(tableName = "stazioni_meteo")
data class StazioneMeteo(
    @PrimaryKey val id: Long?, val codice: String?,
    val nome: String?,
    val nomeBreve: String?,
    val quota: Int?,
    val latitudine: Double?,
    val longitudine: Double?,
    val est: Double?,
    val nord: Double?,
    val startdate: Date?,
    val enddate: Date?
) {

    constructor(codice: String?, nome: String?) : this(
        -1L,
        codice,
        nome,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    override fun toString(): String = nome.orEmpty()

    companion object {

        fun fromStazioneXML(stazioneXML: com.gmail.fattazzo.meteo.data.stazioni.meteo.domain.StazioneMeteo): StazioneMeteo {
            return StazioneMeteo(
                null,
                stazioneXML.codice,
                stazioneXML.nome,
                stazioneXML.nomeBreve,
                stazioneXML.quota,
                stazioneXML.latitudine,
                stazioneXML.longitudine,
                stazioneXML.est,
                stazioneXML.nord,
                stazioneXML.startdate,
                stazioneXML.enddate
            )
        }
    }
}