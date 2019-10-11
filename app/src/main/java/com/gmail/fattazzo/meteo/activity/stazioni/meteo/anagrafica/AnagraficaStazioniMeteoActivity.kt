/*
 * Project: meteo
 * File: AnagraficaStazioniMeteoActivity.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.meteo.anagrafica

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.BaseActivity
import com.gmail.fattazzo.meteo.app.MeteoApplication
import com.gmail.fattazzo.meteo.app.module.viewmodel.DaggerViewModelFactory
import com.gmail.fattazzo.meteo.data.db.entities.StazioneMeteo
import com.gmail.fattazzo.meteo.databinding.ActivityStazioniMeteoAnagraficaBinding
import com.gmail.fattazzo.meteo.preferences.PreferencesService
import com.gmail.fattazzo.meteo.utils.dialog.DialogBuilder
import com.gmail.fattazzo.meteo.utils.dialog.DialogType
import kotlinx.android.synthetic.main.app_bar_main.view.*
import javax.inject.Inject

class AnagraficaStazioniMeteoActivity : BaseActivity<ActivityStazioniMeteoAnagraficaBinding>() {

    @Inject
    lateinit var preferencesService: PreferencesService

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    lateinit var viewModel: AnagraficaStazioniMeteoViewModel

    override fun getLayoutResID(): Int = R.layout.activity_stazioni_meteo_anagrafica

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MeteoApplication).appComponent.inject(this)

        initToolbar(binding.appBarMain.toolbar)

        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(AnagraficaStazioniMeteoViewModel::class.java)
        binding.model = viewModel

        viewModel.stazioniMeteo.observe(this, Observer {

            val adapter = StazioneMeteoListAdapter(
                this,
                it,
                preferencesService.getCodiceStazioneMeteoWidget()
            )
            binding.contentLayout.anagStazioniList.adapter = adapter
        })

        binding.contentLayout.anagStazioniList.setOnItemLongClickListener { parent, _, position, _ ->

            val stazioneMeteo = parent.adapter.getItem(position) as StazioneMeteo

            preferencesService.setCodiceStazioneMeteoWidget(stazioneMeteo.codice.orEmpty())

            Toast.makeText(
                this,
                this.getString(R.string.station_configured_for_widget).format(stazioneMeteo.codice.orEmpty()),
                Toast.LENGTH_SHORT
            ).show()

            viewModel.caricaStazioni(true)
            true
        }
        binding.contentLayout.anagStazioniList.setOnItemClickListener { parent, _, position, _ ->
            val stazioneMeteo = parent.adapter.getItem(position) as StazioneMeteo
            DialogStazioneMeteoBuilder.build(this, stazioneMeteo).show()
        }

        viewModel.caricaStazioni(false)
    }

    fun openInfo(view: View) {
        DialogBuilder(this, DialogType.BUTTONS)
            .apply {
                headerIcon = R.drawable.info_white
                titleResId = R.string.station_widget_info_title
                messageResId = R.string.station_widget_info_message
                positiveText = android.R.string.ok
            }
            .build()
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.anagrafica_stazioni_meteo_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refreshAction -> {
                viewModel.caricaStazioni(true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
