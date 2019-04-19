/*
 * Project: meteo
 * File: StazioniMeteoXmlParser.kt
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

package com.gmail.fattazzo.meteo.parser.xml

import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.domain.xml.stazioni.meteo.StazioneMeteo
import com.gmail.fattazzo.meteo.domain.xml.stazioni.meteo.Stazioni
import com.gmail.fattazzo.meteo.domain.xml.stazioni.meteo.datistazione.DatiStazione
import org.androidannotations.annotations.EBean
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.convert.Registry
import org.simpleframework.xml.convert.RegistryStrategy
import org.simpleframework.xml.core.Persister
import java.io.InputStreamReader


/**
 * @author fattazzo
 *
 *
 * date: 13/lug/2015
 */
@EBean(scope = EBean.Scope.Singleton)
open class StazioniMeteoXmlParser : BaseXmlParser() {

    /**
     * Carica l'anagrafica delle stazioni meteo.
     *
     * @return stazioni caricate
     */
    fun caricaAnagrafica(): List<StazioneMeteo> {

        val inputStream = getInputStreamFromURL(Config.ANAGRAFICA_STAZIONI_XML)

        val strategy = AnnotationStrategy()
        val serializer = Persister(strategy)
        val stazioni = serializer.read(Stazioni::class.java, InputStreamReader(inputStream), false)

        return stazioni?.stazioniMeteo.orEmpty()
    }

    /**
     * Carica tutti i dati per la stazione con il codice passato come parametro.
     *
     * @param codiceStazione codice della stazione
     * @return dati caricati
     */
    fun caricaDatiStazione(codiceStazione: String): DatiStazione {

        val inputStream = getInputStreamFromURL(Config.DATI_STAZIONI_XML + codiceStazione)

        val registry = Registry()
        val strategy = RegistryStrategy(registry)
        val serializer = Persister(strategy)

        var datiStazione = serializer.read(DatiStazione::class.java, InputStreamReader(inputStream), false)

        if (datiStazione == null) {
            datiStazione = DatiStazione()
        }
        datiStazione.codiceStazione = codiceStazione

        datiStazione.neve = datiStazione.neve.orEmpty().filter { it.data != null }
        datiStazione.precipitazioni = datiStazione.precipitazioni.orEmpty().filter { it.data != null }
        datiStazione.radiazioni = datiStazione.radiazioni.orEmpty().filter { it.data != null }
        datiStazione.temperature = datiStazione.temperature.orEmpty().filter { it.data != null }
        datiStazione.umidita = datiStazione.umidita.orEmpty().filter { it.data != null }
        datiStazione.venti = datiStazione.venti.orEmpty().filter { it.data != null }

        return datiStazione
    }

}