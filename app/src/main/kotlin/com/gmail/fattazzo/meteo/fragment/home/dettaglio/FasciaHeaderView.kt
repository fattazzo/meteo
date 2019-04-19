/*
 * Project: meteo
 * File: FasciaHeaderView.kt
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

package com.gmail.fattazzo.meteo.fragment.home.dettaglio

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.domain.json.previsione.Fascia
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/04/19
 */
@EViewGroup(R.layout.item_fascia_header)
open class FasciaHeaderView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    @ViewById
    internal lateinit var descriptionTV : TextView

    @ViewById
    internal lateinit var iconaImageView: ImageView

    fun bind(fascia: Fascia) {
        descriptionTV.text = String.format("%s",fascia.descrizione.orEmpty().capitalize())

        if (!fascia.icona.isNullOrBlank()) {
            Glide.with(context).load(fascia.icona).into(iconaImageView)
        }
    }

    fun setSelectedView(selected: Boolean) {
        if (selected) {
            this.setBackgroundResource(R.drawable.border_line_round_selected)
        } else {
            this.setBackgroundResource(R.drawable.border_line_round)
        }
    }
}