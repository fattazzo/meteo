/*
 * Project: meteo
 * File: StazioneMeteoView.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.meteo.anagrafica

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.db.StazioneMeteo
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 05/11/17
 */
@EViewGroup(R.layout.item_stazione_meteo)
open class StazioneMeteoView(context: Context?) : ConstraintLayout(context) {

    @ViewById
    lateinit var nomeTV: TextView

    @ViewById
    lateinit var codiceTV: TextView

    @ViewById
    lateinit var widgetTV: TextView

    fun bind(stazioneMeteo: StazioneMeteo, codiceStazioneWidget: String) {
        nomeTV.text = stazioneMeteo.nome
        codiceTV.text = stazioneMeteo.codice

        if (stazioneMeteo.enddate != null) {
            codiceTV.paintFlags = codiceTV.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            nomeTV.paintFlags = nomeTV.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        widgetTV.visibility = if (codiceStazioneWidget == stazioneMeteo.codice) View.VISIBLE else View.GONE
    }
}