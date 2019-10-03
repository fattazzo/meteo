/*
 * Project: meteo
 * File: GraficoDatiStazioneFragment.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.meteo.rilevazioni.dati.grafico

import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.LineData
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.rilevazioni.dati.IDatoStazioneFragment
import com.gmail.fattazzo.meteo.data.stazioni.meteo.domain.datistazione.IDatoStazione
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById


/**
 * @author fattazzo
 *         <p/>
 *         date: 07/11/17
 */
@EFragment(R.layout.dato_stazione_meteo_grafico)
open class GraficoDatiStazioneFragment : Fragment(),
    IDatoStazioneFragment {

    @Bean
    internal lateinit var dataSetBuilder: DataSetBuilder

    @ViewById
    internal lateinit var lineChart: LineChart

    override var dati: List<IDatoStazione>? = mutableListOf()

    @AfterViews
    open fun initViews() {

        val dataSetList = dataSetBuilder.buildDataset(dati)

        val l = lineChart.legend
        l.formSize = 10f
        l.form = Legend.LegendForm.CIRCLE

        val xAxis = lineChart.xAxis
        xAxis.valueFormatter =
            DateXAxisValueFormatter()
        xAxis.granularity = 3f

        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.valueFormatter =
            UMValueFormatter(
                dati?.firstOrNull()?.unitaMisura.orEmpty()
            )

        val charDescription = Description()
        charDescription.text = ""
        lineChart.description = charDescription

        val data = LineData(dataSetList)
        lineChart.data = data
        lineChart.invalidate()
    }
}