/*
 * Project: meteo
 * File: GiornoView.kt
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

package com.gmail.fattazzo.meteo.activity.main

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.main.dettaglio.DettaglioGiornoActivity
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Giorni
import com.gmail.fattazzo.meteo.databinding.ItemGiornoBinding
import com.gmail.fattazzo.meteo.utils.glide.GlideHelper
import com.gmail.fattazzo.meteo.utils.icons.WeatherIconsFactory


/**
 * @author fattazzo
 *         <p/>
 *         date: 26/10/17
 */
class GiornoView : LinearLayout {

    val binding: ItemGiornoBinding by lazy {
        DataBindingUtil.inflate(
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.item_giorno,
            this as LinearLayout,
            false
        ) as ItemGiornoBinding
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    private fun initView() {
        binding.giorno = null
        binding.roundBorderTop = true
        binding.roundBorderBottom = false

        addView(binding.root)
    }

    fun bind(giorno: Giorni, roundBorderTop: Boolean, roundBorderBottom: Boolean) {
        binding.giorno = giorno
        binding.roundBorderTop = roundBorderTop
        binding.roundBorderBottom = roundBorderBottom

        if (!giorno.icona.isNullOrBlank()) {
            val iconsRetriever = WeatherIconsFactory.getIconsRetriever(context)
            val icona = iconsRetriever.getIcon(giorno.idIcona)

            if (icona == null) {
                Glide.with(context)
                    .load(giorno.icona)
                    .apply(GlideHelper.createNoCacheOptions(context!!, false, addTimeOut = false))
                    .into(binding.iconaImageView)
            } else {
                binding.iconaImageView.setImageResource(icona)
            }
        }
        binding.rootLayout.setOnClickListener { openDetailView() }
    }

    private fun openDetailView() {
        binding.giorno?.let {
            val intent = Intent(context, DettaglioGiornoActivity::class.java)
            intent.putExtra(DettaglioGiornoActivity.GIORNO_EXTRA, it)
            context.startActivity(intent)
        }
    }
}