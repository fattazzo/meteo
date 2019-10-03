/*
 * Project: meteo
 * File: RilevazioneSezioneListAdapter.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.gmail.fattazzo.meteo.R

/**
 *
 * @author fattazzo
 *
 * date: 03/feb/2016
 */
class RilevazioneSezioneListAdapter(context: Context, objects: List<RilevazioneSezione>) : ArrayAdapter<RilevazioneSezione>(context, R.layout.item_neve_rilevazione_stazione, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val rilevazioneSezioneView: RilevazioneSezioneView = if (convertView == null) {
            RilevazioneSezioneView_.build(context)
        } else {
            convertView as RilevazioneSezioneView
        }

        rilevazioneSezioneView.bind(getItem(position)!!)

        return rilevazioneSezioneView
    }
}
