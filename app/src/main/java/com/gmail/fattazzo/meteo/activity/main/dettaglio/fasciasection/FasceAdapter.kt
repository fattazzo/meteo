/*
 * Project: meteo
 * File: FasceAdapter.kt
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

package com.gmail.fattazzo.meteo.activity.main.dettaglio.fasciasection

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Fasce
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Giorni


/**
 * @author fattazzo
 *         <p/>
 *         date: 01/06/17
 */
open class FasceAdapter(val giorno: Giorni?, fasce: Fasce?, val context: Context) :
    RecyclerView.Adapter<FasceAdapter.FasciaViewHolder>() {

    var data: List<Fasce> = SectionBuilder.build(fasce)

    override fun onBindViewHolder(holder: FasciaViewHolder, position: Int) {
        holder.bind(data[position], Sections.values()[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FasciaViewHolder {
        return FasciaViewHolder(FasciaView(context))
    }

    override fun getItemCount(): Int = data.size

    inner class FasciaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            val layoutParams = StaggeredGridLayoutManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.isFullSpan = true
            itemView.layoutParams = layoutParams
            itemView.setBackgroundColor(Color.TRANSPARENT)
        }

        fun bind(fasce: Fasce, sections: Sections) {
            (itemView as FasciaView).bind(giorno, fasce, sections)
        }
    }
}