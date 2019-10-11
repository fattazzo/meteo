/*
 * Project: meteo
 * File: MainActivity.kt
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

package com.gmail.fattazzo.meteo.activity.main

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.BaseActivity
import com.gmail.fattazzo.meteo.activity.about.AboutActivityIntentBuilder
import com.gmail.fattazzo.meteo.activity.bollettino.probabilistico.BollettinoProbabilisticoActivity
import com.gmail.fattazzo.meteo.activity.news.NewsActivity
import com.gmail.fattazzo.meteo.activity.radar.RadarActivity
import com.gmail.fattazzo.meteo.activity.settings.SettingsActivity
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.StazioniMeteoActivity
import com.gmail.fattazzo.meteo.activity.stazioni.valanghe.StazioniValangheActivity
import com.gmail.fattazzo.meteo.activity.webcam.WebcamActivity
import com.gmail.fattazzo.meteo.app.MeteoApplication
import com.gmail.fattazzo.meteo.app.module.viewmodel.DaggerViewModelFactory
import com.gmail.fattazzo.meteo.data.VersioniService
import com.gmail.fattazzo.meteo.data.db.entities.Localita
import com.gmail.fattazzo.meteo.databinding.ActivityMainBinding
import com.gmail.fattazzo.meteo.preferences.PreferencesService
import com.gmail.fattazzo.meteo.utils.AppRater
import com.gmail.fattazzo.meteo.utils.dialog.DialogBuilder
import com.gmail.fattazzo.meteo.utils.dialog.DialogType
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * @author fattazzo
 *
 *
 * date: 09/giu/2014
 */
class MainActivity : BaseActivity<ActivityMainBinding>(),
    NavigationView.OnNavigationItemSelectedListener, MediaPlayer.OnCompletionListener {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    @Inject
    lateinit var preferencesService: PreferencesService

    @Inject
    lateinit var versioniService: VersioniService

    lateinit var viewModel: MainViewModel

    private var audioOptionMenu: MenuItem? = null

    override fun getLayoutResID(): Int = R.layout.activity_main

    companion object {
        var mediaPlayer: MediaPlayer = MediaPlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MeteoApplication).appComponent.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding.model = viewModel

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            0,
            0
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            AppRater.rate(this)
        }

        initLocalita()

        binding.contentMain.containerLayout.setHasFixedSize(true)
        binding.contentMain.containerLayout.adapter =
            PrevisioneAdapter(mutableListOf(), this)
        val columns = this.resources.getInteger(R.integer.previsione_columns)
        val layoutManager = StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL)
        binding.contentMain.containerLayout.layoutManager = layoutManager
        viewModel.previsioneLocalita.observe(this, androidx.lifecycle.Observer {
            val data = mutableListOf<Any?>()
            if (it != null) data.add(it)
            if (it?.previsione.orEmpty().isNotEmpty()) {
                data.addAll(it?.previsione?.get(0)?.giorni.orEmpty())
            }

            (binding.contentMain.containerLayout.adapter as PrevisioneAdapter).data = data
            binding.contentMain.containerLayout.adapter!!.notifyDataSetChanged()
            binding.contentMain.containerLayout.scheduleLayoutAnimation()
        })

        viewModel.caricaLocalita(false)

        mediaPlayer.reset()
        try {
            mediaPlayer.setDataSource(Config.PREVISIONE_AUDIO_URL)
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            // Error on prepare mediaplayer
        }
        mediaPlayer.setOnCompletionListener(this)
    }

    fun impostaLocalitaPreferita(view: View) {
        val locSel = binding.contentMain.localitaSpinner.selectedItem as Localita?
        if (locSel?.nome.orEmpty().isNotBlank()) {
            preferencesService.setNomeLocalitaPreferita(locSel!!.nome!!)
            Toast.makeText(
                this,
                locSel.nome!! + " impostata come località preferita",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun initLocalita() {

        viewModel.localita.observe(this, androidx.lifecycle.Observer {
            binding.contentMain.localitaSpinner.adapter = ArrayAdapter<Localita>(
                binding.contentMain.localitaSpinner.context,
                R.layout.item_localita_previsione,
                it.orEmpty()
            )
        })

        viewModel.localitaSelezionata.observe(this, androidx.lifecycle.Observer {
            var locSelIdx: Int = -1
            viewModel.localita.value.orEmpty().forEachIndexed { index, localita ->
                if (localita.nome == it) {
                    locSelIdx = index
                }
            }

            if (locSelIdx != -1) {
                binding.contentMain.localitaSpinner.setSelection(locSelIdx, false)
            }
        })

        binding.contentMain.localitaSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val localita = binding.contentMain.localitaSpinner.selectedItem as Localita?
                    localita?.let {
                        viewModel.localitaSelezionata.postValue(it.nome)
                    }
                }
            }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val intent: Intent? = when (menuItem.itemId) {
            R.id.nav_boll_prob -> Intent(this, BollettinoProbabilisticoActivity::class.java)
            R.id.nav_news_e_allerte -> Intent(this, NewsActivity::class.java)
            R.id.nav_stazioni_meteo -> Intent(this, StazioniMeteoActivity::class.java)
            R.id.nav_stazioni_neve -> Intent(this, StazioniValangheActivity::class.java)
            R.id.nav_radar -> Intent(this, RadarActivity::class.java)
            R.id.nav_webcam -> Intent(this, WebcamActivity::class.java)
            R.id.nav_preferences -> Intent(this, SettingsActivity::class.java)
            R.id.nav_version_info -> {
                versioniService.showVersionInfo(this)
                null
            }
            R.id.nav_about -> AboutActivityIntentBuilder.build(this)
            else -> null
        }

        intent?.let { startActivity(it) }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        if (versioniService.checkShowVersionChangelog()) {
            versioniService.showVersionInfo(this)
        }
    }

    override fun onBackPressed() {
        DialogBuilder(this, DialogType.BUTTONS)
            .apply {
                titleResId = R.string.app_name
                messageResId = R.string.exit_message

                positiveText = R.string.yes
                positiveAction = object : DialogBuilder.OnClickListener {
                    override fun onClick(dialog: Dialog?) {
                        dialog?.dismiss()
                        this@MainActivity.finish()
                    }
                }

                negativeText = R.string.no
            }
            .build()
            .show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean {
        audioOptionMenu = menu.findItem(R.id.previsioneAudioAction)
        if (mediaPlayer.isPlaying) {
            audioOptionMenu?.setIcon(R.drawable.stop_black)
        } else {
            audioOptionMenu?.setIcon(R.drawable.play_black)
        }
        return super.onPreparePanel(featureId, view, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refreshAction -> {
                viewModel.caricaPrevisione(true)
                true
            }
            R.id.previsioneAudioAction -> {
                previsioneAudioToggle()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        audioOptionMenu?.setIcon(R.drawable.play_black)
    }

    private fun previsioneAudioToggle() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            audioOptionMenu?.setIcon(R.drawable.play_black)
        } else {
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
            audioOptionMenu?.setIcon(R.drawable.stop_black)
        }

    }
}
