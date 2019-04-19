/*
 * Project: meteo
 * File: BollettinoProbabilisticoFragment.kt
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

package com.gmail.fattazzo.meteo.fragment.bollettino.probabilistico

import android.graphics.Color
import android.text.Html
import android.util.SparseIntArray
import android.view.Gravity
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.domain.json.bollettino.probabilistico.BollettinoProbabilistico
import com.gmail.fattazzo.meteo.fragment.BaseFragment
import com.gmail.fattazzo.meteo.fragment.home.HomeFragment_
import com.gmail.fattazzo.meteo.manager.MeteoManager
import com.gmail.fattazzo.meteo.utils.FragmentUtils
import org.androidannotations.annotations.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author fattazzo
 *
 *
 * date: 13/lug/2014
 */
@OptionsMenu(R.menu.boll_prob_menu)
@EFragment(R.layout.fragment_boll_probabilistico)
open class BollettinoProbabilisticoFragment : BaseFragment() {

    @Bean
    internal lateinit var meteoManager: MeteoManager

    @ViewById
    internal lateinit var tableLayout: TableLayout

    @ViewById
    internal lateinit var tableLayoutFenomeni: TableLayout

    private var bollettinoCorrente: BollettinoProbabilistico? = null

    private lateinit var fenomeniColor: SparseIntArray

    override fun getTitleResId(): Int = R.string.nav_boll_prob

    /**
     * Crea una riga standard per la tabella.
     *
     * @return riga creata
     */
    private fun createTableRow(): TableRow {
        val tr = TableRow(context)
        tr.setBackgroundColor(Color.LTGRAY)
        tr.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT)
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
    private fun createTextView(text: String, backgroundColor: Int = Color.WHITE, paramWidth: Int? = null,
                               gravity: Int = Gravity.CENTER, span: Int = 1): TextView {
        val textView = TextView(context)
        textView.text = text
        val layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f)
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
        val legendPrecipAbbtextView = view!!.findViewById<View>(R.id.boll_prob_legenda_precip_abbondanti) as TextView
        legendPrecipAbbtextView.text = Html.fromHtml(view!!.context.getString(R.string.legend_heavy_rainfall))
        val legendRovTemptextView = view!!.findViewById<View>(R.id.boll_prob_legenda_rov_temporali) as TextView
        legendRovTemptextView.text = Html.fromHtml(view!!.context.getString(R.string.legend_showers_or_storms))
        val legendVentiMonttextView = view!!.findViewById<View>(R.id.boll_prob_legenda_venti_forti_montagna) as TextView
        legendVentiMonttextView.text = Html.fromHtml(view!!.context
                .getString(R.string.legend_strong_winds_mountain))
        val legendVentiValletextView = view!!.findViewById<View>(R.id.boll_prob_legenda_venti_forti_valle) as TextView
        legendVentiValletextView.text = Html
                .fromHtml(view!!.context.getString(R.string.legend_strong_winds_valley))
        val legendNevicatetextView = view!!.findViewById<View>(R.id.boll_prob_legenda_nevicate) as TextView
        legendNevicatetextView.text = Html.fromHtml(view!!.context.getString(R.string.legend_snowfall))
        val legendCaldoIntensotextView = view!!.findViewById<View>(R.id.boll_prob_legenda_caldo_intenso) as TextView
        legendCaldoIntensotextView.text = Html.fromHtml(view!!.context.getString(R.string.legend_intense_warm))
        val legendFreddoIntensotextView = view!!.findViewById<View>(R.id.boll_prob_legenda_freddo_intenso) as TextView
        legendFreddoIntensotextView.text = Html.fromHtml(view!!.context.getString(R.string.legend_intense_cold))
    }

    @AfterViews
    fun initViews() {
        caricaBollettino()

        initLegendaFenomeni()
    }

    @AfterInject
    fun init() {
        // inizializzo l'array dei colori da utilizzare
        fenomeniColor = SparseIntArray(4)
        fenomeniColor.append(0, R.color.boll_prob_fenomeno_0)
        fenomeniColor.append(1, R.color.boll_prob_fenomeno_1)
        fenomeniColor.append(2, R.color.boll_prob_fenomeno_2)
        fenomeniColor.append(3, R.color.boll_prob_fenomeno_3)
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    open fun caricaBollettino() {
        openIndeterminateDialog(getString(R.string.bull_prob_loading_dialog))
        downloadBollettino()
    }

    @Background
    open fun downloadBollettino() {
        try {
            if (bollettinoCorrente == null) {
                bollettinoCorrente = meteoManager.caricaBollettinoProbabilistico()
            }
        } finally {
            closeIndeterminateDialog()
        }
        buildTableView()
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    open fun buildTableView() {
        try {
            tableLayout.removeAllViews()
            tableLayoutFenomeni.removeAllViews()

            buildGiorniRow()
            buildFasceRow()
            buildFenomeniRows()
        } catch (e: Exception) {
            e.printStackTrace()
            bollettinoCorrente = null
        }
    }

    private fun buildGiorniRow() {
        val row = createTableRow()
        for (giorno in bollettinoCorrente?.giorni.orEmpty()) {
            var desc = giorno.nome.orEmpty().capitalize() + "\n"
            if (giorno.data != null) {
                desc += SimpleDateFormat("dd MMMM", Locale.getDefault()).format(giorno.data)
            }
            val textView = createTextView(desc, span = giorno.fasce.orEmpty().size)
            row.addView(textView)
        }
        tableLayout.addView(row, TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT))
    }

    private fun buildFasceRow() {
        val row = createTableRow()
        for (giorno in bollettinoCorrente?.giorni.orEmpty()) {
            giorno.fasce.orEmpty()
                    .map { createTextView(it.ore.orEmpty()) }
                    .forEach { row.addView(it) }
        }
        tableLayout.addView(row, TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT))
    }

    private fun buildFenomeniRows() {
        val elencoFenomeni: Set<String> = bollettinoCorrente?.fenomeniPresenti()!!

        for (fenomenoCorrente in elencoFenomeni) {
            val rowFenomeni = createTableRow()
            rowFenomeni.addView(createTextView(fenomenoCorrente.capitalize(), gravity = Gravity.LEFT))
            tableLayoutFenomeni.addView(rowFenomeni, TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT))

            val row = createTableRow()
            for (giorno in bollettinoCorrente?.giorni.orEmpty()) {
                for (fascia in giorno.fasce.orEmpty()) {
                    for (fenomeno in fascia.fenomeni.orEmpty()) {
                        if (fenomenoCorrente == fenomeno.nome) {
                            val color = if (fenomeniColor[fenomeno.valore] != 0) resources.getColor(fenomeniColor[fenomeno.valore]) else Color.LTGRAY
                            val textView = createTextView(fenomeno.valore.toString(), backgroundColor = color)
                            row.addView(textView)
                        }
                    }
                }
            }
            tableLayout.addView(row, TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT))
        }
    }

    @OptionsItem
    fun refreshAction() {
        bollettinoCorrente = null
        caricaBollettino()
    }

    override fun backPressed(): Boolean {
        FragmentUtils.replace(activity as AppCompatActivity, HomeFragment_.builder().build())
        return true
    }

}
