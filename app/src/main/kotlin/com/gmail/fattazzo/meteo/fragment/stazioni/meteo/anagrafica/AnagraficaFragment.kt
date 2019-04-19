/*
 * Project: meteo
 * File: AnagraficaFragment.kt
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

package com.gmail.fattazzo.meteo.fragment.stazioni.meteo.anagrafica

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.widget.ListView
import android.widget.Switch
import android.widget.Toast
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.db.StazioneMeteo
import com.gmail.fattazzo.meteo.fragment.BaseFragment
import com.gmail.fattazzo.meteo.manager.MeteoManager
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import com.gmail.fattazzo.meteo.utils.dialog.DialogBuilder
import com.gmail.fattazzo.meteo.utils.dialog.DialogType
import org.androidannotations.annotations.*
import java.text.DateFormat
import java.util.*


/**
 * @author fattazzo
 *
 *
 * date: 17/lug/2014
 */
@EFragment(R.layout.fragment_anagrafica_stazioni)
open class AnagraficaFragment : BaseFragment() {

    @ViewById(R.id.anag_stazioni_list)
    internal lateinit var listView: ListView

    @ViewById
    internal lateinit var stazioniTypeSwitch: Switch

    @Bean
    internal lateinit var meteoManager: MeteoManager

    @Bean
    internal lateinit var preferenceManager: ApplicationPreferencesManager

    @AfterViews
    fun afterViews() {

        loadStazioniMeteo()
    }

    /**
     * Carica le stazioni meteo.
     */
    @UiThread(propagation = UiThread.Propagation.REUSE)
    open fun loadStazioniMeteo() {
        openIndeterminateDialog("Caricamento stazioni in corso...")

        // se non ho ancora stazioni meteo nel database le carico
        if (StazioneMeteo.count() == 0) {
            downloadStazioni()
        } else {
            loadTableData()
        }
    }

    @Background
    open fun downloadStazioni() {
        meteoManager.downloadAnagraficaStazioni()

        loadTableData()
    }

    /**
     * Carica tutte le stazioni meteo nell'adapter della tabella.
     */
    @UiThread(propagation = UiThread.Propagation.REUSE)
    open fun loadTableData() {

        val stazioniMeteo = StazioneMeteo.laodAll(!stazioniTypeSwitch.isChecked)

        val adapter = StazioneMeteoListAdapter(context!!, stazioniMeteo, preferenceManager.getCodiceStazioneMeteoWidget())

        listView.adapter = adapter

        closeIndeterminateDialog()
    }

    @Click
    fun refreshButtonClicked() {
        StazioneMeteo.deleteAll()

        loadStazioniMeteo()
    }

    @Click
    fun infoButtonClicked() {
        DialogBuilder(context!!, DialogType.BUTTONS)
                .apply {
                    headerIcon = R.drawable.info_white
                    titleResId = R.string.station_widget_info_title
                    messageResId = R.string.station_widget_info_message
                    positiveText = android.R.string.ok
                }
                .build()
                .show()
    }

    @CheckedChange
    fun stazioniTypeSwitchCheckedChanged() {
        loadTableData()
    }

    @ItemLongClick
    fun anag_stazioni_listItemLongClicked(stazioneMeteo: StazioneMeteo) {
        preferenceManager.setCodiceStazioneMeteoWidget(stazioneMeteo.codice.orEmpty())

        Toast.makeText(context, context!!.getString(R.string.station_configured_for_widget).format(stazioneMeteo.codice.orEmpty()), Toast.LENGTH_SHORT).show()

        refreshButtonClicked()
    }

    @ItemClick
    fun anag_stazioni_listItemClicked(stazioneMeteo: StazioneMeteo) {

        val dialogMessage = StringBuilder()
        dialogMessage.append("Codice: ").append(stazioneMeteo.codice.orEmpty()).append("\n")
        dialogMessage.append("Nome breve: ").append(stazioneMeteo.nomeBreve.orEmpty()).append("\n")
        dialogMessage.append("Quota: ").append(stazioneMeteo.quota).append("\n")
        dialogMessage.append("Latitudine: ").append(stazioneMeteo.latitudine).append("\n")
        dialogMessage.append("Longitudine: ").append(stazioneMeteo.longitudine).append("\n")
        dialogMessage.append("Est: ").append(stazioneMeteo.est).append("\n")
        dialogMessage.append("Nord: ").append(stazioneMeteo.nord).append("\n")

        val dateFormat = DateFormat.getDateInstance()
        dialogMessage.append("Inizio rilevazioni: ").append(dateFormat.format(stazioneMeteo.startdate)).append("\n")
        stazioneMeteo.enddate?.let {
            dialogMessage.append("Fine rilevazioni: ").append(dateFormat.format(stazioneMeteo.enddate))
        }

        DialogBuilder(context!!, DialogType.BUTTONS)
                .apply {
                    title = stazioneMeteo.nome.orEmpty()
                    message = dialogMessage.toString()

                    headerIcon = R.drawable.stazione

                    positiveText = android.R.string.ok

                    negativeText = R.string.street_view
                    negativeAction = object : DialogBuilder.OnClickListener {
                        override fun onClick(dialog: Dialog?) {
                            val coordinates = String.format(Locale.ENGLISH,
                                    "google.streetview:cbll=%f,%f",
                                    stazioneMeteo.latitudine, stazioneMeteo.longitudine)
                            val gmmIntentUri = Uri.parse(coordinates)
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.`package` = "com.google.android.apps.maps"
                            startActivity(mapIntent)
                        }
                    }

                    neutralText = R.string.google_maps
                    neutralAction = object : DialogBuilder.OnClickListener {
                        override fun onClick(dialog: Dialog?) {
                            val coordinates = String.format(Locale.ENGLISH,
                                    "geo:%f,%f?q=%f,%f(%s)",
                                    stazioneMeteo.latitudine, stazioneMeteo.longitudine,
                                    stazioneMeteo.latitudine, stazioneMeteo.longitudine, stazioneMeteo.nome + " - " + stazioneMeteo.nomeBreve)
                            val gmmIntentUri = Uri.parse(coordinates)
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.`package` = "com.google.android.apps.maps"
                            startActivity(mapIntent)
                        }
                    }
                }
                .build()
                .show()
    }

    override fun getTitleResId(): Int = R.string.nav_stazioni_meteo
}