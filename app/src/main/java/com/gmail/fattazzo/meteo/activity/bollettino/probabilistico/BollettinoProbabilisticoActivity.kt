/*
 * Project: meteo
 * File: BollettinoProbabilisticoActivity.kt
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

package com.gmail.fattazzo.meteo.activity.bollettino.probabilistico

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.SparseIntArray
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.BaseActivity
import com.gmail.fattazzo.meteo.app.MeteoApplication
import com.gmail.fattazzo.meteo.app.module.viewmodel.DaggerViewModelFactory
import com.gmail.fattazzo.meteo.databinding.ActivityBollettinoProbabilisticoBinding
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.fragment_boll_probabilistico.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 28/09/19
 */
class BollettinoProbabilisticoActivity : BaseActivity<ActivityBollettinoProbabilisticoBinding>() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    private lateinit var viewModel: BollettinoProbabilisticoViewModel

    private val fenomeniColor = SparseIntArray(4).apply {
        append(0, R.color.boll_prob_fenomeno_0)
        append(1, R.color.boll_prob_fenomeno_1)
        append(2, R.color.boll_prob_fenomeno_2)
        append(3, R.color.boll_prob_fenomeno_3)
    }

    override fun getLayoutResID(): Int = R.layout.activity_bollettino_probabilistico

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MeteoApplication).appComponent.inject(this)

        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(BollettinoProbabilisticoViewModel::class.java)

        initToolbar(binding.appBarMain.toolbar)

        initLegendaFenomeni()

        viewModel.bollettino.observe(this, Observer { buildTableView() })

        viewModel.caricaBollettino(false)
    }

    private fun buildTableView() {
        try {
            binding.contentLayout.tableLayout.removeAllViews()
            binding.contentLayout.tableLayoutFenomeni.removeAllViews()

            buildGiorniRow()
            buildFasceRow()
            buildFenomeniRows()
        } catch (e: Exception) {
            e.printStackTrace()
            viewModel.bollettino.postValue(null)
        }
    }

    private fun buildGiorniRow() {
        val row = createTableRow()
        for (giorno in viewModel.bollettino.value?.giorni.orEmpty()) {
            var desc = giorno.nome.orEmpty().capitalize() + "\n"
            if (giorno.data != null) {
                desc += SimpleDateFormat("dd MMMM", Locale.getDefault()).format(giorno.data)
            }
            val textView = createTextView(desc, span = giorno.fasce.orEmpty().size)
            row.addView(textView)
        }
        binding.contentLayout.tableLayout.addView(
            row,
            TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun buildFasceRow() {
        val row = createTableRow()
        for (giorno in viewModel.bollettino.value?.giorni.orEmpty()) {
            giorno.fasce.orEmpty()
                .map { createTextView(it.ore.orEmpty()) }
                .forEach { row.addView(it) }
        }
        binding.contentLayout.tableLayout.addView(
            row,
            TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun buildFenomeniRows() {
        val elencoFenomeni: Set<String> = viewModel.bollettino.value?.fenomeniPresenti()!!

        for (fenomenoCorrente in elencoFenomeni) {
            val rowFenomeni = createTableRow()
            rowFenomeni.addView(
                createTextView(
                    fenomenoCorrente.capitalize(),
                    gravity = Gravity.LEFT
                )
            )
            binding.contentLayout.tableLayoutFenomeni.addView(
                rowFenomeni,
                TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
            )

            val row = createTableRow()
            for (giorno in viewModel.bollettino.value?.giorni.orEmpty()) {
                for (fascia in giorno.fasce.orEmpty()) {
                    for (fenomeno in fascia.fenomeni.orEmpty()) {
                        if (fenomenoCorrente == fenomeno.nome) {
                            val color = if (fenomeniColor[fenomeno.valore] != 0) resources.getColor(
                                fenomeniColor[fenomeno.valore]
                            ) else Color.LTGRAY
                            val textView =
                                createTextView(fenomeno.valore.toString(), backgroundColor = color)
                            row.addView(textView)
                        }
                    }
                }
            }
            binding.contentLayout.tableLayout.addView(
                row,
                TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }
    }

    /**
     * Crea una riga standard per la tabella.
     *
     * @return riga creata
     */
    private fun createTableRow(): TableRow {
        val tr = TableRow(this)
        tr.setBackgroundColor(Color.LTGRAY)
        tr.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        return tr
    }

    /**
     * Crea un [TextView] standard da utilizzare nella tabella.
     *
     * @param text             testo da visualizzare
     * @param backgroundColor colore
     * @param paramWidth       width della view
     * @param gravity          gravity della view
     * @return textView
     */
    private fun createTextView(
        text: String, backgroundColor: Int = Color.WHITE, paramWidth: Int? = null,
        gravity: Int = Gravity.CENTER, span: Int = 1
    ): TextView {
        val textView = TextView(this)
        textView.text = text
        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.MATCH_PARENT,
            1.0f
        )
        layoutParams.setMargins(1, 1, 1, 1)
        layoutParams.span = span
        if (paramWidth != null) {
            layoutParams.width = paramWidth
        }
        textView.layoutParams = layoutParams
        textView.setPadding(8, 4, 4, 2)

        textView.setBackgroundColor(backgroundColor)
        textView.gravity = gravity

        return textView
    }

    /**
     * Carica la legenda dei fenomeni dallo strings.xml.
     */
    private fun initLegendaFenomeni() {
        binding.contentLayout.boll_prob_legenda_precip_abbondanti.text =
            Html.fromHtml(this.getString(R.string.legend_heavy_rainfall))

        binding.contentLayout.boll_prob_legenda_rov_temporali.text =
            Html.fromHtml(this.getString(R.string.legend_showers_or_storms))

        binding.contentLayout.boll_prob_legenda_venti_forti_montagna.text = Html.fromHtml(
            this
                .getString(R.string.legend_strong_winds_mountain)
        )

        binding.contentLayout.boll_prob_legenda_venti_forti_valle.text = Html
            .fromHtml(this.getString(R.string.legend_strong_winds_valley))

        binding.contentLayout.boll_prob_legenda_nevicate.text =
            Html.fromHtml(this.getString(R.string.legend_snowfall))

        binding.contentLayout.boll_prob_legenda_caldo_intenso.text =
            Html.fromHtml(this.getString(R.string.legend_intense_warm))

        binding.contentLayout.boll_prob_legenda_freddo_intenso.text =
            Html.fromHtml(this.getString(R.string.legend_intense_cold))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.boll_prob_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refreshAction -> {
                viewModel.caricaBollettino(true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}