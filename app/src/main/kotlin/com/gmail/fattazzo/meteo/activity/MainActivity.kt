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

package com.gmail.fattazzo.meteo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.gmail.fattazzo.aboutlibrary.activity.AboutActivity
import com.gmail.fattazzo.aboutlibrary.builder.AboutButtonBuilder
import com.gmail.fattazzo.aboutlibrary.builder.AboutViewBuilder
import com.gmail.fattazzo.aboutlibrary.builder.Action
import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.fragment.BaseFragment
import com.gmail.fattazzo.meteo.fragment.bollettino.probabilistico.BollettinoProbabilisticoFragment_
import com.gmail.fattazzo.meteo.fragment.home.HomeFragment_
import com.gmail.fattazzo.meteo.fragment.news.NewsFragment_
import com.gmail.fattazzo.meteo.fragment.radar.RadarFragment_
import com.gmail.fattazzo.meteo.fragment.stazioni.meteo.StazioniFragment_
import com.gmail.fattazzo.meteo.fragment.stazioni.valanghe.ValangheFragment_
import com.gmail.fattazzo.meteo.fragment.webcam.WebcamFragment_
import com.gmail.fattazzo.meteo.parser.versioni.VersioniManager
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import com.gmail.fattazzo.meteo.utils.AppRater
import com.gmail.fattazzo.meteo.utils.FragmentUtils
import com.gmail.fattazzo.meteo.utils.Utils
import com.gmail.fattazzo.meteo.utils.dialog.DialogBuilder
import com.gmail.fattazzo.meteo.utils.dialog.DialogType
import com.google.android.material.navigation.NavigationView
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById
import java.util.*


/**
 * @author fattazzo
 *
 *
 * date: 09/giu/2014
 */
@EActivity(R.layout.activity_main)
open class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Bean
    internal lateinit var preferencesManager: ApplicationPreferencesManager

    @Bean
    internal lateinit var utils: Utils

    @ViewById
    internal lateinit var nav_view: NavigationView

    @ViewById
    internal lateinit var drawer_layout: DrawerLayout

    @ViewById
    internal lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            FragmentUtils.replace(this, HomeFragment_.builder().build())

            AppRater.rate(this)
        }
    }

    override fun onResume() {
        super.onResume()
        nav_view.menu.clear()
        nav_view.inflateMenu(R.menu.activity_main_drawer)
    }

    @AfterViews
    fun initMaterial() {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, 0, 0)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_home -> FragmentUtils.replace(this, HomeFragment_.builder().build())
            R.id.nav_boll_prob -> FragmentUtils.replace(this, BollettinoProbabilisticoFragment_.builder().build())
            R.id.nav_news_e_allerte -> FragmentUtils.replace(this, NewsFragment_.builder().build())
            R.id.nav_stazioni_meteo -> FragmentUtils.replace(this, StazioniFragment_.builder().build())
            R.id.nav_stazioni_neve -> FragmentUtils.replace(this, ValangheFragment_.builder().build())
            R.id.nav_radar -> FragmentUtils.replace(this, RadarFragment_.builder().build())
            R.id.nav_webcam -> FragmentUtils.replace(this, WebcamFragment_.builder().build())
            R.id.nav_preferences -> SettingsActivity_.intent(this).start()
            R.id.nav_version_info -> VersioniManager(this).showVersionInfo()
            R.id.nav_about -> openAboutActivity()
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val versioniManager = VersioniManager(this)
        if (versioniManager.checkShowVersionChangelog()) {
            versioniManager.showVersionInfo()
        }
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager

        val fragments = fragmentManager.fragments

        for (fragment in fragments) {
            if (fragment != null) {
                var done = false
                if (fragment is BaseFragment) {
                    done = fragment.backPressed()
                }
                if (done) {
                    break
                }
            }
        }
    }

    private fun openAboutActivity() {
        val reportIssueButton = AboutButtonBuilder()
                .withText(R.string.report_error)
                .withDrawable(R.drawable.bug)
                .withFlatStyle(true)
                .withAction(object : Action {
                    override fun run(context: Context) {
                        DialogBuilder(context, DialogType.BUTTONS).apply {
                            headerIcon = R.drawable.info_white
                            message = "Per segnalare un errore o richiedere nuove funzionalità puoi contattarmi usando il modo che preferisci tra quelli presenti nella sezione 'Autore' di questa pagina."
                            positiveText = android.R.string.ok
                        }.build().show()
                    }
                })
                .withTextColor(android.R.color.holo_red_dark)

        val changelogButton = AboutButtonBuilder()
                .withText(R.string.changelog)
                .withDrawable(R.drawable.format_list)
                .withFlatStyle(true)
                .withAction(object : Action {
                    override fun run(context: Context) {
                        VersioniManager(context).showVersionChangelog()
                    }
                })

        val aboutViewBuilder = AboutViewBuilder()
                .withInfoUrl(Config.PROJECTS_INFO_URL)
                .withAppId(this.applicationContext.packageName)
                .withFlatStyleButtons(true)
                .withAdditionalAppButtons(listOf(reportIssueButton, changelogButton))
                .withLang(Locale.getDefault().language)
                .withExcludeThisAppFromProjects(true)

        val intent = Intent(this, AboutActivity::class.java)
                .apply { putExtra(AboutActivity.EXTRA_BUILDER, aboutViewBuilder) }
        startActivity(intent)
    }
}
