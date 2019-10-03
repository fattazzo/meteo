/*
 * Project: meteo
 * File: StazioneNeveAdapter.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.gmail.fattazzo.meteo.R

import com.gmail.fattazzo.meteo.db.StazioneValanghe

/**
 * @author fattazzo
 *
 *
 * date: 02/feb/2016
 */
class StazioneNeveAdapter
/**
 * Costruttore.
 *
 * @param context  context
 * @param itemList lista delle stazioni neve
 */
(context: Context, val itemList: List<StazioneValanghe>) : ArrayAdapter<StazioneValanghe>(context, R.layout.spinner_item) {

    override fun getCount(): Int = this.itemList.size

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): TextView {

        var v = convertView as TextView?

        if (v == null) {
            val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = vi.inflate(R.layout.spinner_item, null as ViewGroup?) as TextView
        }

        v.text = itemList[position].nome
        return v
    }

    override fun getItem(position: Int): StazioneValanghe? = itemList[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): TextView {

        var v = convertView as TextView?

        if (v == null) {
            val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = vi.inflate(R.layout.spinner_item, null as ViewGroup?) as TextView
        }

        v.text = itemList[position].nome
        return v
    }

}
