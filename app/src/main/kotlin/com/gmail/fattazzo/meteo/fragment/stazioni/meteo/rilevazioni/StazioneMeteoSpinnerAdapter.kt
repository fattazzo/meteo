/*
 * Project: meteo
 * File: StazioneMeteoSpinnerAdapter.kt
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

package com.gmail.fattazzo.meteo.fragment.stazioni.meteo.rilevazioni

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.db.StazioneMeteo

/**
 *
 * @author fattazzo
 *
 * date: 05/nov/2017
 */
class StazioneMeteoSpinnerAdapter
(context: Context, val objects: List<StazioneMeteo>) : ArrayAdapter<StazioneMeteo>(context, R.layout.spinner_item) {

    override fun getCount(): Int = this.objects.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View = buildView(convertView, position)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View = buildView(convertView, position)

    private fun buildView(convertView: View?, position: Int): View {
        val v = if (convertView == null) {
            val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            vi.inflate(R.layout.spinner_item, null as ViewGroup?) as TextView
        } else {
            convertView as TextView
        }

        v.text = objects[position].nome
        //v.maxLines = 1
        //v.ellipsize = TextUtils.TruncateAt.END

        return v
    }
}