/*
 * Project: meteo
 * File: DataSetBuilder.kt
 *
 * Created by fattazzo
 * Copyright © 2018 Gianluca Fattarsi. All rights reserved.
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

package com.gmail.fattazzo.meteo.fragment.stazioni.meteo.rilevazioni.dati.grafico

import android.content.Context
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.gmail.fattazzo.meteo.domain.xml.stazioni.meteo.datistazione.IDatoStazione
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 06/12/17
 */
@EBean
open class DataSetBuilder {

    @RootContext
    internal lateinit var context: Context

    @Bean
    lateinit var preferencesManager: ApplicationPreferencesManager

    open fun buildDataset(datiParam: List<IDatoStazione>?, applySettings: Boolean = true): ArrayList<ILineDataSet> {

        val dati = datiParam.orEmpty().sortedBy { it.data }

        val entries = createChartEntries(dati)

        return buildDataset(entries, applySettings)
    }

    private fun createChartEntries(dati: List<IDatoStazione>): HashMap<String, MutableList<Entry>> {
        val entries = HashMap<String, MutableList<Entry>>()

        val calendar = Calendar.getInstance()
        val refTime = Calendar.getInstance()

        var currentDay: String
        for (dato in dati) {
            calendar.time = dato.data

            refTime.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY))
            refTime.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE))
            refTime.set(Calendar.SECOND, calendar.get(Calendar.SECOND))

            val entry = Entry(refTime.timeInMillis.toFloat(), dato.valore.toFloat())
            currentDay = calendar.get(Calendar.DAY_OF_MONTH).toString() + "/" + (calendar.get(Calendar.MONTH) + 1)

            var entryDay: MutableList<Entry>? = entries[currentDay]
            if (entryDay == null) {
                entryDay = ArrayList()
            }
            entryDay.add(entry)
            entries[currentDay] = entryDay
        }
        return entries
    }

    private fun buildDataset(entries: HashMap<String, MutableList<Entry>>, applySettings: Boolean): ArrayList<ILineDataSet> {
        var serieNumber = 0
        val dataSetList = ArrayList<ILineDataSet>()
        for ((key, value) in entries) {
            val dataSet = LineDataSet(value, key)
            dataSet.lineWidth = 1f

            dataSet.color = GraphSeriesColorProvider.getSeriesColor(serieNumber)
            dataSet.setDrawFilled(preferencesManager.showStazMeteoGraphShowSeriesBackground())
            dataSet.fillColor = GraphSeriesColorProvider.getSeriesColor(serieNumber)
            dataSet.setDrawCircles(false)

            if (applySettings) {
                dataSet.setDrawCircles(preferencesManager.showStazMeteoGraphPoint())
                dataSet.setCircleColor(GraphSeriesColorProvider.getSeriesColor(serieNumber))
                dataSet.circleRadius = preferencesManager.getStazMeteoGraphPointRadius().toFloat()
            }

            dataSetList.add(dataSet)
            serieNumber++
        }
        return dataSetList
    }
}