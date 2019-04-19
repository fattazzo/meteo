/*
 * Project: meteo
 * File: RadarAdapter.kt
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
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail.fattazzo.meteo.fragment.radar.RadarModel
import com.gmail.fattazzo.meteo.fragment.radar.RadarView
import com.gmail.fattazzo.meteo.fragment.radar.RadarView_


/**
 * @author fattazzo
 *         <p/>
 *         date: 01/06/17
 */
open class RadarAdapter(var data: List<RadarModel>, val context: Context, val clickListener: OnClickListener) : RecyclerView.Adapter<RadarAdapter.RadarViewHolder>() {

    private var oldPos = 0

    override fun onBindViewHolder(holder: RadarViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener {
            oldPos = position
            notifyDataSetChanged()
            clickListener.onClick(data[position])
        }

        (holder.itemView as RadarView).setSelectedView(oldPos == position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadarViewHolder {
        return RadarViewHolder(RadarView_.build(context))
    }

    override fun getItemCount(): Int = data.size

    inner class RadarViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(radarModel: RadarModel) {
            (itemView as RadarView).bind(radarModel)
        }
    }

    interface OnClickListener {
        fun onClick(radarModel: RadarModel)
    }
}