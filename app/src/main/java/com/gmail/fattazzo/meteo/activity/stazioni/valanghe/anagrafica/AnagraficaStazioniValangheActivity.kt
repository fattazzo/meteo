/*
 * Project: meteo
 * File: AnagraficaStazioniValangheActivity.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.valanghe.anagrafica

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.BaseActivity
import com.gmail.fattazzo.meteo.app.MeteoApplication
import com.gmail.fattazzo.meteo.app.module.viewmodel.DaggerViewModelFactory
import com.gmail.fattazzo.meteo.databinding.ActivityStazioniValangheAnagraficaBinding
import kotlinx.android.synthetic.main.app_bar_main.view.*
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 08/10/19
 */
class AnagraficaStazioniValangheActivity :
    BaseActivity<ActivityStazioniValangheAnagraficaBinding>() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    lateinit var viewModel: AnagraficaStazioniValangheViewModel

    override fun getLayoutResID(): Int = R.layout.activity_stazioni_valanghe_anagrafica

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MeteoApplication).appComponent.inject(this)

        initToolbar(binding.appBarMain.toolbar)

        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(AnagraficaStazioniValangheViewModel::class.java)

        viewModel.stazioniValanghe.observe(this, Observer {

            val adapter =
                StazioneValangheListAdapter(it, this)

            binding.contentLayout.anagraficaList.adapter = adapter
            binding.contentLayout.anagraficaList.scheduleLayoutAnimation()
        })

        viewModel.caricaStazioni(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.anagrafica_stazioni_valanghe_menu, menu)
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