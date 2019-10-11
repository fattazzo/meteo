/*
 * Project: meteo
 * File: MainActivityTest.kt
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

package com.gmail.fattazzo.meteo.activity.main

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.rule.ActivityTestRule
import com.gmail.fattazzo.meteo.activity.BaseActivityTest
import com.gmail.fattazzo.meteo.activity.main.dettaglio.DettaglioGiornoPageObject
import com.gmail.fattazzo.meteo.config.PrevisioneConfig
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.PrevisioneLocalita
import com.gmail.fattazzo.meteo.data.opendata.previsione.LocalitaMockData
import com.gmail.fattazzo.meteo.data.opendata.previsione.PrevisioneLocalitaMockData
import com.gmail.fattazzo.meteo.databinding.converters.PrevisioneConverter
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.greaterThan
import org.junit.Rule
import org.junit.Test

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/10/19
 */
class MainActivityTest : BaseActivityTest() {

    @Rule
    @JvmField
    var rule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java, true, false)

    @Test
    fun caricaPrevisioneOk() {

        LocalitaMockData.mockLoadAll_dataOk(meteoService)
        PrevisioneLocalitaMockData.mockDataOkTrento10102019(meteoService)

        val previsioneExpected =
            PrevisioneLocalitaMockData.fromJson<PrevisioneLocalita>(PrevisioneConfig.DATA_OK_TRENTO_10_10_2019)

        rule.launchActivity(null)
        val viewModel = rule.activity.viewModel

        MainPageObject.checkActivityMainTitle()

        assertThat(viewModel.localitaSelezionata.value!!, `is`("TRENTO"))
        assertThat(viewModel.previsioneLocalita.value, `is`(notNullValue()))
        assertThat(viewModel.previsioneLocalita.value!!.idPrevisione, `is`(1477))

        MainPageObject.checkLocalitaSelected("TRENTO")
        MainPageObject.checkPrevisioneList(previsioneExpected)
    }

    @Test
    fun caricaPrevisioneError() {
        LocalitaMockData.mockLoadAll_dataOk(meteoService)
        PrevisioneLocalitaMockData.mockDataError(meteoService)

        rule.launchActivity(null)
        val viewModel = rule.activity.viewModel

        assertThat(viewModel.localitaSelezionata.value!!, `is`("TRENTO"))
        assertThat(viewModel.previsioneLocalita.value, `is`(nullValue()))

        MainPageObject.checkLocalitaSelected("TRENTO")
        MainPageObject.checkPrevisioneEmptyList()
    }

    @Test
    fun cambiaLocalita() {

        LocalitaMockData.mockLoadAll_dataOk(meteoService)
        PrevisioneLocalitaMockData.mockDataOkTrento10102019(meteoService)

        var previsioneExpected =
            PrevisioneLocalitaMockData.fromJson<PrevisioneLocalita>(PrevisioneConfig.DATA_OK_TRENTO_10_10_2019)

        rule.launchActivity(null)

        MainPageObject.checkLocalitaSelected("TRENTO")
        MainPageObject.checkPrevisioneList(previsioneExpected)

        // Cambio località
        PrevisioneLocalitaMockData.mockDataOkRovereto11102019(meteoService)
        previsioneExpected =
            PrevisioneLocalitaMockData.fromJson(PrevisioneConfig.DATA_OK_ROVERETO_11_10_2019)

        MainPageObject.selectLocalita("ROVERETO")

        MainPageObject.checkLocalitaSelected("ROVERETO")
        MainPageObject.checkPrevisioneList(previsioneExpected)
    }

    @Test
    fun apriDettaglioPrevisione() {

        LocalitaMockData.mockLoadAll_dataOk(meteoService)
        PrevisioneLocalitaMockData.mockDataOkTrento10102019(meteoService)

        val previsioneExpected =
            PrevisioneLocalitaMockData.fromJson<PrevisioneLocalita>(PrevisioneConfig.DATA_OK_TRENTO_10_10_2019)

        rule.launchActivity(null)

        MainPageObject.checkPrevisioneEvoluzioneText(
            PrevisioneConverter.evoluzione(
                previsioneExpected,
                false
            )
        )
        MainPageObject.clickPrevisioneView()
        MainPageObject.checkPrevisioneEvoluzioneText(
            PrevisioneConverter.evoluzione(
                previsioneExpected,
                true
            )
        )
        MainPageObject.clickPrevisioneView()
        MainPageObject.checkPrevisioneEvoluzioneText(
            PrevisioneConverter.evoluzione(
                previsioneExpected,
                false
            )
        )
    }

    @Test
    fun apriDettaglioGiorno() {

        LocalitaMockData.mockLoadAll_dataOk(meteoService)
        PrevisioneLocalitaMockData.mockDataOkTrento10102019(meteoService)

        val previsioneExpected =
            PrevisioneLocalitaMockData.fromJson<PrevisioneLocalita>(PrevisioneConfig.DATA_OK_TRENTO_10_10_2019)

        rule.launchActivity(null)

        previsioneExpected.previsione!![0].giorni!!.forEachIndexed { index, giorni ->
            MainPageObject.clickGiornoView(index)
            DettaglioGiornoPageObject.checkDataView(giorni)
            DettaglioGiornoPageObject.pressBack()
        }
    }

    @Test
    fun ricaricaPrevisione() {

        LocalitaMockData.mockLoadAll_dataOk(meteoService)
        PrevisioneLocalitaMockData.mockDataOkTrento10102019(meteoService)

        val previsioneExpected =
            PrevisioneLocalitaMockData.fromJson<PrevisioneLocalita>(PrevisioneConfig.DATA_OK_TRENTO_10_10_2019)

        rule.launchActivity(null)

        MainPageObject.checkPrevisioneList(previsioneExpected)

        // Refresh 1
        PrevisioneLocalitaMockData.mockDataError(meteoService)

        MainPageObject.clickRefreshMenu()
        MainPageObject.checkPrevisioneEmptyList()

        // Refresh 2
        PrevisioneLocalitaMockData.mockDataOkTrento10102019(meteoService)

        MainPageObject.clickRefreshMenu()
        MainPageObject.checkPrevisioneList(previsioneExpected)
    }

    @Test
    fun ascoltaPrevisione() {

        LocalitaMockData.mockLoadAll_dataOk(meteoService)
        PrevisioneLocalitaMockData.mockDataOkTrento10102019(meteoService)

        rule.launchActivity(null)

        assertThat(MainActivity.mediaPlayer.isPlaying, `is`(false))

        // Start
        MainPageObject.clickPlayStopAudioPrevisione()
        assertThat(MainActivity.mediaPlayer.isPlaying, `is`(true))
        assertThat(MainActivity.mediaPlayer.trackInfo.size, `is`(greaterThan(0)))

        // Stop
        MainPageObject.clickPlayStopAudioPrevisione()
        assertThat(MainActivity.mediaPlayer.isPlaying, `is`(false))
    }
}