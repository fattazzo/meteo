/*
 * Project: meteo
 * File: MainPageObject.kt
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

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.data.db.entities.Localita
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Giorni
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.PrevisioneLocalita
import com.gmail.fattazzo.meteo.databinding.converters.GiornoConverter
import com.gmail.fattazzo.meteo.databinding.converters.PrevisioneConverter
import com.gmail.fattazzo.meteo.matcher.Matcher.withRecyclerView
import com.gmail.fattazzo.meteo.matcher.Matcher.withToStringValue
import com.gmail.fattazzo.meteo.matcher.RecyclerViewItemCountAssertion.Companion.withItemCount
import org.hamcrest.CoreMatchers.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 10/10/19
 */
object MainPageObject {

    fun checkActivityMainTitle() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withText(R.string.title_activity_main)).check(matches(withParent(withId(R.id.toolbar))))
    }

    fun checkLocalitaSelected(text: String) {
        onView(withId(R.id.localitaSpinner)).check(matches(withSpinnerText(text)))
    }

    fun checkPrevisioneList(previsione: PrevisioneLocalita) {
        onView(withId(R.id.containerLayout)).check(withItemCount(previsione.previsione!![0].giorni!!.size + 1))

        checkPrevisioneView(previsione)

        previsione.previsione!![0].giorni!!.forEachIndexed { index, giorno ->
            checkGiornoView(index + 1, giorno)
        }
    }

    fun checkPrevisioneEmptyList() {
        onView(withId(R.id.containerLayout)).check(withItemCount(0))
    }

    fun selectLocalita(text: String) {
        onView(withId(R.id.localitaSpinner)).perform(click())
        onData(
            allOf(
                `is`(instanceOf(Localita::class.java)),
                withToStringValue(text)
            )
        ).perform(click())
    }

    fun checkPrevisioneEvoluzioneText(text: String) {
        onView(
            withRecyclerView(R.id.containerLayout)
                .atPositionOnView(0, R.id.evoluzioneTempoTV)
        ).check(matches(withText(text)))
    }

    fun clickPrevisioneView() {
        onView(withId(R.id.containerLayout)).perform(
            actionOnItemAtPosition<PrevisioneAdapter.PrevisioneViewHolder>(
                0,
                click()
            )
        )
    }

    fun clickGiornoView(index: Int) {
        onView(withId(R.id.containerLayout))
            .perform(scrollToPosition<PrevisioneAdapter.PrevisioneViewHolder>(index + 1))
        onView(withId(R.id.containerLayout)).perform(
            actionOnItemAtPosition<PrevisioneAdapter.PrevisioneViewHolder>(
                index + 1,
                click()
            )
        )
    }

    fun clickRefreshMenu() {
        onView(withId(R.id.refreshAction)).perform(click())
    }

    fun clickPlayStopAudioPrevisione() {
        onView(withId(R.id.previsioneAudioAction)).perform(click());
    }

    private fun checkPrevisioneView(previsione: PrevisioneLocalita) {
        // Scroll to position
        onView(withId(R.id.containerLayout))
            .perform(scrollToPosition<PrevisioneAdapter.PrevisioneViewHolder>(0))
        // check views
        onView(
            withRecyclerView(R.id.containerLayout)
                .atPositionOnView(0, R.id.dataTV)
        )
            .check(matches(withText(PrevisioneConverter.dataToString(previsione))))

        onView(
            withRecyclerView(R.id.containerLayout)
                .atPositionOnView(0, R.id.evoluzioneTempoTV)
        )
            .check(matches(withText(PrevisioneConverter.evoluzione(previsione, false))))

        onView(
            withRecyclerView(R.id.containerLayout)
                .atPositionOnView(0, R.id.titolareTV)
        )
            .check(matches(withText(previsione.nomeTitolare)))

        onView(
            withRecyclerView(R.id.containerLayout)
                .atPositionOnView(0, R.id.editoreTV)
        )
            .check(matches(withText(previsione.nomeEditore)))
    }

    private fun checkGiornoView(index: Int, giorno: Giorni) {
        // Scroll to position
        onView(withId(R.id.containerLayout))
            .perform(scrollToPosition<PrevisioneAdapter.PrevisioneViewHolder>(index))
        // check views
        onView(
            withRecyclerView(R.id.containerLayout)
                .atPositionOnView(index, R.id.dataTV)
        )
            .check(matches(withText(GiornoConverter.giornoData(giorno))))

        onView(
            withRecyclerView(R.id.containerLayout)
                .atPositionOnView(index, R.id.temperatureMinTV)
        )
            .check(matches(withText(GiornoConverter.giornoTMin(giorno))))

        onView(
            withRecyclerView(R.id.containerLayout)
                .atPositionOnView(index, R.id.temperatureMaxTV)
        )
            .check(matches(withText(GiornoConverter.giornoTMax(giorno))))

        onView(
            withRecyclerView(R.id.containerLayout)
                .atPositionOnView(index, R.id.descrizioneTV)
        )
            .check(matches(withText(GiornoConverter.giornoTesto(giorno))))
    }
}