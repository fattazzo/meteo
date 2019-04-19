/*
 * Project: meteo
 * File: StazioniFragment.kt
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

package com.gmail.fattazzo.meteo.fragment.stazioni.meteo

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.AnagraficaStazioniMeteoActivity_
import com.gmail.fattazzo.meteo.db.StazioneMeteo
import com.gmail.fattazzo.meteo.domain.xml.stazioni.meteo.datistazione.DatiStazione
import com.gmail.fattazzo.meteo.fragment.BaseFragment
import com.gmail.fattazzo.meteo.fragment.home.HomeFragment_
import com.gmail.fattazzo.meteo.fragment.stazioni.meteo.rilevazioni.StazioneMeteoSpinnerAdapter
import com.gmail.fattazzo.meteo.fragment.stazioni.meteo.rilevazioni.TipoDatoStazione
import com.gmail.fattazzo.meteo.fragment.stazioni.meteo.rilevazioni.dati.IDatoStazioneFragment
import com.gmail.fattazzo.meteo.fragment.stazioni.meteo.rilevazioni.dati.grafico.GraficoDatiStazioneFragment_
import com.gmail.fattazzo.meteo.fragment.stazioni.meteo.rilevazioni.dati.tabella.TabellaDatiStazioneFragment_
import com.gmail.fattazzo.meteo.manager.MeteoManager
import com.gmail.fattazzo.meteo.utils.FragmentUtils
import org.androidannotations.annotations.*
import java.util.*

/**
 * @author fattazzo
 *
 *
 * date: 17/lug/2014
 */
//@EFragment(R.layout.fragment_stazioni)
@OptionsMenu(R.menu.stazioni_meteo_menu)
@EFragment(R.layout.fragment_dati_stazioni)
open class StazioniFragment : BaseFragment(), RadioGroup.OnCheckedChangeListener {

    @Bean
    internal lateinit var meteoManager: MeteoManager

    @ViewById
    internal lateinit var headerLayout: ConstraintLayout

    @ViewById
    internal lateinit var tipoDatoLayout: ConstraintLayout

    @ViewById
    internal lateinit var titleTV: TextView

    @ViewById
    @JvmField
    internal var stazioneSpinner: Spinner? = null
    @ViewById
    @JvmField
    internal var tipoDatoSpinner: Spinner? = null

    @ViewById
    internal lateinit var graficoRadioButton: RadioButton

    @ViewById
    internal lateinit var tipoVisualizzazioneRadioGroup: RadioGroup

    private var datiStazione: DatiStazione = DatiStazione()

    private var stazioniMeteo: List<StazioneMeteo>? = null

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        visualizzaDatiStazione()
    }

    @AfterViews
    internal fun initViews() {
        caricaStazioniMeteo()

        tipoVisualizzazioneRadioGroup.setOnCheckedChangeListener(this)
    }

    @ItemSelect
    fun stazioneSpinnerItemSelected(selected: Boolean, position: Int) {
        visualizzaDatiStazione()
    }

    @ItemSelect
    fun tipoDatoSpinnerItemSelected(selected: Boolean, position: Int) {
        visualizzaDatiStazione()
    }

    @Background
    internal open fun caricaStazioniMeteo() {
        stazioniMeteo = StazioneMeteo.laodAll(false)
        if (stazioniMeteo!!.isEmpty()) {
            meteoManager.downloadAnagraficaStazioni()
            stazioniMeteo = StazioneMeteo.laodAll(false)
        }
        updateStazioniUI()
    }

    @UiThread
    internal open fun updateStazioniUI() {
        val stazioneMeteo = StazioneMeteo()
        stazioneMeteo.codice = null
        stazioneMeteo.nome = ""

        val stazioniSpinner = ArrayList<StazioneMeteo>()
        stazioniSpinner.add(stazioneMeteo)
        stazioniSpinner.addAll(stazioniMeteo!!)

        if (stazioneSpinner != null) {
            val dataAdapter = StazioneMeteoSpinnerAdapter(context!!, stazioniSpinner)
            stazioneSpinner!!.adapter = dataAdapter
        }
    }

    /**
     * Visualizza i dati della stazione selezionata.
     */
    private fun visualizzaDatiStazione() {
        titleTV.text = ""

        // no faccio niente finchè non viene scelta una stazione meteo
        if (stazioneSpinner != null && stazioneSpinner!!.selectedItemPosition > 0) {

            openIndeterminateDialog("Caricamento dati in corso...")

            val stazioneMeteo = (stazioneSpinner!!.adapter as StazioneMeteoSpinnerAdapter).objects[stazioneSpinner!!.selectedItemPosition]
            titleTV.text = String.format("%s  - %s", stazioneMeteo.nome, tipoDatoSpinner!!.selectedItem.toString())

            if (stazioneMeteo.codice == datiStazione.codiceStazione) {
                updateDatiStazioneUI()
            } else {
                caricaDatiStazione(stazioneMeteo.codice)
            }
        }
    }

    @Background
    internal open fun caricaDatiStazione(codiceStazione: String?) {
        this.datiStazione = meteoManager.caricaDatiStazione(codiceStazione!!)
        updateDatiStazioneUI()
    }

    @UiThread
    open fun updateDatiStazioneUI() {

        val fragment: IDatoStazioneFragment = if (graficoRadioButton.isChecked)
            GraficoDatiStazioneFragment_.builder().build() else TabellaDatiStazioneFragment_.builder().build()

        val tipoDatoStazione = TipoDatoStazione.values()[tipoDatoSpinner!!.selectedItemPosition]
        val dati = when (tipoDatoStazione) {
            TipoDatoStazione.TEMPERATURA -> datiStazione.temperature.orEmpty()
            TipoDatoStazione.PRECIPITAZIONE -> datiStazione.precipitazioni.orEmpty()
            TipoDatoStazione.UMIDITA -> datiStazione.umidita.orEmpty()
            TipoDatoStazione.RADIAZIONI -> datiStazione.radiazioni.orEmpty()
            TipoDatoStazione.VENTO -> datiStazione.venti.orEmpty()
            TipoDatoStazione.NEVE -> datiStazione.neve.orEmpty()
        }
        fragment.dati = dati

        FragmentUtils.replace(activity as AppCompatActivity, fragment as Fragment, R.id.datoContainer)

        closeIndeterminateDialog()
    }

    @Click
    fun anagraficaStazioniButtonClicked() {
        AnagraficaStazioniMeteoActivity_.intent(context).start()
    }

    @OptionsItem
    fun toggleFullscreenAction() {
        headerLayout.visibility = if (headerLayout.visibility == View.GONE) View.VISIBLE else View.GONE
        tipoDatoLayout.visibility = if (tipoDatoLayout.visibility == View.GONE) View.VISIBLE else View.GONE
        tipoVisualizzazioneRadioGroup.visibility = if (tipoVisualizzazioneRadioGroup.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    override fun backPressed(): Boolean {
        FragmentUtils.replace(activity as AppCompatActivity, HomeFragment_.builder().build())
        return true
    }

    override fun getTitleResId(): Int = R.string.nav_stazioni_meteo
}
