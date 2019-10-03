/*
 * Project: meteo
 * File: AppComponent.kt
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

package com.gmail.fattazzo.meteo.app.component

import com.gmail.fattazzo.meteo.activity.bollettino.probabilistico.BollettinoProbabilisticoActivity
import com.gmail.fattazzo.meteo.activity.changelog.ChangeLogActivity
import com.gmail.fattazzo.meteo.activity.main.MainActivity
import com.gmail.fattazzo.meteo.activity.news.NewsActivity
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.StazioniMeteoActivity
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.anagrafica.AnagraficaFragment
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.rilevazioni.dati.grafico.DataSetBuilder
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.StazioniValangheActivity
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.RilevazioniFragment
import com.gmail.fattazzo.meteo.activity.webcam.WebcamActivity
import com.gmail.fattazzo.meteo.activity.webcam.WebcamsAdapter
import com.gmail.fattazzo.meteo.app.module.AppModule
import com.gmail.fattazzo.meteo.app.module.ServicesModule
import com.gmail.fattazzo.meteo.app.module.viewmodel.ViewModelModule
import com.gmail.fattazzo.meteo.widget.providers.previsione.LoadPrevisioneLocalitaTask
import com.gmail.fattazzo.meteo.widget.providers.previsione.fascia.corrente.FasceListRemoteViewsFactory
import com.gmail.fattazzo.meteo.widget.providers.previsione.fascia.corrente.FasceProvider
import com.gmail.fattazzo.meteo.widget.providers.previsione.locale.BullettinPrevisioneLocalitaTodayProvider4x1
import com.gmail.fattazzo.meteo.widget.providers.previsione.locale.PrevisioneLocaleGridRemoteViewsFactory
import com.gmail.fattazzo.meteo.widget.providers.previsione.locale.horizontal.BullettinPrevisioneLocalitaHorizontalProvider4x1
import com.gmail.fattazzo.meteo.widget.providers.stazioni.meteo.LoadDatiStazioneMeteoTask
import com.gmail.fattazzo.meteo.widget.providers.stazioni.meteo.StazioneMeteoWidgetProvider
import com.gmail.fattazzo.meteo.widget.providers.webcam.WebcamWidgetProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ServicesModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: BollettinoProbabilisticoActivity)
    fun inject(activity: NewsActivity)
    fun inject(activity: ChangeLogActivity)
    fun inject(activity: WebcamActivity)
    fun inject(activity: StazioniMeteoActivity)
    fun inject(activity: StazioniValangheActivity)

    fun inject(fragment: AnagraficaFragment)
    fun inject(fragment: com.gmail.fattazzo.meteo.activity.stazioni.valanghe.anagrafica.AnagraficaFragment)
    fun inject(fragment: RilevazioniFragment)

    fun inject(task: LoadPrevisioneLocalitaTask)
    fun inject(task: LoadDatiStazioneMeteoTask)

    fun inject(builder: DataSetBuilder)

    fun inject(factory: FasceListRemoteViewsFactory)
    fun inject(factory: PrevisioneLocaleGridRemoteViewsFactory)

    fun inject(provider: FasceProvider)
    fun inject(provider: BullettinPrevisioneLocalitaTodayProvider4x1)
    fun inject(provider: BullettinPrevisioneLocalitaHorizontalProvider4x1)
    fun inject(provider: StazioneMeteoWidgetProvider)
    fun inject(provider: WebcamWidgetProvider)

    fun inject(adapter: WebcamsAdapter)
}