/*
 * Project: meteo
 * File: FasceHeaderAdapter.kt
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

package com.gmail.fattazzo.meteo.activity.main.dettaglio

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Fasce


/**
 * @author fattazzo
 *         <p/>
 *         date: 01/06/17
 */
open class FasceHeaderAdapter(
    var data: List<Fasce>,
    val context: Context,
    private val clickListener: OnClickListener
) : RecyclerView.Adapter<FasceHeaderAdapter.FasciaViewHolder>() {

    private var oldPos = 0

    override fun onBindViewHolder(holder: FasciaViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener {
            oldPos = position
            notifyDataSetChanged()
            clickListener.onClick(data[position])
        }

        (holder.itemView as FasciaHeaderView).setSelectedView(oldPos == position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FasciaViewHolder =
        FasciaViewHolder(FasciaHeaderView(context))

    override fun getItemCount(): Int = data.size

    inner class FasciaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(fascia: Fasce) {
            (itemView as FasciaHeaderView).bind(fascia)
        }
    }

    interface OnClickListener {
        fun onClick(fascia: Fasce)
    }
}