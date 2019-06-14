/*
 * Project: meteo
 * File: MeteoManager.kt
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

package com.gmail.fattazzo.meteo.manager

import android.content.Context
import android.widget.Toast
import com.activeandroid.query.Select
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.db.Localita
import com.gmail.fattazzo.meteo.db.StazioneMeteo
import com.gmail.fattazzo.meteo.db.StazioneValanghe
import com.gmail.fattazzo.meteo.domain.json.bollettino.probabilistico.BollettinoProbabilistico
import com.gmail.fattazzo.meteo.domain.json.previsione.PrevisioneLocalita
import com.gmail.fattazzo.meteo.domain.xml.stazioni.meteo.datistazione.DatiStazione
import com.gmail.fattazzo.meteo.domain.xml.stazioni.valanghe.dati.DatoStazione
import com.gmail.fattazzo.meteo.domain.xml.webcam.Webcams
import com.gmail.fattazzo.meteo.parser.BollettinoGeneraleParser
import com.gmail.fattazzo.meteo.parser.BollettinoProbabilisticoParser
import com.gmail.fattazzo.meteo.parser.xml.StazioniMeteoXmlParser
import com.gmail.fattazzo.meteo.parser.xml.StazioniValangheXmlParser
import com.gmail.fattazzo.meteo.parser.xml.WebcamXmlParser
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import org.androidannotations.annotations.UiThread

/**
 * @author fattazzo
 *         <p/>
 *         date: 27/10/17
 */
@EBean(scope = EBean.Scope.Singleton)
open class MeteoManager {

    @RootContext
    internal lateinit var context: Context

    @Bean
    internal lateinit var bollettinoGeneraleParser: BollettinoGeneraleParser
    @Bean
    internal lateinit var bollettinoProbabilisticoParser: BollettinoProbabilisticoParser
    @Bean
    internal lateinit var stazioniMeteoXmlParser: StazioniMeteoXmlParser
    @Bean
    internal lateinit var stazioniValangheXmlParser: StazioniValangheXmlParser
    @Bean
    internal lateinit var webcamXmlParser: WebcamXmlParser

    @JvmOverloads
    fun caricaPrevisioneLocalita(localita: String, suppressException: Boolean = false): PrevisioneLocalita? {
        try {
            return bollettinoGeneraleParser.caricaPrevisione(localita)
        } catch (e: Exception) {
            if (!suppressException) {
                showErrorException(e)
            }
        }

        return null
    }

    fun caricaBollettinoProbabilistico(): BollettinoProbabilistico? {
        try {
            return bollettinoProbabilisticoParser.caricaBollettino()
        } catch (e: Exception) {
            showErrorException(e)
        }

        return null
    }

    fun downloadAnagraficaStazioni() {
        try {
            val stazioniXML = stazioniMeteoXmlParser.caricaAnagrafica()

            StazioneMeteo.recreateTable()
            stazioniXML.map { StazioneMeteo.fromStazioneXML(it) }.forEach { it.save() }
        } catch (e: Exception) {
            showErrorException(e)
        }
    }

    fun caricaDatiStazione(codiceStazione: String, suppressException: Boolean = false): DatiStazione {
        return try {
            stazioniMeteoXmlParser.caricaDatiStazione(codiceStazione)
        } catch (e: Exception) {
            if (!suppressException) {
                showErrorException(e)
            }
            DatiStazione()
        }
    }

    fun downloadAnagraficaStazioniValanghe() {
        try {
            val stazioniXML = stazioniValangheXmlParser.caricaAnagrafica()

            StazioneValanghe.recreateTable()
            stazioniXML.map { StazioneValanghe.fromStazioneXML(it) }.forEach { it.save() }
        } catch (e: Exception) {
            showErrorException(e)
        }
    }

    fun caricaDatiStazioneNeve(): List<DatoStazione> {
        return try {
            stazioniValangheXmlParser.caricaDatiStazioneNeve()
        } catch (e: Exception) {
            showErrorException(e)
            mutableListOf<DatoStazione>()
        }
    }

    fun caricaWebcam(): Webcams {
        return try {
            webcamXmlParser.caricaWebcam()
        } catch (e: Exception) {
            showErrorException(e)
            Webcams()
        }
    }

    open fun caricaLocalita(forceDownload: Boolean = false): MutableList<Localita> {

        if (Select().from(Localita::class.java).count() == 0 || forceDownload) {

            try {
                val localitaRemote = bollettinoGeneraleParser.caricaLocalita()
                Localita.recreateTable()
                for (localitaRem in localitaRemote) {
                    if (localitaRem.nome.orEmpty().toUpperCase() == localitaRem.nome.orEmpty()) {
                        val localita = Localita()
                        localita.nome = localitaRem.nome
                        localita.comune = localitaRem.comune
                        localita.quota = localitaRem.quota
                        localita.latitudine = localitaRem.latitudine
                        localita.longitudine = localitaRem.longitudine
                        localita.save()
                    }
                }
            } catch (e: Exception) {
                showErrorException(e)
            }
        }

        return Select().from(Localita::class.java).orderBy("nome").execute()
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    open fun showErrorException(e:Exception) {
        Toast.makeText(context, context.getString(R.string.service_error), Toast.LENGTH_LONG).show()
    }
}