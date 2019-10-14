/*
 * Project: meteo
 * File: WebcamActivity.kt
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

package com.gmail.fattazzo.meteo.activity.webcam

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.BaseActivity
import com.gmail.fattazzo.meteo.app.MeteoApplication
import com.gmail.fattazzo.meteo.app.services.WebcamService
import com.gmail.fattazzo.meteo.data.webcam.Webcam
import com.gmail.fattazzo.meteo.databinding.ActivityWebcamBinding
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.fragment_webcam.view.*
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 01/10/19
 */
class WebcamActivity : BaseActivity<ActivityWebcamBinding>() {

    @Inject
    lateinit var webcamService: WebcamService

    override fun getLayoutResID(): Int = R.layout.activity_webcam

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MeteoApplication).appComponent.inject(this)

        initToolbar(binding.appBarMain.toolbar)

        binding.contentLayout.webcamRefreshLayout.setOnRefreshListener {
            refreshUI()
            binding.contentLayout.webcamRefreshLayout.isRefreshing = false
        }

        refreshUI()
    }

    private fun refreshUI() {
        val webcam = webcamService.caricaWebcam()
        val webcams =
            filtraWebcam(webcam.webcams).sortedWith(compareBy({ it.ratio }, { it.localita }))
        val webcamsAdapter = WebcamsAdapter(this, webcams)

        binding.contentLayout.webcamRecyclerView.setHasFixedSize(true)

        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = webcamsAdapter.getItemViewType(position)
        }

        binding.contentLayout.webcamRecyclerView.layoutManager = layoutManager
        binding.contentLayout.webcamRecyclerView.adapter = webcamsAdapter
    }

    private fun filtraWebcam(webcams: List<Webcam>): List<Webcam> {
        if (binding.contentLayout.widgetWebcamButton.isChecked) {
            return webcams.filter { it.showInWidget }
        }
        return webcams
    }
}