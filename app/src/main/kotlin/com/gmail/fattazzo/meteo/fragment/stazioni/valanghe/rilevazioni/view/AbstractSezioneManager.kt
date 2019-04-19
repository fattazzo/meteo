/*
 * Project: meteo
 * File: AbstractSezioneManager.kt
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

package com.gmail.fattazzo.meteo.fragment.stazioni.valanghe.rilevazioni.view

import android.view.View
import com.gmail.fattazzo.meteo.domain.xml.stazioni.valanghe.dati.DatoStazione
import com.gmail.fattazzo.meteo.fragment.stazioni.valanghe.rilevazioni.LegendaDatiNeveManager
import com.gmail.fattazzo.meteo.fragment.stazioni.valanghe.rilevazioni.LegendaDatiNeveManager_
import com.gmail.fattazzo.meteo.utils.Fx

/**
 *
 * @author fattazzo
 *
 * date: 03/feb/2016
 */
abstract class AbstractSezioneManager(protected val view: View) : IRilevazioneSezioneViewManager {

    private val toggleView: View by lazy { view.findViewById<View>(toggleViewId) }

    private var loadedContent = false

    var datiStazione: List<DatoStazione>? = null

    internal val legendaDatiNeveManager: LegendaDatiNeveManager by lazy {
        LegendaDatiNeveManager_.getInstance_(view.context)
    }

    /**
     * @return id della risorsa utilizzata per visualizzare/nascondere la sezione
     */
    internal abstract val headerViewId: Int

    /**
     * @return id della view da visualizzare/nascondere
     */
    protected abstract val toggleViewId: Int

    override fun clear() {

        clearContent()

        loadedContent = false

        hide()

    }

    /**
     * Cancella il contenuto della sezione.
     */
    protected abstract fun clearContent()

    /**
     * Nasconde la view dei dati della sezione.
     */
    private fun hide() {
        Fx.slideUp(toggleView)
    }

    /**
     * Carica i dati da visualizzare.
     */
    protected abstract fun loadContent()

    /**
     * Visualizza la view dei dati della sezione.
     */
    private fun show() {
        Fx.slideDown(toggleView)
    }

    override fun toggleContent() {

        if (toggleView.isShown) {
            hide()
        } else {
            if (!loadedContent) {
                loadContent()
                loadedContent = true
            }
            show()
        }

    }

}
