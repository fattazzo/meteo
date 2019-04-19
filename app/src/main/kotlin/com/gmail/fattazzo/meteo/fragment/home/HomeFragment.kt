/*
 * Project: meteo
 * File: HomeFragment.kt
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

package com.gmail.fattazzo.meteo.fragment.home

import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.db.Localita
import com.gmail.fattazzo.meteo.fragment.BaseFragment
import com.gmail.fattazzo.meteo.manager.MeteoManager
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import com.gmail.fattazzo.meteo.utils.dialog.DialogBuilder
import com.gmail.fattazzo.meteo.utils.dialog.DialogType
import org.androidannotations.annotations.*
import java.util.concurrent.TimeUnit


/**
 * @author fattazzo
 *
 *
 * date: 09/giu/2014
 */
@OptionsMenu(R.menu.home_menu)
@EFragment(R.layout.fragment_home)
open class HomeFragment : BaseFragment(), MediaPlayer.OnCompletionListener {

    @ViewById
    internal lateinit var containerLayout: RecyclerView

    @ViewById
    internal lateinit var localitaSpinner: Spinner

    @Bean
    internal lateinit var meteoManager: MeteoManager

    @Bean
    internal lateinit var applicationPreferencesManager: ApplicationPreferencesManager

    private var localita: Localita? = null

    private var localitaDB: MutableList<Localita>? = null

    override fun getTitleResId(): Int = R.string.nav_home

    private var audioOptionMenu: MenuItem? = null

    init {
        mediaPlayer.reset()
        try {
            mediaPlayer.setDataSource(com.gmail.fattazzo.meteo.Config.PREVISIONE_AUDIO_URL)
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            // Error on prepare mediaplayer
        }
    }

    @AfterViews
    fun initViews() {
        caricaLocalita()

        mediaPlayer.setOnCompletionListener(this)
    }

    @UiThread
    open fun bindView() {
        try {
            containerLayout.adapter = PrevisioneAdapter(mutableListOf(), context!!)
            val prevloc: String = if (localita?.nome.orEmpty().isNotBlank()) localita!!.nome!! else applicationPreferencesManager.getLocalita()

            localitaSpinner.adapter = ArrayAdapter<Localita>(context, R.layout.item_localita_previsione, localitaDB)
            var locSelIdx: Int = -1
            var idx = 0
            for (loc in localitaDB.orEmpty()) {
                if (loc.nome.orEmpty() == prevloc) {
                    locSelIdx = idx
                    break
                }
                idx++
            }
            if (locSelIdx != -1) {
                localita = localitaDB!![locSelIdx]
                localitaSpinner.setSelection(locSelIdx, false)
            }

            val data = mutableListOf<Any?>(PrevisioneCorrente.previsioneLocalita)
            if (PrevisioneCorrente.previsioneLocalita?.previsioni.orEmpty().isNotEmpty()) {
                data.addAll(PrevisioneCorrente.previsioneLocalita?.previsioni?.get(0)?.giorni.orEmpty())
            }

            val previsioneAdapter = PrevisioneAdapter(data, context!!)

            val columns = context!!.resources.getInteger(R.integer.previsione_columns)
            val layoutManager = StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL)

            containerLayout.layoutManager = layoutManager
            containerLayout.adapter = previsioneAdapter
            previsioneAdapter.notifyDataSetChanged()
            containerLayout.scheduleLayoutAnimation()
        } catch (e: Exception) {
            PrevisioneCorrente.clear()
        }
    }

    @Background
    open fun downloadPrevisione(forceDownloadLocalita: Boolean, localitaNome: String) {
        openIndeterminateDialog(getString(R.string.forecast_loading_dialog))
        try {
            if (forceDownloadLocalita) {
                localitaDB = meteoManager.caricaLocalita(forceDownloadLocalita)
            }
            if (PrevisioneCorrente.previsioneLocalita == null) {
                PrevisioneCorrente.setPrevisione(meteoManager.caricaPrevisioneLocalita(localitaNome), localitaNome)
            }
        } finally {
            closeIndeterminateDialog()
        }

        bindView()
    }

    @UiThread
    open fun caricaPrevisione(forceDownloadLocalita: Boolean = false, localitaNome: String) {
        if (PrevisioneCorrente.needToUpdate(localitaNome, TimeUnit.MINUTES, 10)) {
            downloadPrevisione(forceDownloadLocalita, localitaNome)
        } else {
            bindView()
        }
    }

    @Background
    open fun caricaLocalita(forceDownloadLocalita: Boolean = false) {
        val localitaNome: String = if (localita?.nome.orEmpty().isNotBlank()) localita!!.nome!! else applicationPreferencesManager.getLocalita()
        localitaDB = meteoManager.caricaLocalita(false)

        caricaPrevisione(forceDownloadLocalita, localitaNome)
    }

    @ItemSelect
    open fun localitaSpinnerItemSelected(selected: Boolean) {

        val locSel = localitaSpinner.selectedItem as Localita?

        if (locSel != null && locSel.nome.orEmpty() != PrevisioneCorrente.localita.orEmpty()) {
            localita = locSel
            PrevisioneCorrente.clear()
            caricaLocalita()
        }
    }

    @Click
    open fun localitaFavoritaButtonClicked() {
        val locSel = localitaSpinner.selectedItem as Localita?
        if (locSel?.nome.orEmpty().isNotBlank()) {
            applicationPreferencesManager.getPrefs().edit().putString(Config.KEY_LOCALITA, locSel!!.nome).apply()
            Toast.makeText(context, locSel.nome!! + " impostata come località preferita", Toast.LENGTH_SHORT).show()
        }
    }

    @OptionsItem
    fun refreshAction() {
        PrevisioneCorrente.clear()
        caricaLocalita(true)
    }

    @OptionsItem
    fun previsioneAudioAction() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            audioOptionMenu?.setIcon(android.R.drawable.ic_media_play)
        } else {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
            audioOptionMenu?.setIcon(R.drawable.ic_media_stop)
        }

    }

    override fun backPressed(): Boolean {
        DialogBuilder(context!!, DialogType.BUTTONS)
                .apply {
                    titleResId = R.string.app_name
                    messageResId = R.string.exit_message

                    positiveText = R.string.yes
                    positiveAction = object : DialogBuilder.OnClickListener {
                        override fun onClick(dialog: Dialog?) {
                            activity?.finish()
                        }
                    }

                    negativeText = R.string.no
                }
                .build()
                .show()

        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("Position", mediaPlayer.currentPosition)
        outState.putBoolean("isplaying", mediaPlayer.isPlaying)

        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()

        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null) {
            val position = savedInstanceState.getInt("Position", 0)
            mediaPlayer.seekTo(position)
            if (savedInstanceState.getBoolean("isplaying", false))
                Handler().post {
                    mediaPlayer.start()
                }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        audioOptionMenu = menu.findItem(R.id.previsioneAudioAction)
        if (mediaPlayer.isPlaying) {
            audioOptionMenu?.setIcon(R.drawable.ic_media_stop)
        } else {
            audioOptionMenu?.setIcon(android.R.drawable.ic_media_play)
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        audioOptionMenu?.setIcon(android.R.drawable.ic_media_play)
    }

    companion object {

        private var mediaPlayer: MediaPlayer = MediaPlayer()
    }
}
