/*
 * Project: meteo
 * File: TemperaturaMaxMinSezioneManager.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.temperaturamaxmin

import android.view.View
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.RilevazioneSezione
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.RilevazioneSezioneListAdapter
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.view.AbstractSezioneManager
import com.gmail.fattazzo.meteo.utils.ExpandableHeightListView
import java.util.*

/**
 *
 * @author fattazzo
 *
 * date: 03/feb/2016
 */
class TemperaturaMaxMinSezioneManager
/**
 * Costruttore.
 *
 * @param view
 * view
 */
    (view: View) : AbstractSezioneManager(view) {

    override val headerViewId: Int
        get() = R.id.anag_stazione_neve_temperaturaminmax_layout

    override val toggleViewId: Int
        get() = R.id.anag_stazione_neve_temperaturaminmaxData_layout

    override fun clearContent() {
        val condTempoList = view
            .findViewById<View>(R.id.neve_temperaturaminmax_list) as ExpandableHeightListView
        condTempoList.adapter = null
    }

    override fun loadContent() {
        val temperaturaList = ArrayList<RilevazioneSezione>()
        for (dato in datiStazione!!) {
            val temperatura = RilevazioneSezione()
            temperatura.data = dato.dataMisurazione
            var descrizione = legendaDatiNeveManager.parseTemperatura(dato.temperaturaMinima)
            descrizione += " - "
            descrizione += legendaDatiNeveManager.parseTemperatura(dato.temperaturaMassima)
            temperatura.descrizione = descrizione
            temperaturaList.add(temperatura)
        }

        val adapter = RilevazioneSezioneListAdapter(view.context, temperaturaList)

        val listView =
            view.findViewById<View>(R.id.neve_temperaturaminmax_list) as ExpandableHeightListView
        listView.adapter = adapter
        listView.isExpanded = true

    }
}
