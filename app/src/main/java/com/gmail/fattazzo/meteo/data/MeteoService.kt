/*
 * Project: meteo
 * File: MeteoService.kt
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

package com.gmail.fattazzo.meteo.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.data.db.AppDatabase
import com.gmail.fattazzo.meteo.data.db.entities.Localita
import com.gmail.fattazzo.meteo.data.opendata.json.api.BollettinoProbabilisticoService
import com.gmail.fattazzo.meteo.data.opendata.json.api.LocalitaService
import com.gmail.fattazzo.meteo.data.opendata.json.api.PrevisioneLocalitaService
import com.gmail.fattazzo.meteo.data.opendata.json.model.bollettinoprobabilistico.BollettinoProbabilistico
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.PrevisioneLocalita

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/09/19
 */
class MeteoService(private val context: Context) {

    companion object {
        val TAG = MeteoService::class.simpleName
    }

    fun caricaPrevisioneLocalita(
        codiceLocalita: String,
        suppressException: Boolean = false
    ): Result<PrevisioneLocalita> {

        val result: Result<PrevisioneLocalita>
        result = try {
            val previsione = PrevisioneLocalitaService.caricaPrevisione(codiceLocalita)

            if (previsione != null) {
                Result(previsione)
            } else {
                Result(null, true, null)
            }

        } catch (e: Exception) {
            if (!suppressException) showError(e)
            Result(null, true, e)
        }

        return result
    }

    fun caricaLocalita(forceDownload: Boolean = false): List<Localita> {

        val localitaDao = AppDatabase(context).localitaDao()

        if (localitaDao.count() == 0 || forceDownload) {

            val localitaCaricate: List<com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Localita> =
                try {
                    LocalitaService.caricaLocalita().orEmpty()
                } catch (e: Exception) {
                    Log.e(TAG, "Errore durante il caricamento delle località", e)
                    Crashlytics.logException(e)
                    listOf()
                }

            try {
                localitaDao.deleteAll()
                localitaCaricate.map { Localita(it) }.forEach { localitaDao.insert(it) }
            } catch (e: Exception) {
                showError(e)
            }
        }
        return localitaDao.loadAll()
    }

    fun caricaBollettinoProbabilistico(): Result<BollettinoProbabilistico> {

        val result: Result<BollettinoProbabilistico>
        result = try {
            val bollettino = BollettinoProbabilisticoService.caricaBollettino()

            if (bollettino != null) {
                Result(bollettino)
            } else {
                Result(null, true, null)
            }

        } catch (e: Exception) {
            showError(e)
            Result(null, true, e)
        }

        return result
    }

    private fun showError(e: Exception? = null) {

        e?.let {
            Crashlytics.logException(it)
            Log.e(Companion.TAG, "Errore", it)
        }

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, context.getString(R.string.service_error), Toast.LENGTH_LONG)
                .show()
        }
    }
}