/*
 * Project: meteo
 * File: WebcamFragment.kt
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

package com.gmail.fattazzo.meteo.fragment.webcam

import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.domain.xml.webcam.Webcam
import com.gmail.fattazzo.meteo.fragment.BaseFragment
import com.gmail.fattazzo.meteo.fragment.home.HomeFragment_
import com.gmail.fattazzo.meteo.manager.MeteoManager
import com.gmail.fattazzo.meteo.utils.FragmentUtils
import org.androidannotations.annotations.*


/**
 * @author fattazzo
 *
 *
 * date: 09/nov/2017
 */
@EFragment(R.layout.fragment_webcam)
open class WebcamFragment : BaseFragment() {

    @ViewById
    internal lateinit var webcamRecyclerView: RecyclerView

    @ViewById
    internal lateinit var widgetWebcamButton: RadioButton

    @ViewById
    internal lateinit var webcamRefreshLayout: SwipeRefreshLayout

    @Bean
    internal lateinit var meteoManager: MeteoManager

    @AfterViews
    fun initViews() {
        refreshUI()

        webcamRefreshLayout.setOnRefreshListener {
            refreshUI()
            webcamRefreshLayout.isRefreshing = false
        }
    }

    private fun refreshUI() {
        val webcam = meteoManager.caricaWebcam()
        val webcams = filtraWebcam(webcam.webcams).sortedWith(compareBy({ it.ratio }, { it.localita }))
        val webcamsAdapter = WebcamsAdapter(context!!, webcams)

        webcamRecyclerView.setHasFixedSize(true)

        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = webcamsAdapter.getItemViewType(position)
        }

        webcamRecyclerView.layoutManager = layoutManager
        webcamRecyclerView.adapter = webcamsAdapter
    }

    private fun filtraWebcam(webcams: List<Webcam>): List<Webcam> {

        if (widgetWebcamButton.isChecked) {
            return webcams.filter { it.showInWidget }
        }

        return webcams
    }

    @Click(value = [(R.id.allWebcamButton), (R.id.widgetWebcamButton)])
    fun radioWebcamButton() {
        refreshUI()
    }

    override fun backPressed(): Boolean {
        FragmentUtils.replace(activity as AppCompatActivity, HomeFragment_.builder().build())
        return true
    }

    override fun getTitleResId(): Int = R.string.nav_webcam


}