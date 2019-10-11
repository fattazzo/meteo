/*
 * Project: meteo
 * File: RadaActivityTest.kt
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

package com.gmail.fattazzo.meteo.activity.radar

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.rule.ActivityTestRule
import com.gmail.fattazzo.meteo.activity.BaseActivityTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Rule
import org.junit.Test

/**
 * @author fattazzo
 *         <p/>
 *         date: 11/10/19
 */
class RadaActivityTest : BaseActivityTest() {

    @Rule
    @JvmField
    var rule: ActivityTestRule<RadarActivity> =
        ActivityTestRule(RadarActivity::class.java, true, false)

    @Test
    fun loadData() {

        rule.launchActivity(null)
        val vieModel = rule.activity.viewModel

        assertThat(vieModel.radars.value!!.size, `is`(radarService.loadRadars().size))
        assertThat(vieModel.currentRadar.value!!.id, `is`(radarService.loadRadars()[0].id))
    }

    @Test
    fun verificaRadarSelezionabili() {

        rule.launchActivity(null)
        val vieModel = rule.activity.viewModel

        RadarPageObject.checkRadarRecyclerView(vieModel.radars.value!!)
    }
}