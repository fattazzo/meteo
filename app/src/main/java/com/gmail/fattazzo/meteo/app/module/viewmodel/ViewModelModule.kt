/*
 * Project: meteo
 * File: ViewModelModule.kt
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

package com.gmail.fattazzo.meteo.app.module.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gmail.fattazzo.meteo.activity.bollettino.probabilistico.BollettinoProbabilisticoViewModel
import com.gmail.fattazzo.meteo.activity.main.MainViewModel
import com.gmail.fattazzo.meteo.activity.news.NewsViewModel
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.StazioniMeteoViewModel
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.StazioniValangheViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun provideMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BollettinoProbabilisticoViewModel::class)
    internal abstract fun provideBollettinoProbabilisticoViewModel(viewModel: BollettinoProbabilisticoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    internal abstract fun provideNewsViewModel(viewModel: NewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StazioniMeteoViewModel::class)
    internal abstract fun provideStazioniMeteoViewModel(viewModel: StazioniMeteoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StazioniValangheViewModel::class)
    internal abstract fun provideStazioniValangheViewModel(viewModel: StazioniValangheViewModel): ViewModel
}