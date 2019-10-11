/*
 * Project: meteo
 * File: StazioniMeteoActivity.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.meteo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.BaseActivity
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.anagrafica.AnagraficaStazioniMeteoActivity
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.rilevazioni.TipoDatoStazione
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.rilevazioni.dati.IDatoStazioneFragment
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.rilevazioni.dati.grafico.GraficoDatiStazioneFragment_
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.rilevazioni.dati.tabella.TabellaDatiStazioneFragment_
import com.gmail.fattazzo.meteo.app.MeteoApplication
import com.gmail.fattazzo.meteo.app.module.viewmodel.DaggerViewModelFactory
import com.gmail.fattazzo.meteo.data.db.entities.StazioneMeteo
import com.gmail.fattazzo.meteo.databinding.ActivityStazioniMeteoBinding
import com.gmail.fattazzo.meteo.utils.FragmentUtils
import kotlinx.android.synthetic.main.app_bar_main.view.*
import java.util.*
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 01/10/19
 */
class StazioniMeteoActivity : BaseActivity<ActivityStazioniMeteoBinding>() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    lateinit var viewModel: StazioniMeteoViewModel

    override fun getLayoutResID(): Int = R.layout.activity_stazioni_meteo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.appBarMain.toolbar)

        (application as MeteoApplication).appComponent.inject(this)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(StazioniMeteoViewModel::class.java)
        binding.model = viewModel

        initStazioneSelezionata()
        initTipoDato()

        viewModel.datiStazione.observe(this, Observer { updateDatiStazioneUI() })
        viewModel.graficoFormat.observe(this, Observer {
            if (viewModel.datiStazione.value != null) {
                updateDatiStazioneUI()
            }
        })

        viewModel.caricaAnagrafica(false)
    }

    private fun initStazioneSelezionata() {

        viewModel.stazioniMeteo.observe(this, Observer {
            val stazioneMeteo = StazioneMeteo(null, "")

            val stazioniSpinner = ArrayList<StazioneMeteo>()
            stazioniSpinner.add(stazioneMeteo)
            stazioniSpinner.addAll(viewModel.stazioniMeteo.value ?: listOf())

            binding.contentLayout.stazioneSpinner.adapter =
                ArrayAdapter<StazioneMeteo>(this, R.layout.spinner_item, stazioniSpinner)
        })

        viewModel.codiceStazioneSelezionata.observe(this, Observer {
            var stazioneSelIdx: Int = -1
            viewModel.stazioniMeteo.value.orEmpty().forEachIndexed { index, stazione ->
                if (stazione.codice == null) {
                    stazioneSelIdx = 0
                } else {
                    if (stazione.codice == it) {
                        stazioneSelIdx = index + 1
                    }
                }
            }

            if (stazioneSelIdx != -1) {
                binding.contentLayout.stazioneSpinner.setSelection(stazioneSelIdx, false)
            }
        })

        binding.contentLayout.stazioneSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val localita =
                        binding.contentLayout.stazioneSpinner.selectedItem as StazioneMeteo?
                    localita?.let {
                        viewModel.codiceStazioneSelezionata.postValue(it.codice)
                    }
                }
            }
    }

    private fun initTipoDato() {

        viewModel.tipoDato.observe(this, Observer {
            binding.contentLayout.tipoDatoSpinner.setSelection(it.ordinal, false)
        })

        binding.contentLayout.tipoDatoSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val tipoDatoPosition =
                        binding.contentLayout.tipoDatoSpinner.selectedItemPosition
                    if (tipoDatoPosition != AdapterView.INVALID_POSITION) {
                        viewModel.tipoDato.postValue(TipoDatoStazione.values()[tipoDatoPosition])
                    }
                }
            }
    }

    private fun updateDatiStazioneUI() {

        val fragment: IDatoStazioneFragment =
            if (viewModel.graficoFormat.value == true)
                GraficoDatiStazioneFragment_.builder().build() else TabellaDatiStazioneFragment_.builder().build()

        val dati = when (TipoDatoStazione.values()[viewModel.tipoDato.value?.ordinal ?: 0]) {
            TipoDatoStazione.TEMPERATURA -> viewModel.datiStazione.value?.temperature.orEmpty()
            TipoDatoStazione.PRECIPITAZIONE -> viewModel.datiStazione.value?.precipitazioni.orEmpty()
            TipoDatoStazione.UMIDITA -> viewModel.datiStazione.value?.umidita.orEmpty()
            TipoDatoStazione.RADIAZIONI -> viewModel.datiStazione.value?.radiazioni.orEmpty()
            TipoDatoStazione.VENTO -> viewModel.datiStazione.value?.venti.orEmpty()
            TipoDatoStazione.NEVE -> viewModel.datiStazione.value?.neve.orEmpty()
        }
        fragment.dati = dati

        FragmentUtils.replace(this, fragment as Fragment, R.id.datoContainer)
    }

    fun toggleFullscreenAction() {
        binding.contentLayout.headerLayout.visibility =
            if (binding.contentLayout.headerLayout.visibility == View.GONE) View.VISIBLE else View.GONE
        binding.contentLayout.tipoDatoLayout.visibility =
            if (binding.contentLayout.tipoDatoLayout.visibility == View.GONE) View.VISIBLE else View.GONE
        binding.contentLayout.tipoVisualizzazioneRadioGroup.visibility =
            if (binding.contentLayout.tipoVisualizzazioneRadioGroup.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    fun openAnagraficaStazioniActivity(view: View) {
        val intent = Intent(this, AnagraficaStazioniMeteoActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.stazioni_meteo_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toggleFullscreenAction -> {
                toggleFullscreenAction()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}