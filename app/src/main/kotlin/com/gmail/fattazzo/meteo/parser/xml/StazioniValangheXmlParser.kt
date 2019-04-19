/*
 * Project: meteo
 * File: StazioniValangheXmlParser.kt
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
import com.gmail.fattazzo.meteo.domain.xml.stazioni.valanghe.StazioneValanghe
import com.gmail.fattazzo.meteo.domain.xml.stazioni.valanghe.Stazioni
import com.gmail.fattazzo.meteo.domain.xml.stazioni.valanghe.dati.Dati
import com.gmail.fattazzo.meteo.domain.xml.stazioni.valanghe.dati.DatoStazione
import com.gmail.fattazzo.meteo.parser.xml.converter.DateConverter
import org.androidannotations.annotations.EBean
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.convert.Registry
import org.simpleframework.xml.convert.RegistryStrategy
import org.simpleframework.xml.core.Persister
import java.io.InputStreamReader
import java.util.*

/**
 *
 * @author fattazzo
 *
 * date: 01/feb/2016
 */
@EBean(scope = EBean.Scope.Singleton)
open class StazioniValangheXmlParser : BaseXmlParser() {

    /**
     * Carica l'anagrafica delle stazioni valanghe.
     *
     * @return stazioni caricate
     */
    fun caricaAnagrafica(): List<StazioneValanghe> {

        val inputStream = getInputStreamFromURL(Config.ANAGRAFICA_STAZIONI_VALANGHE_XML)

        val strategy = AnnotationStrategy()
        val serializer = Persister(strategy)
        val stazioni = serializer.read(Stazioni::class.java, InputStreamReader(inputStream), false)

        return stazioni?.stazioniValanghe.orEmpty()
    }


    /**
     * Carica tutti i dati sul rilevamento neve delle stazioni.
     *
     * @return dati caricati
     */
    fun caricaDatiStazioneNeve(): List<DatoStazione> {

        val inputStream = getInputStreamFromURL(Config.DATI_STAZIONI_NEVE_XML)

        val registry = Registry()
        val strategy = RegistryStrategy(registry)
        val serializer = Persister(strategy)
        val dateConverter = DateConverter("dd/MM/yyyy HH:mm:ss")
        registry.bind(Date::class.java, dateConverter)
        val stazioni = serializer.read(Dati::class.java, InputStreamReader(inputStream), false)

        return stazioni?.datiStazione.orEmpty()
    }


}
