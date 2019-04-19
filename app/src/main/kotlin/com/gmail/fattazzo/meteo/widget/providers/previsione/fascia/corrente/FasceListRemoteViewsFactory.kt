/*
 * Project: meteo
 * File: FasceListRemoteViewsFactory.kt
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

package com.gmail.fattazzo.meteo.widget.providers.previsione.fascia.corrente

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.SplashActivity_
import com.gmail.fattazzo.meteo.manager.MeteoManager
import com.gmail.fattazzo.meteo.manager.MeteoManager_
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager_
import com.gmail.fattazzo.meteo.preferences.widget.bollettino.BollettinoWidgetsSettingsManager
import com.gmail.fattazzo.meteo.utils.LoadBitmapTask
import com.gmail.fattazzo.meteo.widget.providers.previsione.LoadPrevisioneLocalitaTask
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author fattazzo
 *
 *
 * date: 27/10/17
 */
class FasceListRemoteViewsFactory(private val mContext: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<FasciaWidget>()

    private var data: String? = null

    private val preferencesManager: BollettinoWidgetsSettingsManager

    var meteoManager: MeteoManager? = null
    private var applicationPreferencesManager: ApplicationPreferencesManager? = null

    init {
        meteoManager = MeteoManager_.getInstance_(mContext)
        applicationPreferencesManager = ApplicationPreferencesManager_.getInstance_(mContext)

        data = intent.getStringExtra(EXTRA_DATA)

        val previsione = try {
            LoadPrevisioneLocalitaTask(meteoManager!!, applicationPreferencesManager!!).execute().get()
        } catch (e: Exception) {
            null
        }

        for (giorno in previsione?.previsioni.orEmpty().firstOrNull()?.giorni.orEmpty()) {
            val localita = previsione?.previsioni.orEmpty().firstOrNull()?.localita.orEmpty()
            giorno.fasce.orEmpty().mapTo(mWidgetItems) { FasciaWidget(localita, giorno.data, it, giorno.temperaturaMin, giorno.temperaturaMax) }
        }

        preferencesManager = BollettinoWidgetsSettingsManager(mContext)
    }

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        println("onDataSetChanged")
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val fascia = mWidgetItems[position].fascia

        val rv = RemoteViews(mContext.packageName, R.layout.widget_fasce_item)
        try {
            rv.setBitmap(R.id.iconaImageView, "setImageBitmap", LoadBitmapTask().execute(fascia.icona).get())
        } catch (e: Exception) {
            rv.setBitmap(R.id.iconaImageView, "setImageBitmap", null)
        }

        mWidgetItems[position].data.let {
            if (position == 0) rv.setTextViewText(R.id.syncDateTV, data) else rv.setTextViewText(R.id.syncDateTV, "")
        }
        rv.setTextColor(R.id.syncDateTV, preferencesManager.textColor)

        val dateFormatter = SimpleDateFormat("EEEE dd ", Locale.ITALIAN)
        rv.setTextViewText(R.id.giornoTV, dateFormatter.format(mWidgetItems[position].data).capitalize() + mWidgetItems[position].localita)
        rv.setTextColor(R.id.giornoTV, preferencesManager.textColor)

        rv.setTextViewText(R.id.descrizioneTV, fascia.descrizione.orEmpty().capitalize() + " ore " + fascia.ore)
        rv.setTextColor(R.id.descrizioneTV, preferencesManager.textColor)

        rv.setTextViewText(R.id.temporaliTV, fascia.descProbabilitaTemporali.orEmpty().capitalize())
        rv.setTextColor(R.id.temporaliTV, preferencesManager.textColor)

        rv.setTextViewText(R.id.precipitazioniTV, fascia.descProbabilitaPrecipitazioni.orEmpty().capitalize())
        rv.setTextColor(R.id.precipitazioniTV, preferencesManager.textColor)

        rv.setTextViewText(R.id.ventoTV, fascia.descVentoIntensitaValle.orEmpty().capitalize())
        rv.setTextColor(R.id.ventoTV, preferencesManager.textColor)

        rv.setViewVisibility(R.id.tempMinTV, View.GONE)
        rv.setTextColor(R.id.tempMinTV, preferencesManager.textColor)
        if (mWidgetItems[position].temperaturaMin != null) {
            rv.setViewVisibility(R.id.tempMinTV, View.VISIBLE)
            rv.setTextViewText(R.id.tempMinTV, mWidgetItems[position].temperaturaMin.toString() + " °C")
        }

        rv.setViewVisibility(R.id.tempMaxTV, View.GONE)
        rv.setTextColor(R.id.tempMaxTV, preferencesManager.textColor)
        if (mWidgetItems[position].temperaturaMax != null) {
            rv.setViewVisibility(R.id.tempMaxTV, View.VISIBLE)
            rv.setTextViewText(R.id.tempMaxTV, mWidgetItems[position].temperaturaMax.toString() + " °C")
        }

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