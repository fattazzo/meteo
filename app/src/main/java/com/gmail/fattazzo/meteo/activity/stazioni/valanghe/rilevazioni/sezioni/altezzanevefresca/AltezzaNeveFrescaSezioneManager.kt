/*
 * Project: meteo
 * File: AltezzaNeveFrescaSezioneManager.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.altezzanevefresca

import android.view.View
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.RilevazioneSezione
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.RilevazioneSezioneListAdapter
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.view.AbstractSezioneManager
import com.gmail.fattazzo.meteo.utils.ExpandableHeightListView
import java.util.*

/**
 * @author fattazzo
 *
 *
 * date: 03/feb/2016
 */
class AltezzaNeveFrescaSezioneManager
/**
 * Costruttore.
 *
 * @param view view
 */
    (view: View) : AbstractSezioneManager(view) {

    override val headerViewId: Int
        get() = R.id.anag_stazione_neve_altezzanevefresca_layout

    override val toggleViewId: Int
        get() = R.id.anag_stazione_neve_altezzanevefrescaData_layout

    override fun clearContent() {
        val condTempoList = view
            .findViewById<View>(R.id.neve_altezzanevefresca_list) as ExpandableHeightListView
        condTempoList.adapter = null
    }

    override fun loadContent() {
        val altezzaNeveFrescaList = ArrayList<RilevazioneSezione>()
        for (dato in datiStazione!!) {
            val altezzaNeveFresca = RilevazioneSezione()
            altezzaNeveFresca.data = dato.dataMisurazione
            val descrizione = legendaDatiNeveManager.parseAltezzaNeveFresca(dato.altezzaNeveFresca)
            altezzaNeveFresca.descrizione = descrizione
            altezzaNeveFrescaList.add(altezzaNeveFresca)
        }

        val adapter = RilevazioneSezioneListAdapter(view.context, altezzaNeveFrescaList)

        val listView =
            view.findViewById<View>(R.id.neve_altezzanevefresca_list) as ExpandableHeightListView
        listView.adapter = adapter
        listView.isExpanded = true

    }
}
