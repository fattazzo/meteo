/*
 * Project: meteo
 * File: RilevazioniFragment.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni

import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.view.RilevazioniViewManager
import com.gmail.fattazzo.meteo.app.MeteoApplication
import com.gmail.fattazzo.meteo.data.StazioniService
import com.gmail.fattazzo.meteo.data.stazioni.valanghe.domain.dati.DatoStazione
import com.gmail.fattazzo.meteo.db.StazioneValanghe
import com.gmail.fattazzo.meteo.fragment.BaseFragment
import org.androidannotations.annotations.*
import javax.inject.Inject

/**
 * @author fattazzo
 *
 *
 * date: 02/feb/2016
 */
@EFragment(R.layout.fragment_dati_stazioni_neve)
open class RilevazioniFragment : BaseFragment(), OnClickListener, OnItemSelectedListener {

    @ViewById
    internal lateinit var stazioniSpinner: Spinner

    private var rilevazioniViewManager: RilevazioniViewManager? = null

    private var datiStazioni: List<DatoStazione>? = null

    @Inject
    lateinit var stazioniService: StazioniService

    /**
     * Carica i dati delle stazioni presenti.
     */
    @Background
    open fun loadData() {
        var stazioni = StazioneValanghe.loadAll().toMutableList()
        if (stazioni.isEmpty()) {
            stazioniService.caricaAnagraficaStazioniValanghe(false)
            stazioni = StazioneValanghe.loadAll().toMutableList()
        }
        updateSpinnerStazioni(stazioni)
    }

    @UiThread
    open fun updateSpinnerStazioni(stazioni: MutableList<StazioneValanghe>) {
        val stazioneValanghe = StazioneValanghe()
        stazioneValanghe.codice = null
        stazioneValanghe.nome = ""
        stazioni.add(0, stazioneValanghe)

        val dataAdapter = StazioneNeveAdapter(context!!, stazioni)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        stazioniSpinner.adapter = dataAdapter
        closeIndeterminateDialog()
    }

    override fun onClick(v: View) {
        rilevazioniViewManager!!.toggleContent(v.id)
    }

    @AfterViews
    internal fun onCreateView() {

        context?.let {
            (it.applicationContext as MeteoApplication).appComponent.inject(this)
        }

        rilevazioniViewManager = RilevazioniViewManager(view!!)
        rilevazioniViewManager!!.registerOnCLickListener(this)

        openIndeterminateDialog("Caricamento stazioni in corso...")
        loadData()
        stazioniSpinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        // non faccio niente finchè non viene scelta una stazione meteo
        if (stazioniSpinner.selectedItemPosition > 0) {
            openIndeterminateDialog("Caricamento dati in corso...")
            downloadDatiStazioni()
        }
    }

    @Background
    internal open fun downloadDatiStazioni() {
        if (datiStazioni == null) {
            datiStazioni = stazioniService.caricaDatiStazioneValanghe().value
        }

        updateRilevazioniView()
    }

    @UiThread
    internal open fun updateRilevazioniView() {
        closeIndeterminateDialog()
        val stazioneValanghe = stazioniSpinner.selectedItem as StazioneValanghe
        rilevazioniViewManager!!.setStazioneCorrente(
            stazioneValanghe.codice!!,
            datiStazioni.orEmpty()
        )
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}

    override fun getTitleResId(): Int = 0
}
