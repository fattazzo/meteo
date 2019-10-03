/*
 * Project: meteo
 * File: StazioniValangheActivity.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.valanghe

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.AnagraficaStazioniValangheActivity_
import com.gmail.fattazzo.meteo.activity.BaseActivity
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.StazioneNeveAdapter
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.view.RilevazioniViewManager
import com.gmail.fattazzo.meteo.app.MeteoApplication
import com.gmail.fattazzo.meteo.app.module.viewmodel.DaggerViewModelFactory
import com.gmail.fattazzo.meteo.databinding.ActivityStazioniValangheBinding
import com.gmail.fattazzo.meteo.db.StazioneValanghe
import kotlinx.android.synthetic.main.app_bar_main.view.*
import java.util.*
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 02/10/19
 */
class StazioniValangheActivity : BaseActivity<ActivityStazioniValangheBinding>(),
    View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    lateinit var viewModel: StazioniValangheViewModel

    private var rilevazioniViewManager: RilevazioniViewManager? = null

    override fun getLayoutResID(): Int = R.layout.activity_stazioni_valanghe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MeteoApplication).appComponent.inject(this)

        initToolbar(binding.appBarMain.toolbar)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(StazioniValangheViewModel::class.java)
        binding.model = viewModel

        initStazioneSelezionata()

        rilevazioniViewManager = RilevazioniViewManager(binding.root)
        rilevazioniViewManager!!.registerOnCLickListener(this)

        viewModel.datiStazione.observe(this, Observer {
            rilevazioniViewManager!!.setStazioneCorrente(
                viewModel.codiceStazioneSelezionata.value,
                viewModel.datiStazione.value.orEmpty()
            )
        })

        viewModel.caricaAnagrafica(false)
    }

    override fun onClick(v: View) {
        rilevazioniViewManager!!.toggleContent(v.id)
    }

    fun openAnagraficaStazioniActivity(view: View) {
        AnagraficaStazioniValangheActivity_.intent(this).start()
    }

    private fun initStazioneSelezionata() {

        viewModel.stazioniValanghe.observe(this, Observer {
            val stazioneValanghe = StazioneValanghe()
            stazioneValanghe.codice = null
            stazioneValanghe.nome = ""

            val stazioniSpinner = ArrayList<StazioneValanghe>()
            stazioniSpinner.add(stazioneValanghe)
            stazioniSpinner.addAll(viewModel.stazioniValanghe.value.orEmpty())

            val dataAdapter = StazioneNeveAdapter(this, stazioniSpinner)
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.contentLayout.stazioniSpinner.adapter = dataAdapter
        })

        viewModel.codiceStazioneSelezionata.observe(this, Observer {
            var stazioneSelIdx: Int = -1
            viewModel.stazioniValanghe.value.orEmpty().forEachIndexed { index, stazione ->
                if (stazione.codice == null) {
                    stazioneSelIdx = 0
                } else {
                    if (stazione.codice == it) {
                        stazioneSelIdx = index + 1
                    }
                }
            }

            if (stazioneSelIdx != -1) {
                binding.contentLayout.stazioniSpinner.setSelection(stazioneSelIdx, false)
            }
        })

        binding.contentLayout.stazioniSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val stazione =
                        binding.contentLayout.stazioniSpinner.selectedItem as StazioneValanghe?
                    stazione?.let {
                        viewModel.codiceStazioneSelezionata.postValue(it.codice)
                    }
                }
            }
    }
}