/*
 * Project: meteo
 * File: AnagraficaFragment.kt
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

package com.gmail.fattazzo.meteo.fragment.stazioni.valanghe.anagrafica

import androidx.recyclerview.widget.RecyclerView
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.db.StazioneValanghe
import com.gmail.fattazzo.meteo.fragment.BaseFragment
import com.gmail.fattazzo.meteo.manager.MeteoManager
import org.androidannotations.annotations.*

/**
 * @author fattazzo
 *
 *
 * date: 17/lug/2014
 */
@EFragment(R.layout.fragment_anagrafica_stazioni_valanghe)
open class AnagraficaFragment : BaseFragment() {

    @Bean
    internal lateinit var meteoManager: MeteoManager

    @ViewById
    internal lateinit var anagraficaList: RecyclerView

    /**
     * Carica le stazioni meteo.
     */
    @UiThread
    open fun loadStazioniValanghe() {

        openIndeterminateDialog("Caricamento stazioni in corso...")

        // se non ho ancora stazioni meteo nel database le carico
        if (StazioneValanghe.count() == 0) {
            downloadStazioni()
        } else {
            loadTableData()
        }
    }

    @Background
    open fun downloadStazioni() {
        meteoManager.downloadAnagraficaStazioniValanghe()

        loadTableData()
    }

    /**
     * Carica tutte le stazioni meteo nell'adapter della tabella.
     */
    @UiThread
    open fun loadTableData() {

        if (activity != null) {
            val stazioni: List<StazioneValanghe> = StazioneValanghe.loadAll()

            val adapter = StazioneValangheListAdapter(stazioni, activity!!)

            anagraficaList.adapter = adapter
            anagraficaList.scheduleLayoutAnimation()

            closeIndeterminateDialog()
        }
    }

    @AfterViews
    fun initViews() {

        loadStazioniValanghe()
    }

    @Click
    fun refreshButtonClicked() {
        StazioneValanghe.deleteAll()
        loadStazioniValanghe()
    }

    override fun getTitleResId(): Int = R.string.nav_stazioni_neve
}
