/*
 * Project: meteo
 * File: Localita.kt
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
import com.activeandroid.util.SQLiteUtils

/**
 * @author fattazzo
 *         <p/>
 *         date: 20/11/17
 */
@Table(name = "localita")
class Localita() : Model() {

    @Column
    var nome: String? = null

    @Column
    var comune: String? = null

    @Column
    var quota: Int? = null

    @Column
    var latitudine: Double? = null

    @Column
    var longitudine: Double? = null

    constructor(nome: String?) : this() {
        this.nome = nome
    }

    companion object {

        fun recreateTable() {
            val tableInfo = Cache.getTableInfo(Localita::class.java)
            try {
                ActiveAndroid.execSQL(String.format("DELETE FROM sqlite_sequence WHERE name='%s';", tableInfo.tableName))
                ActiveAndroid.execSQL(String.format("DROP TABLE %s;", tableInfo.tableName))
            } finally {
                ActiveAndroid.execSQL(SQLiteUtils.createTableDefinition(tableInfo))
            }
        }
    }

    override fun toString(): String = nome.orEmpty()
}