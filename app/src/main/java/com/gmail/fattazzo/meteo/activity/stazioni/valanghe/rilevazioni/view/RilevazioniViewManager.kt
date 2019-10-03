/*
 * Project: meteo
 * File: RilevazioniViewManager.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.view

import android.util.SparseArray
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.altezzamantonevoso.AltezzaMantoNevosoSezioneManager
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.altezzanevefresca.AltezzaNeveFrescaSezioneManager
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.brinasuperficie.BrinaSuperficieSezioneManager
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.condtempo.CondTempoSezioneManager
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.nuvolisita.NuvolositaSezioneManager
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.rugositasuperficiale.RugositaSuperficialeSezioneManager
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.stratosuperficiale.StratoSuperficialeSezioneManager
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.temperaturaaria.TemperaturaAriaSezioneManager
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.temperaturamaxmin.TemperaturaMaxMinSezioneManager
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.ventoquotafenomeni.VentoQuotaFenomeniSezioneManager
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.ventoquotatipo.VentoQuotaTipoSezioneManager
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.rilevazioni.sezioni.visibilita.VisibilitaSezioneManager
import com.gmail.fattazzo.meteo.data.stazioni.valanghe.domain.dati.DatoStazione
import java.util.*

/**
 * @author fattazzo
 *
 *
 * date: 03/feb/2016
 */
class RilevazioniViewManager(private val view: View) {

    private val sezioni: SparseArray<AbstractSezioneManager> = SparseArray()

    private var codiceStazione: String? = null

    private var datiStazione: MutableList<DatoStazione>? = null

    init {

        sezioni.put(R.id.anag_stazione_neve_condMeteo_layout, CondTempoSezioneManager(view))
        sezioni.put(R.id.anag_stazione_neve_nuvolosita_layout, NuvolositaSezioneManager(view))
        sezioni.put(R.id.anag_stazione_neve_visibilita_layout, VisibilitaSezioneManager(view))
        sezioni.put(R.id.anag_stazione_neve_vqt_layout, VentoQuotaTipoSezioneManager(view))
        sezioni.put(R.id.anag_stazione_neve_vqf_layout, VentoQuotaFenomeniSezioneManager(view))
        sezioni.put(
            R.id.anag_stazione_neve_stratosuperficiale_layout,
            StratoSuperficialeSezioneManager(view)
        )
        sezioni.put(
            R.id.anag_stazione_neve_rugositasuperficiale_layout,
            RugositaSuperficialeSezioneManager(view)
        )
        sezioni.put(
            R.id.anag_stazione_neve_brinasuperficie_layout,
            BrinaSuperficieSezioneManager(view)
        )
        sezioni.put(
            R.id.anag_stazione_neve_temperaturaaria_layout,
            TemperaturaAriaSezioneManager(view)
        )
        sezioni.put(
            R.id.anag_stazione_neve_temperaturaminmax_layout,
            TemperaturaMaxMinSezioneManager(view)
        )
        sezioni.put(
            R.id.anag_stazione_neve_altezzamantonevoso_layout,
            AltezzaMantoNevosoSezioneManager(view)
        )
        sezioni.put(
            R.id.anag_stazione_neve_altezzanevefresca_layout,
            AltezzaNeveFrescaSezioneManager(view)
        )
    }

    /**
     * Filtra i dati di tutte le stazioni estraendo quella corrente.
     *
     * @param datiiStazioni dati di tutte le stazioni
     */
    private fun filtraDatiStazioni(datiiStazioni: List<DatoStazione>) {
        datiStazione = ArrayList()
        datiiStazioni.filter { it.codiceStazione == codiceStazione }.forEach {
            datiStazione!!.add(it)
        }
    }

    /**
     * Registra il listener alle viste che espandono le sezioni.
     *
     * @param clickListener listener
     */
    fun registerOnCLickListener(clickListener: OnClickListener) {

        var i = 0
        val nsize = sezioni.size()
        while (i < nsize) {
            val sezione = sezioni.valueAt(i)

            val viewRegister = view.findViewById<View>(sezione.headerViewId)
            viewRegister?.setOnClickListener(clickListener)
            i++
        }
    }

    /**
     * Setta la stazione corrente.
     *
     * @param stazione      stazione corrente
     * @param datiiStazioni dati di tutte le stazioni
     */
    fun setStazioneCorrente(codiceStazioneParam: String?, datiiStazioni: List<DatoStazione>) {

        if (this.codiceStazione == null || this.codiceStazione != codiceStazioneParam) {
            this.codiceStazione = codiceStazioneParam

            filtraDatiStazioni(datiiStazioni)

            // pulisco tutti i dati delle sezioni
            var i = 0
            val nsize = sezioni.size()
            while (i < nsize) {
                val sezione = sezioni.valueAt(i)
                sezione.datiStazione = datiStazione
                sezione.clear()
                i++
            }
        }
    }

    /**
     * Visualizza o nasconde il contenuto della sezione gestita dalla risorsa passata come parametro.
     *
     * @param resId id risorsa
     */
    fun toggleContent(resId: Int) {

        val sezione = sezioni.get(resId)
        if (sezione != null) {
            if (datiStazione == null || datiStazione!!.isEmpty()) {
                val toast = Toast.makeText(
                    view.context,
                    "Nessun dato al momento disponibile per la stazione.",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            } else {
                sezione.toggleContent()
            }
        }
    }

}
