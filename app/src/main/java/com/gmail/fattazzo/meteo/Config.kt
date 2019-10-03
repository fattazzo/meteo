/*
 * Project: meteo
 * File: Config.kt
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

package com.gmail.fattazzo.meteo

/**
 * @author fattazzo
 *         <p/>
 *         date: 27/10/17
 */
object Config {

    // Globali
    const val KEY_LAST_RUN_VERSION_NAME = "lastRunVersionName"
    const val KEY_LOCALITA = "localita"
    const val PRIVACY_POLICY_URL = "https://raw.githubusercontent.com/wiki/fattazzo/meteo/privacy/privacy_policy.pdf"
    const val PREVISIONE_AUDIO_URL = "https://content.meteotrentino.it/Bollettini/Audio/sintetico.mp3"

    // Bollettini
    const val LOCALITA_URL: String = "https://www.meteotrentino.it/protcivtn-meteo/api/front/localitaOpenData"
    const val PREVISIONE_LOCALITA_URL: String = "https://www.meteotrentino.it/protcivtn-meteo/api/front/previsioneOpenDataLocalita?localita="
    const val BOLLETTINO_PROBABILISTICO_URL: String = "https://www.meteotrentino.it/protcivtn-meteo/api/front/previsioneOpenDataProbabilistico"

    // Stazioni meteo
    const val ANAGRAFICA_STAZIONI_XML = "http://dati.meteotrentino.it/service.asmx/getListOfMeteoStations"
    const val DATI_STAZIONI_XML = "http://dati.meteotrentino.it/service.asmx/getLastDataOfMeteoStation?codice="

    // Stazioni valanghe
    const val ANAGRAFICA_STAZIONI_VALANGHE_XML = "http://dati.meteotrentino.it/service.asmx/listaCampiNeve"
    const val DATI_STAZIONI_NEVE_XML = "http://dati.meteotrentino.it/service.asmx/tuttiUltimiRilieviNeve"

    // Webcam
    const val FAVORITE_WEBCAM_IDS = "FAVORITE_WEBCAM_IDS"

    // Widgets
    const val WIDGETS_WEBCAM_IDS = "WIDGETS_WEBCAM_IDS"

    // git
    const val GIT_WIKI_LINK = "https://github.com/fattazzo/meteo/wiki"

    const val PROJECTS_INFO_URL = "https://gist.githubusercontent.com/fattazzo/d6aa41128c39b4882c0b6bd232984cfb/raw/projetcs.json"

    // radar
    const val RADAR_PRECIPITAZIONI = "https://content.meteotrentino.it/dati-meteo/radar/home/lastRadar4mobile.aspx"
    const val RADAR_INFRAROSSI = "http://api.sat24.com/animated/ALPS/infraPolair/1/Central%20European%20Standard%20Time/493234"
    const val RADAR_NEVE = "https://api.sat24.com/animated/EU/snow/1/Central%20European%20Standard%20Time"
    const val RADAR_EUROPA = "https://api.sat24.com/animated/EU/infraPolair/1/Central%20European%20Standard%20Time"

    // news e avvisi
    const val NEWS_URL = "http://www.protezionecivile.tn.it/news_comunicati_stampa"
    const val AVVISI_URL = "http://avvisi.protezionecivile.tn.it/elencoavvisi.aspx"
    const val INFOTRAFFICO_URL = "http://www.protezionecivile.tn.it/infotraffico"
}