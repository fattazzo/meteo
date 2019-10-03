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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.BaseActivity
import com.gmail.fattazzo.meteo.databinding.ActivityRadarBinding
import com.gmail.fattazzo.meteo.utils.ItemOffsetDecoration
import com.gmail.fattazzo.meteo.utils.glide.GlideHelper
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.fragment_radar.view.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 30/09/19
 */
class RadarActivity : BaseActivity<ActivityRadarBinding>(), RadarAdapter.OnClickListener {

    lateinit var currentRadar: RadarModel

    override fun getLayoutResID(): Int = R.layout.activity_radar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.appBarMain.toolbar)

        val radarData = listOf(
            RadarModel(
                "Precipitazioni",
                Config.RADAR_PRECIPITAZIONI,
                R.drawable.drop
            ),
            RadarModel(
                "Infrarossi",
                Config.RADAR_INFRAROSSI,
                R.drawable.infrared
            ),
            RadarModel(
                "Neve",
                Config.RADAR_NEVE,
                R.drawable.snowflake
            ),
            RadarModel(
                "Europa",
                Config.RADAR_EUROPA,
                R.drawable.europe
            )
        )

        binding.contentLayout.radarRecyclerView.addItemDecoration(
            ItemOffsetDecoration(
                0,
                0,
                10,
                10
            )
        )
        binding.contentLayout.radarRecyclerView.adapter =
            RadarAdapter(radarData, this, this)

        currentRadar = radarData[0]
        loadCurrentRadar()
    }

    override fun onClick(radarModel: RadarModel) {
        currentRadar = radarModel
        loadCurrentRadar()
    }

    private fun loadCurrentRadar() {
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(this)
            .load(currentRadar.url)
            .apply(GlideHelper.createNoCacheOptions(this, true, addTimeOut = true))
            .into(binding.contentLayout.radarView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.radar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refreshAction -> {
                loadCurrentRadar()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}