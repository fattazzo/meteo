/*
 * Project: meteo
 * File: MainViewModelTest.kt
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

import com.gmail.fattazzo.meteo.AbstractViewModelTest
import com.gmail.fattazzo.meteo.data.MeteoService
import com.gmail.fattazzo.meteo.data.opendata.previsione.LocalitaMockData
import com.gmail.fattazzo.meteo.preferences.PreferencesService
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mock

/**
 * @author fattazzo
 *         <p/>
 *         date: 08/10/19
 */
class MainViewModelTest : AbstractViewModelTest<MainViewModel>() {

    @Mock
    lateinit var meteoService: MeteoService

    @Mock
    lateinit var preferencesService: PreferencesService

    override fun createViewModel(): MainViewModel = MainViewModel(meteoService, preferencesService)

    @Test
    override fun initialValues() {

        assertThat(viewModel.loadingLocalita.get(), `is`(false))
        assertThat(viewModel.loadingPrevisione.get(), `is`(false))

        assertThat(viewModel.localita.value, `is`(nullValue()))
        assertThat(viewModel.localitaSelezionata.value, `is`(nullValue()))

        assertThat(viewModel.previsioneLocalita.value, `is`(nullValue()))
    }

    @Test
    fun loadLocalita() {

        assertThat(viewModel.localita.value, `is`(nullValue()))

        LocalitaMockData.mockLoadAll_dataOk(meteoService)
        viewModel.caricaLocalita(false)

        assertThat(viewModel.localita.value, `is`(notNullValue()))
        assertThat(viewModel.localita.value?.size, `is`(539))
    }
}