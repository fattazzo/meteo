/*
 * Project: meteo
 * File: SplashActivity.kt
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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.fragment.home.PrevisioneCorrente
import com.gmail.fattazzo.meteo.manager.MeteoManager
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import com.gmail.fattazzo.meteo.utils.Utils
import org.androidannotations.annotations.*


/**
 * @author fattazzo
 *
 *
 * date: 09/giu/2014
 */
@SuppressLint("DefaultLocale")
@EActivity(R.layout.activity_splash)
open class SplashActivity : Activity() {

    @Bean
    lateinit var preferencesManager: ApplicationPreferencesManager

    @Bean
    internal lateinit var meteoManager: MeteoManager

    @Bean
    internal lateinit var utils: Utils

    @ViewById
    internal lateinit var appTitleTV: TextView

    @ViewById
    internal lateinit var gifImageView: ImageView

    private var previsioneLoaded = false


    @AfterViews
    fun initViews() {
        Glide.with(this)
                .load(if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) R.drawable.splash_v else R.drawable.splash_h)
                .into(gifImageView)

        val myTypeface = Typeface.createFromAsset(assets, "fonts/anarchistic.ttf")
        appTitleTV.typeface = myTypeface
        appTitleTV.setTextColor(Color.BLACK)
        appTitleTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.font_size_big))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            // initialize default settings for all preferences
            PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

            Handler().postDelayed({ loadPrevisione() }, 2000)
        }

        // Update all widgets
        //sendBroadcast(Intent().apply {
        //    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        //})
    }

    @Background
    open fun loadPrevisione() {
        val localita = preferencesManager.getLocalita()
        PrevisioneCorrente.setPrevisione(meteoManager.caricaPrevisioneLocalita(localita), localita)
        previsioneLoaded = true

        startMainActivity()
    }

    @UiThread
    open fun startMainActivity() {
        MainActivity_.intent(this).start()
        this@SplashActivity.finish()
    }
}
