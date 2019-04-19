/*
 * Project: meteo
 * File: PrevisioneAdapter.kt
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
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gmail.fattazzo.meteo.domain.json.previsione.Giorno
import com.gmail.fattazzo.meteo.domain.json.previsione.PrevisioneLocalita


/**
 * @author fattazzo
 *         <p/>
 *         date: 06/12/17
 */
open class PrevisioneAdapter(var data: MutableList<Any?>, val context: Context) : RecyclerView.Adapter<PrevisioneAdapter.PrevisioneViewHolder>() {
    override fun onBindViewHolder(holder: PrevisioneViewHolder, position: Int) {
        val roundBorderTop = position == 0
        val roundBorderBottom = position == itemCount - 1
        holder.bind(data[position], roundBorderTop, roundBorderBottom)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrevisioneViewHolder {
        return if (viewType == 0) {
            PrevisioneLocalitaViewHolder(PrevisioneView_.build(context))
        } else {
            GiornoViewHolder(GiornoView_.build(context))
        }
    }

    override fun getItemViewType(position: Int): Int = if (data[position] is PrevisioneLocalita) 0 else 1

    override fun getItemCount(): Int = data.size

    abstract inner class PrevisioneViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setBackgroundColor(Color.TRANSPARENT)
        }

        abstract fun bind(data: Any?, roundBorderTop: Boolean, roundBorderBottom: Boolean)
    }

    inner class PrevisioneLocalitaViewHolder(view: View) : PrevisioneViewHolder(view) {

        init {
            val layoutParams = StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.isFullSpan = true
            itemView.layoutParams = layoutParams
        }

        override fun bind(data: Any?, roundBorderTop: Boolean, roundBorderBottom: Boolean) {
            if (data != null)
                (itemView as PrevisioneView).bind(data as PrevisioneLocalita,roundBorderTop,roundBorderBottom)
        }
    }

    inner class GiornoViewHolder(view: View) : PrevisioneViewHolder(view) {
        override fun bind(data: Any?, roundBorderTop: Boolean, roundBorderBottom: Boolean) {
            if (data != null)
                (itemView as GiornoView).bind(data as Giorno,roundBorderTop,roundBorderBottom)
        }

    }
}