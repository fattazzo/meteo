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

package com.gmail.fattazzo.meteo.activity.main.dettaglio

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.BaseActivity
import com.gmail.fattazzo.meteo.activity.main.dettaglio.fasciasection.FasceAdapter
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Fasce
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Giorni
import com.gmail.fattazzo.meteo.databinding.ActivityDettaglioGiornoBinding
import com.gmail.fattazzo.meteo.utils.ItemOffsetDecoration
import java.text.SimpleDateFormat
import java.util.*

open class DettaglioGiornoActivity : BaseActivity<ActivityDettaglioGiornoBinding>(),
    FasceHeaderAdapter.OnClickListener {

    companion object {
        const val GIORNO_EXTRA = "giorno"
    }

    lateinit var viewModel: DettaglioGiornoViewModel

    override fun getLayoutResID(): Int = R.layout.activity_dettaglio_giorno

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(DettaglioGiornoViewModel::class.java)
        binding.model = viewModel

        viewModel.giorno.observe(this, androidx.lifecycle.Observer {
            val dateFormat = SimpleDateFormat("EEEE dd/MM/yyyy", Locale.ITALIAN)

            binding.dataTV.text = dateFormat.format(it.data).capitalize()

            binding.fasceHeaderRecyclerView.addItemDecoration(ItemOffsetDecoration(0, 0, 10, 10))
            binding.fasceHeaderRecyclerView.adapter =
                FasceHeaderAdapter(
                    it.fasce.orEmpty(),
                    this,
                    this
                )

            updateDetailView(it.fasce?.firstOrNull())
        })

        if (intent.extras?.containsKey(GIORNO_EXTRA) == true) {
            viewModel.giorno.postValue(intent.extras!!.getSerializable(GIORNO_EXTRA) as Giorni?)
        }
    }

    override fun onClick(fascia: Fasce) {
        updateDetailView(fascia)
    }

    open fun updateDetailView(fasce: Fasce?) {
        val fasceAdapter = FasceAdapter(viewModel.giorno.value, fasce, this)
        binding.fasceRecyclerView.adapter = fasceAdapter
        binding.fasceRecyclerView.scheduleLayoutAnimation()
    }
}