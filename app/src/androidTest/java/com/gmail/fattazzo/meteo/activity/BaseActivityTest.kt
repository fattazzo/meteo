/*
 * Project: meteo
 * File: BaseActivityTest.kt
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

package com.gmail.fattazzo.meteo.activity

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.app.TestApplication
import com.gmail.fattazzo.meteo.app.component.TestAppComponent
import com.gmail.fattazzo.meteo.data.*
import com.gmail.fattazzo.meteo.preferences.PreferencesService
import com.gmail.fattazzo.meteo.utils.background
import com.gmail.fattazzo.meteo.utils.io
import com.gmail.fattazzo.meteo.utils.ui
import io.mockk.every
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * @author fattazzo
 *         <p/>
 *         date: 28/08/19
 */
abstract class BaseActivityTest {

    @Inject
    lateinit var preferencesService: PreferencesService
    @Inject
    lateinit var meteoService: MeteoService
    @Inject
    lateinit var newsService: NewsService
    @Inject
    lateinit var versioniService: VersioniService
    @Inject
    lateinit var webcamService: WebcamService
    @Inject
    lateinit var stazioniService: StazioniService
    @Inject
    lateinit var radarService: RadarService

    @Rule
    @JvmField
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    fun drain() {
        countingTaskExecutorRule.drainTasks(20, TimeUnit.SECONDS)
    }

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        (getTargetApplication().appComponent as TestAppComponent).inject(this)

        unconfinifyTestScope()

        initDefaultPreferencesService()
        initDefaultVersioniService()
    }

    @ExperimentalCoroutinesApi
    private fun unconfinifyTestScope() {
        ui = Dispatchers.Unconfined
        io = Dispatchers.Unconfined
        background = Dispatchers.Unconfined
    }

    fun getTargetApplication(): TestApplication =
        InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestApplication

    private fun initDefaultPreferencesService() {
        every { preferencesService.getNomeLocalitaPreferita() } returns "TRENTO"
        every { preferencesService.getWidgetWebcamIds() } returns mutableListOf()
        every { preferencesService.getCodiceStazioneMeteoWidget() } returns "TRENTO"
        every { preferencesService.getLastRunVersionName() } returns "nd"
        every { preferencesService.getStazMeteoGraphPointRadius() } returns 5
        every { preferencesService.getWidgetsBackground() } returns R.drawable.widget_shape_transparent_40_black
        every { preferencesService.getWidgetsTextColor() } returns -0x1
        every { preferencesService.showStazMeteoGraphPoint() } returns true
        every { preferencesService.showStazMeteoGraphShowSeriesBackground() } returns true
    }

    private fun initDefaultVersioniService() {
        every { versioniService.checkShowVersionChangelog() } returns false
        every { versioniService.caricaVersioni() } returns ""
    }
}