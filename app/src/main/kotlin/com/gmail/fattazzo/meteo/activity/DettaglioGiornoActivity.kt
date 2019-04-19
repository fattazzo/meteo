/*
 * Project: meteo
 * File: DettaglioGiornoActivity.kt
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

package com.gmail.fattazzo.meteo.activity

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.domain.json.previsione.Fascia
import com.gmail.fattazzo.meteo.domain.json.previsione.Giorno
import com.gmail.fattazzo.meteo.fragment.home.dettaglio.FasceHeaderAdapter
import com.gmail.fattazzo.meteo.fragment.home.dettaglio.fasciasection.FasceAdapter
import com.gmail.fattazzo.meteo.utils.ItemOffsetDecoration
import org.androidannotations.annotations.*
import java.text.SimpleDateFormat
import java.util.*

@EActivity(R.layout.activity_dettaglio_giorno)
open class DettaglioGiornoActivity : BaseActivity(), FasceHeaderAdapter.OnClickListener {

    @Extra
    lateinit var giorno: Giorno

    @ViewById
    internal lateinit var dataTV: TextView

    @ViewById
    internal lateinit var fasceRecyclerView: RecyclerView

    @ViewById
    internal lateinit var fasceHeaderRecyclerView: RecyclerView

    @AfterViews
    fun initViews() {
        val dateFormat = SimpleDateFormat("EEEE dd/MM/yyyy", Locale.ITALIAN)

        dataTV.text = dateFormat.format(giorno.data).capitalize()

        fasceHeaderRecyclerView.addItemDecoration(ItemOffsetDecoration(0, 0, 10, 10))
        fasceHeaderRecyclerView.adapter = FasceHeaderAdapter(giorno.fasce.orEmpty(), this, this)

        updateDetailView(giorno.fasce?.firstOrNull())
    }

    override fun onClick(fascia: Fascia) {
        updateDetailView(fascia)
    }

    @UiThread(delay = 50)
    open fun updateDetailView(fascia: Fascia?) {
        val fasceAdapter = FasceAdapter(giorno,fascia, this)
        fasceRecyclerView.adapter = fasceAdapter
        //fasceAdapter.notifyDataSetChanged()
        fasceRecyclerView.scheduleLayoutAnimation()
    }
}
