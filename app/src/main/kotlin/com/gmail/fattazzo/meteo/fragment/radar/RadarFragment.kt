/*
 * Project: meteo
 * File: RadarFragment.kt
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

package com.gmail.fattazzo.meteo.fragment.radar

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.fragment.BaseFragment
import com.gmail.fattazzo.meteo.fragment.home.HomeFragment_
import com.gmail.fattazzo.meteo.utils.FragmentUtils
import com.gmail.fattazzo.meteo.utils.ItemOffsetDecoration
import com.gmail.fattazzo.meteo.utils.glide.GlideHelper
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *
 *
 * date: 13/lug/2015
 */
@EFragment(R.layout.fragment_radar)
open class RadarFragment : BaseFragment(), RadarAdapter.OnClickListener {

    @ViewById
    internal lateinit var radarView: PhotoView

    @ViewById
    internal lateinit var radarRecyclerView: RecyclerView

    lateinit var currentRadar: RadarModel

    override fun getTitleResId(): Int = R.string.nav_radar

    @AfterViews
    fun afterView() {

        val radarData = listOf(RadarModel("Precipitazioni", Config.RADAR_PRECIPITAZIONI, R.drawable.drop),
                RadarModel("Infrarossi", Config.RADAR_INFRAROSSI, R.drawable.infrared),
                RadarModel("Neve", Config.RADAR_NEVE, R.drawable.snowflake),
                RadarModel("Europa", Config.RADAR_EUROPA, R.drawable.europe))

        radarRecyclerView.addItemDecoration(ItemOffsetDecoration(0, 0, 10, 10))
        radarRecyclerView.adapter = RadarAdapter(radarData, context!!, this)

        currentRadar = radarData[0]
        loadCurrentRadar()
    }

    override fun backPressed(): Boolean {
        FragmentUtils.replace(activity as AppCompatActivity, HomeFragment_.builder().build())
        return true
    }

    override fun onClick(radarModel: RadarModel) {
        currentRadar = radarModel
        loadCurrentRadar()
    }

    @Click
    fun refreshRadarFabClicked() {
        loadCurrentRadar()
    }

    private fun loadCurrentRadar() {
        val circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(this)
                .load(currentRadar.url)
                .apply(GlideHelper.createNoCacheOptions(context!!, true, addTimeOut = true))
                .into(radarView)
    }
}
