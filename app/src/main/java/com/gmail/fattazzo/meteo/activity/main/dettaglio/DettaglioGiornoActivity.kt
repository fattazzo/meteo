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
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Giorni
import com.gmail.fattazzo.meteo.databinding.ActivityDettaglioGiornoBinding
import com.gmail.fattazzo.meteo.databinding.converters.GiornoConverter
import com.gmail.fattazzo.meteo.utils.ItemOffsetDecoration

open class DettaglioGiornoActivity : BaseActivity<ActivityDettaglioGiornoBinding>() {

    companion object {
        const val GIORNO_EXTRA = "giorno"
    }

    lateinit var viewModel: DettaglioGiornoViewModel

    override fun getLayoutResID(): Int = R.layout.activity_dettaglio_giorno

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(DettaglioGiornoViewModel::class.java)
        binding.model = viewModel

        viewModel.giorno.observe(this, androidx.lifecycle.Observer { giorno ->
            binding.dataTV.text = GiornoConverter.giornoData(giorno)

            binding.fasceHeaderRecyclerView.addItemDecoration(ItemOffsetDecoration(0, 0, 10, 10))
            binding.fasceHeaderRecyclerView.adapter =
                FasceHeaderAdapter(giorno.fasce.orEmpty(), this) { viewModel.fascia.postValue(it) }
        })

        if (intent.extras?.containsKey(GIORNO_EXTRA) == true) {
            viewModel.giorno.postValue(intent.extras!!.getSerializable(GIORNO_EXTRA) as Giorni?)
        }
    }
}
