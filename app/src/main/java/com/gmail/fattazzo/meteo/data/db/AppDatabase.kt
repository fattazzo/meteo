/*
 * Project: meteo
 * File: AppDatabase.kt
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

package com.gmail.fattazzo.meteo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gmail.fattazzo.meteo.data.db.converters.DateConverter
import com.gmail.fattazzo.meteo.data.db.dao.LocalitaDao
import com.gmail.fattazzo.meteo.data.db.dao.StazioniMeteoDao
import com.gmail.fattazzo.meteo.data.db.dao.StazioniValangheDao
import com.gmail.fattazzo.meteo.data.db.entities.Localita
import com.gmail.fattazzo.meteo.data.db.entities.StazioneMeteo
import com.gmail.fattazzo.meteo.data.db.entities.StazioneValanghe

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/10/19
 */
@Database(
    entities = [Localita::class, StazioneMeteo::class, StazioneValanghe::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun localitaDao(): LocalitaDao
    abstract fun stazioniMeteoDao(): StazioniMeteoDao
    abstract fun stazioniValangheDao(): StazioniValangheDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "meteo.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}