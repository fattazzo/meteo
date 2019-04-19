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

package com.gmail.fattazzo.meteo.fragment.home

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.DettaglioGiornoActivity_
import com.gmail.fattazzo.meteo.domain.json.previsione.Giorno
import com.squareup.picasso.Picasso
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 26/10/17
 */
@EViewGroup(R.layout.item_giorno)
open class GiornoView : LinearLayout {

    @ViewById
    internal lateinit var dataTV: TextView

    @ViewById
    internal lateinit var descrizioneTV: TextView

    @ViewById
    internal lateinit var temperatureMinTV: TextView

    @ViewById
    internal lateinit var temperatureMaxTV: TextView

    @ViewById
    internal lateinit var iconaImageView: ImageView

    private var giorno: Giorno? = null

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    fun bind(giorno: Giorno, roundBorderTop: Boolean, roundBorderBottom: Boolean) {
        this.setBackgroundResource(R.drawable.border_line_left_right)
        if (roundBorderTop) {
            this.setBackgroundResource(R.drawable.border_line_round_top)
        }
        if (roundBorderBottom) {
            this.setBackgroundResource(R.drawable.border_line_round_bottom)
        }

        this.giorno = giorno

        val dateFormat = SimpleDateFormat("EEEE dd/MM/yyyy", Locale.ITALIAN)

        dataTV.text = dateFormat.format(giorno.data).capitalize()
        descrizioneTV.text = giorno.testo.orEmpty().capitalize()

        temperatureMinTV.text = String.format("%d°C", giorno.temperaturaMin)
        temperatureMaxTV.text = String.format("%d°C", giorno.temperaturaMax)

        if (!giorno.icona.isNullOrBlank()) {
            Picasso.get().load(giorno.icona).into(iconaImageView)
        }


    }

    @Click
    fun rootLayoutClicked() {
        giorno?.let {
            DettaglioGiornoActivity_.intent(context).giorno(giorno).start()
        }
    }
}