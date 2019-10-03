/*
 * Project: meteo
 * File: StazioneValangheListAdapter.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.valanghe.anagrafica

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail.fattazzo.meteo.db.StazioneValanghe

/**
 *
 * @author fattazzo
 *
 * date: 01/feb/2016
 */
class StazioneValangheListAdapter(var data: List<StazioneValanghe>, val context: Context) :
    RecyclerView.Adapter<StazioneValangheListAdapter.StazioneViewHolder>() {

    private var oldPos = 0

    override fun onBindViewHolder(holder: StazioneViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener {
            oldPos = position
            notifyDataSetChanged()
            //clickListener.onClick(data[position])
        }

        //(holder.itemView as StazioneValangheView).setSelectedView(oldPos == position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StazioneViewHolder {
        return StazioneViewHolder(
            StazioneValangheView_.build(
                context
            )
        )
    }

    override fun getItemCount(): Int = data.size

    inner class StazioneViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(stazioneValanghe: StazioneValanghe) {
            (itemView as StazioneValangheView).bind(stazioneValanghe)
        }
    }

    interface OnClickListener {
        fun onClick(stazioneValanghe: StazioneValanghe)
    }
}
