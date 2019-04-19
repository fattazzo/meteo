/*
 * Project: meteo
 * File: PrevisioneLocaleGridRemoteViewsFactory.kt
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

package com.gmail.fattazzo.meteo.widget.providers.previsione.locale

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.SplashActivity_
import com.gmail.fattazzo.meteo.domain.json.previsione.Giorno
import com.gmail.fattazzo.meteo.manager.MeteoManager
import com.gmail.fattazzo.meteo.manager.MeteoManager_
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager_
import com.gmail.fattazzo.meteo.preferences.widget.bollettino.BollettinoWidgetsSettingsManager
import com.gmail.fattazzo.meteo.utils.LoadBitmapTask
import com.gmail.fattazzo.meteo.widget.providers.previsione.LoadPrevisioneLocalitaTask
import com.gmail.fattazzo.meteo.widget.providers.previsione.fascia.corrente.FasceListRemoteViewsFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author fattazzo
 *
 *
 * date: 27/10/17
 */
class PrevisioneLocaleGridRemoteViewsFactory(private val mContext: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<Giorno>()

    private val preferencesManager: BollettinoWidgetsSettingsManager

    private var data: String? = null
    private var localita: String = ""

    var meteoManager: MeteoManager? = null
    private var applicationPreferencesManager: ApplicationPreferencesManager? = null

    init {
        meteoManager = MeteoManager_.getInstance_(mContext)
        applicationPreferencesManager = ApplicationPreferencesManager_.getInstance_(mContext)

        data = intent.getStringExtra(FasceListRemoteViewsFactory.EXTRA_DATA)

        val previsione = try {
            LoadPrevisioneLocalitaTask(meteoManager!!, applicationPreferencesManager!!).execute().get()
        } catch (e: Exception) {
            null
        }

        localita = previsione?.previsioni.orEmpty().firstOrNull()?.localita.orEmpty()

        mWidgetItems.addAll(previsione?.previsioni.orEmpty().firstOrNull()?.giorni.orEmpty())

        preferencesManager = BollettinoWidgetsSettingsManager(mContext)
    }

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val giorno = mWidgetItems[position]

        val rv = RemoteViews(mContext.packageName, R.layout.widget_previsione_locale_item)
        try {
            rv.setBitmap(R.id.iconaImageView, "setImageBitmap", LoadBitmapTask().execute(giorno.icona).get())
        } catch (e: Exception) {
            rv.setBitmap(R.id.iconaImageView, "setImageBitmap", null)
        }

        data.let {
            if (position == 0) rv.setTextViewText(R.id.oraTV, data) else rv.setTextViewText(R.id.oraTV, "")
        }
        rv.setTextColor(R.id.oraTV, preferencesManager.textColor)

        val dateFormatter = SimpleDateFormat("EEEE dd ", Locale.ITALIAN)
        rv.setTextViewText(R.id.giornoTV, dateFormatter.format(giorno.data).capitalize() + localita)
        rv.setTextColor(R.id.giornoTV, preferencesManager.textColor)

        rv.setTextViewText(R.id.testoTV, giorno.testo.orEmpty().capitalize())
        rv.setTextColor(R.id.testoTV, preferencesManager.textColor)

        val fillInIntent = Intent(mContext, SplashActivity_::class.java)
        rv.setOnClickFillInIntent(R.id.widget_background_layout, fillInIntent)

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null


    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = true

    companion object {
        const val EXTRA_DATA = "extraData"
    }

}