/*
 * Project: meteo
 * File: GiornoAdapter.kt
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

package com.gmail.fattazzo.meteo.databinding.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Giorni
import com.gmail.fattazzo.meteo.utils.glide.GlideHelper
import com.gmail.fattazzo.meteo.utils.icons.WeatherIconsFactory

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/10/19
 */
object GiornoAdapter {

    @BindingAdapter("giornoThemeIcon")
    @JvmStatic
    fun giornoThemeIcon(view: ImageView, giorno: Giorni?) {
        val context = view.context
        if (!giorno?.icona.isNullOrBlank()) {
            val iconsRetriever = WeatherIconsFactory.getIconsRetriever(context)
            val icona = iconsRetriever.getIcon(giorno!!.idIcona)

            if (icona == null) {
                Glide.with(context)
                    .load(giorno.icona)
                    .apply(GlideHelper.createNoCacheOptions(context, false, addTimeOut = false))
                    .into(view)
            } else {
                view.setImageResource(icona)
            }
        }
    }
}