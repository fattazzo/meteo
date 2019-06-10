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

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.manager.MeteoManager
import com.gmail.fattazzo.meteo.manager.MeteoManager_
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager_
import com.gmail.fattazzo.meteo.preferences.widget.bollettino.BollettinoWidgetsSettingsManager
import com.gmail.fattazzo.meteo.utils.icons.WeatherIconsFactory
import com.gmail.fattazzo.meteo.widget.providers.previsione.LoadPrevisioneLocalitaTask
import com.gmail.fattazzo.meteo.widget.providers.previsione.locale.PrevisioneLocaleGridRemoteViewsFactory
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author fattazzo
 *
 *
 * date: 27/10/17
 */
class FasceListRemoteViewsFactory(private val mContext: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = mutableListOf<FasciaWidget>()

    private val preferencesManager: BollettinoWidgetsSettingsManager

    private var meteoManager: MeteoManager? = null

    private var applicationPreferencesManager: ApplicationPreferencesManager? = null

    init {
        meteoManager = MeteoManager_.getInstance_(mContext)
        applicationPreferencesManager = ApplicationPreferencesManager_.getInstance_(mContext)
        preferencesManager = BollettinoWidgetsSettingsManager(mContext)
    }

    override fun onCreate() {
    }

    override fun onDataSetChanged() {

        val previsione = try {
            LoadPrevisioneLocalitaTask(meteoManager!!, applicationPreferencesManager!!).execute().get()
        } catch (e: Exception) {
            null
        }

        mWidgetItems.clear()
        for (giorno in previsione?.previsioni.orEmpty().firstOrNull()?.giorni.orEmpty()) {
            val localita = previsione?.previsioni.orEmpty().firstOrNull()?.localita.orEmpty()
            giorno.fasce.orEmpty().mapTo(mWidgetItems) { FasciaWidget(localita, giorno.data, it, giorno.temperaturaMin, giorno.temperaturaMax) }
        }
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val fascia = mWidgetItems[position].fascia

        val rv = RemoteViews(mContext.packageName, R.layout.widget_fasce_item)
        rv.setInt(R.id.iconaImageView, "setColorFilter", 0)
        try {
            val iconsRetriever = WeatherIconsFactory.getIconsRetriever(mContext)
            val icona = iconsRetriever.getIcon(fascia.getIdIcona())

            if (icona == null) {
                val appWidgetTarget = AppWidgetTarget(mContext, R.id.iconaImageView, rv, ComponentName(mContext.applicationContext, PrevisioneLocaleGridRemoteViewsFactory::class.java))
                Glide.with(mContext.applicationContext).asBitmap().load(fascia.icona).into(appWidgetTarget)
            } else {
                rv.setImageViewResource(R.id.iconaImageView, icona)
                if (iconsRetriever.overrideColorForWidget()) {
                    rv.setInt(R.id.iconaImageView, "setColorFilter", preferencesManager.textColor)
                }
            }
        } catch (e: Exception) {
            rv.setBitmap(R.id.iconaImageView, "setImageBitmap", null)
        }

        val dateFormatter = SimpleDateFormat("EEEE dd ", Locale.ITALIAN)
        rv.setTextViewText(R.id.giornoTV, dateFormatter.format(mWidgetItems[position].data).capitalize() + mWidgetItems[position].localita + "(${fascia.ore})")
        rv.setTextColor(R.id.giornoTV, preferencesManager.textColor)

        //rv.setTextViewText(R.id.descrizioneTV, "Ore " + fascia.ore)
        //rv.setTextColor(R.id.descrizioneTV, preferencesManager.textColor)

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

        val fillInIntent = Intent()
        rv.setOnClickFillInIntent(R.id.widget_background_layout, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null


    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

}