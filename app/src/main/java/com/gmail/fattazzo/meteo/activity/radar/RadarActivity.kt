/*
 * Project: meteo
 * File: RadarActivity.kt
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

package com.gmail.fattazzo.meteo.activity.radar

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.BaseActivity
import com.gmail.fattazzo.meteo.app.MeteoApplication
import com.gmail.fattazzo.meteo.app.module.viewmodel.DaggerViewModelFactory
import com.gmail.fattazzo.meteo.databinding.ActivityRadarBinding
import com.gmail.fattazzo.meteo.utils.ItemOffsetDecoration
import com.gmail.fattazzo.meteo.utils.glide.GlideHelper
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.fragment_radar.view.*
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 30/09/19
 */
class RadarActivity : BaseActivity<ActivityRadarBinding>(), RadarAdapter.OnClickListener {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    lateinit var viewModel: RadarViewModel

    override fun getLayoutResID(): Int = R.layout.activity_radar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MeteoApplication).appComponent.inject(this)

        initToolbar(binding.appBarMain.toolbar)

        viewModel = ViewModelProvider(this, viewModelFactory).get(RadarViewModel::class.java)

        binding.contentLayout.radarRecyclerView.addItemDecoration(
            ItemOffsetDecoration(
                0,
                0,
                10,
                10
            )
        )

        viewModel.radars.observe(this, Observer { radarModel ->
            val adapter = RadarAdapter(radarModel.orEmpty(), this, this)
            viewModel.currentRadar.value?.let { adapter.currentRadar = it }
            binding.contentLayout.radarRecyclerView.adapter = adapter
        })

        viewModel.currentRadar.observe(this, Observer { radarModel ->
            loadRadar(radarModel)

            binding.contentLayout.radarRecyclerView.adapter?.let {
                if (radarModel != null) {
                    (it as RadarAdapter).currentRadar = radarModel
                }
            }
        })

        viewModel.caricaRadars()
    }

    override fun onClick(radarModel: RadarModel) {
        viewModel.currentRadar.postValue(radarModel)
    }

    private fun loadRadar(radar: RadarModel?) {
        binding.contentLayout.radarView.setImageDrawable(null)

        radar?.let {
            Glide.with(this)
                .load(it.url)
                .apply(GlideHelper.createNoCacheOptions(this, true, addTimeOut = true))
                .into(binding.contentLayout.radarView)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.radar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refreshAction -> {
                loadRadar(viewModel.currentRadar.value)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}