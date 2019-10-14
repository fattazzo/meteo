/*
 * Project: meteo
 * File: DettaglioGiornoPageObject.kt
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

package com.gmail.fattazzo.meteo.activity.main.dettaglio

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Fasce
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Giorni
import com.gmail.fattazzo.meteo.databinding.converters.FasciaConverter
import com.gmail.fattazzo.meteo.databinding.converters.GiornoConverter
import com.gmail.fattazzo.meteo.matcher.Matcher

/**
 * @author fattazzo
 *         <p/>
 *         date: 11/10/19
 */
object DettaglioGiornoPageObject {

    fun checkDataView(giorno: Giorni) {
        onView(withId(R.id.dataTV)).check(matches(withText(GiornoConverter.giornoData(giorno))))
    }

    fun pressBack() {
        Espresso.pressBack()
    }

    fun checkFasceView(giorno: Giorni?) {

        giorno?.fasce?.forEachIndexed { index, fascia ->

            // Scroll to position
            onView(withId(R.id.fasceHeaderRecyclerView))
                .perform(scrollToPosition<FasceHeaderAdapter.FasciaViewHolder>(index))

            // check views
            onView(
                Matcher.withRecyclerView(R.id.fasceHeaderRecyclerView)
                    .atPositionOnView(index, R.id.descriptionTV)
            )
                .check(matches(withText(FasciaConverter.fasciaPer(fascia))))
        }
    }

    fun clickFasciaHeader(index: Int) {
        onView(withId(R.id.fasceHeaderRecyclerView))
            .perform(scrollToPosition<FasceHeaderAdapter.FasciaViewHolder>(index))

        onView(withId(R.id.fasceHeaderRecyclerView)).perform(
            actionOnItemAtPosition<FasceHeaderAdapter.FasciaViewHolder>(index, click())
        )
    }

    fun checkDatiFasciaView(giorno: Giorni, fascia: Fasce) {
        checkDatiFasciaHeaderView(fascia)
        checkDatiFasciaTemperatureView(giorno, fascia)
        checkDatiFasciaPrecipitazioniView(giorno, fascia)
        checkDatiFasciaVentoView(fascia)
    }

    private fun checkDatiFasciaHeaderView(fascia: Fasce) {
        onView(withId(R.id.descrizioneIconaTV)).check(matches(withText(fascia.descIcona.orEmpty())))

        onView(withId(R.id.descrizioneTV)).check(matches(withText(FasciaConverter.fasciaOre(fascia))))
    }

    private fun checkDatiFasciaTemperatureView(giorno: Giorni?, fascia: Fasce) {

        onView(withId(R.id.massimaTV)).check(matches(withText(GiornoConverter.giornoTMax(giorno))))

        onView(withId(R.id.minimaTV)).check(matches(withText(GiornoConverter.giornoTMin(giorno))))

        onView(withId(R.id.zeroTermicoTV)).check(matches(withText(FasciaConverter.zeroTermico(fascia))))
    }

    private fun checkDatiFasciaPrecipitazioniView(giorno: Giorni?, fascia: Fasce) {

        onView(withId(R.id.probPrecTV)).check(matches(withText(fascia.descPrecProb.orEmpty())))

        onView(withId(R.id.intensitaPrecTV)).check(matches(withText(fascia.descPrecInten.orEmpty())))

        onView(withId(R.id.descTemporaliTV)).check(matches(withText(fascia.descTempProb.orEmpty())))
    }

    private fun checkDatiFasciaVentoView(fascia: Fasce) {

        val ventoValleDesc =
            "${fascia.descVentoIntValle.orEmpty()}\nDirezione ${fascia.descVentoDirValle.orEmpty()}"
        onView(withId(R.id.ventoValleDescTV)).check(matches(withText(ventoValleDesc)))

        val ventoQuotaDesc =
            "${fascia.descVentoIntQuota.orEmpty()}\nDirezione ${fascia.descVentoDirQuota.orEmpty()}"
        onView(withId(R.id.ventoQuotaDescTV)).check(matches(withText(ventoQuotaDesc)))
    }
}