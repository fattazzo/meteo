/*
 * Project: meteo
 * File: StazioniService.kt
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
import com.gmail.fattazzo.meteo.data.stazioni.meteo.StazioniMeteoDownloader
import com.gmail.fattazzo.meteo.data.stazioni.meteo.domain.datistazione.DatiStazione
import com.gmail.fattazzo.meteo.data.stazioni.valanghe.StazioniValangheDownloader
import com.gmail.fattazzo.meteo.db.StazioneMeteo
import com.gmail.fattazzo.meteo.db.StazioneValanghe


/**
 * @author fattazzo
 *         <p/>
 *         date: 01/10/19
 */
class StazioniService(private val context: Context) {

    companion object {
        val TAG = StazioniService::class.simpleName
    }

    fun caricaAnagraficaStazioniMeteo(
        caricaDisabilitate: Boolean,
        forceDownload: Boolean
    ): Result<List<StazioneMeteo>> {

        val stazioniDB = StazioneMeteo.laodAll(caricaDisabilitate)

        return if (stazioniDB.isEmpty() || forceDownload) {
            return try {
                val stazioneDownloaded = StazioniMeteoDownloader().downloadAnagrafica()

                StazioneMeteo.recreateTable()
                stazioneDownloaded.map { StazioneMeteo.fromStazioneXML(it) }.forEach { it.save() }

                Result(StazioneMeteo.laodAll(caricaDisabilitate))
            } catch (e: Exception) {
                showError(e)
                Result(null, true, e)
            }
        } else {
            Result(stazioniDB)
        }
    }

    fun caricaDatiStazione(
        codiceStazione: String,
        suppressError: Boolean = false
    ): Result<DatiStazione> {

        return try {
            val datiStazione = StazioniMeteoDownloader().caricaDatiStazione(codiceStazione)

            Result(datiStazione)
        } catch (e: Exception) {
            if (!suppressError) {
                showError(e)
            }
            Result(null, true, e)
        }
    }

    fun caricaAnagraficaStazioniValanghe(forceDownload: Boolean): Result<List<StazioneValanghe>> {

        val stazioniDB = StazioneValanghe.loadAll()

        return if (stazioniDB.isEmpty() || forceDownload) {
            return try {
                val stazioneDownloaded = StazioniValangheDownloader().downloadAnagrafica()

                StazioneValanghe.recreateTable()
                stazioneDownloaded.map { StazioneValanghe.fromStazioneXML(it) }
                    .forEach { it.save() }

                Result(StazioneValanghe.loadAll())
            } catch (e: Exception) {
                showError(e)
                Result(null, true, e)
            }
        } else {
            Result(stazioniDB)
        }
    }

    fun caricaDatiStazioneValanghe(suppressError: Boolean = false): Result<List<com.gmail.fattazzo.meteo.data.stazioni.valanghe.domain.dati.DatoStazione>> {

        return try {
            val datiStazione = StazioniValangheDownloader().downloadDatiStazioneNeve()

            Result(datiStazione)
        } catch (e: Exception) {
            if (!suppressError) {
                showError(e)
            }
            Result(null, true, e)
        }
    }

    private fun showError(e: Exception? = null) {

        e?.let {
            Crashlytics.logException(it)
            Log.e(TAG, "Errore", it)
        }

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, context.getString(R.string.service_error), Toast.LENGTH_LONG)
                .show()
        }
    }
}