/*
 * Project: meteo
 * File: BollettinoGeneraleTest
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

package com.gmail.fattazzo.meteo.fragment.home


import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gmail.fattazzo.meteo.AppMenuConfig
import com.gmail.fattazzo.meteo.BaseTest
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.matcher.Matcher
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author fattazzo
 *
 *
 * date: 29/02/16
 */
@RunWith(AndroidJUnit4::class)
class BollettinoGeneraleTest : BaseTest() {

    @Test
    fun caricaBollettinoGenerale() {

        selectMenu(AppMenuConfig.HOME)
        waitView(R.id.evoluzioneTempoTV)

        onView(withId(R.id.evoluzioneTempoTV)).check(matches(isDisplayed()))
        onView(withId(R.id.containerLayout)).check(matches(Matcher.minChildrenCount(3)))

        onView(withId(R.id.refreshAction)).perform(click())
        waitView(R.id.evoluzioneTempoTV)

        onView(withId(R.id.evoluzioneTempoTV)).check(matches(isDisplayed()))
        onView(withId(R.id.containerLayout)).check(matches(Matcher.minChildrenCount(3)))

        // Cambio pagina e ritorno sulla home per verificare che il bollettino sia ancora caricato
        selectMenu(AppMenuConfig.BOLL_PROB)
        selectMenu(AppMenuConfig.HOME)
        waitView(R.id.evoluzioneTempoTV)

        onView(withId(R.id.evoluzioneTempoTV)).check(matches(isDisplayed()))
        onView(withId(R.id.containerLayout)).check(matches(Matcher.minChildrenCount(3)))
    }

    @Test
    fun bollettinoGeneraleDetail() {

        selectMenu(AppMenuConfig.HOME)
        waitView(R.id.evoluzioneTempoTV)

        onView(withId(R.id.containerLayout)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))

        // Fascia 1
        onView(withText("(ore 00-06)")).check(matches(isDisplayed()))

        // Fascia 2
        onView(withId(R.id.fasceHeaderRecyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))
        onView(withId(R.id.fasceHeaderRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        onView(withText("(ore 06-12)")).check(matches(isDisplayed()))

        // Fascia 3
        onView(withId(R.id.fasceHeaderRecyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2))
        onView(withId(R.id.fasceHeaderRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))
        onView(withText("(ore 12-18)")).check(matches(isDisplayed()))

        // Fascia 4
        onView(withId(R.id.fasceHeaderRecyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(3))
        onView(withId(R.id.fasceHeaderRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(3, click()))
        onView(withText("(ore 18-24)")).check(matches(isDisplayed()))

        pressBack()
    }
}
