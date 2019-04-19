/*
 * Project: meteo
 * File: StazioneMeteo.kt
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

package com.gmail.fattazzo.meteo.db

import com.activeandroid.ActiveAndroid
import com.activeandroid.Cache
import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table
import com.activeandroid.query.Select
import com.activeandroid.util.SQLiteUtils
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/11/17
 */
@Table(name = "stazioniMeteo")
class StazioneMeteo : Model() {

    @Column
    var codice: String? = null

    @Column
    var nome: String? = null

    @Column
    var nomeBreve: String? = null

    @Column
    var quota: Int? = null

    @Column
    var latitudine: Double? = null

    @Column
    var longitudine: Double? = null

    @Column
    var est: Double? = null

    @Column
    var nord: Double? = null

    @Column
    var startdate: Date? = null

    @Column
    var enddate: Date? = null

    companion object {

        fun fromStazioneXML(stazioneXML: com.gmail.fattazzo.meteo.domain.xml.stazioni.meteo.StazioneMeteo): StazioneMeteo {
            val stazioneDB = StazioneMeteo()
            stazioneDB.codice = stazioneXML.codice
            stazioneDB.nome = stazioneXML.nome
            stazioneDB.nomeBreve = stazioneXML.nomeBreve
            stazioneDB.quota = stazioneXML.quota
            stazioneDB.latitudine = stazioneXML.latitudine
            stazioneDB.longitudine = stazioneXML.longitudine
            stazioneDB.est = stazioneXML.est
            stazioneDB.nord = stazioneXML.nord
            stazioneDB.startdate = stazioneXML.startdate
            stazioneDB.enddate = stazioneXML.enddate
            return stazioneDB
        }

        fun recreateTable() {
            val tableInfo = Cache.getTableInfo(StazioneMeteo::class.java)
            try {
                ActiveAndroid.execSQL(String.format("DELETE FROM sqlite_sequence WHERE name='%s';", tableInfo.tableName))
                ActiveAndroid.execSQL(String.format("DROP TABLE %s;", tableInfo.tableName))
            } finally {
                ActiveAndroid.execSQL(SQLiteUtils.createTableDefinition(tableInfo))
            }
        }

        fun deleteAll() {
            val tableInfo = Cache.getTableInfo(StazioneMeteo::class.java)
            ActiveAndroid.execSQL(String.format("DELETE FROM %s;", tableInfo.tableName))
            ActiveAndroid.execSQL(String.format("DELETE FROM sqlite_sequence WHERE name='%s';", tableInfo.tableName))
        }

        fun count(): Int = Select().from(StazioneMeteo::class.java).count()

        fun laodAll(caricaDisabilitate: Boolean = false): List<StazioneMeteo> {
            if (caricaDisabilitate) {
                return Select().from(StazioneMeteo::class.java)
                        .orderBy("nome")
                        .execute()
            }
            return Select().from(StazioneMeteo::class.java)
                    .where("enddate is null")
                    .orderBy("nome")
                    .execute()
        }
    }
}