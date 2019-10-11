/*
 * Project: meteo
 * File: StazioniMeteoDao.kt
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

package com.gmail.fattazzo.meteo.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gmail.fattazzo.meteo.data.db.entities.StazioneMeteo

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/10/19
 */
@Dao
interface StazioniMeteoDao {

    @Query("DELETE from stazioni_meteo")
    fun deleteAll()

    @Query("SELECT count(id) from stazioni_meteo")
    fun count(): Int

    @Query("SELECT * from stazioni_meteo order by nome")
    fun loadAll(): List<StazioneMeteo>

    @Query("SELECT * from stazioni_meteo where enddate is null order by nome")
    fun loadAllAbilitate(): List<StazioneMeteo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stazioneMeteo: StazioneMeteo)

    @Query("select * from stazioni_meteo where codice = :codiceParam")
    fun load(codiceParam: String): StazioneMeteo
}