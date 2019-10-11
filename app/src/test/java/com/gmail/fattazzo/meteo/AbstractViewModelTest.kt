/*
 * Project: meteo
 * File: AbstractViewModelTest.kt
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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import com.gmail.fattazzo.meteo.utils.background
import com.gmail.fattazzo.meteo.utils.io
import com.gmail.fattazzo.meteo.utils.ui
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations
import java.util.*

abstract class AbstractViewModelTest<T : ViewModel> {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: T

    abstract fun createViewModel(): T

    @ExperimentalCoroutinesApi
    private fun unconfinifyTestScope() {
        ui = Dispatchers.Unconfined
        io = Dispatchers.Unconfined
        background = Dispatchers.Unconfined
    }

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Locale.setDefault(Locale.ITALY)
        MockitoAnnotations.initMocks(this)
        viewModel = createViewModel()
        unconfinifyTestScope()
    }

    abstract fun initialValues()
}