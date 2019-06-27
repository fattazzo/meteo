/*
 * Project: meteo
 * File: BullettinPrevisioneLocalitaHorizontalProvider4x1.kt
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

/**
 *
 */
package com.gmail.fattazzo.meteo.widget.providers.previsione.locale.horizontal

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.domain.json.previsione.Giorno
import com.gmail.fattazzo.meteo.domain.json.previsione.PrevisioneLocalita
import com.gmail.fattazzo.meteo.manager.MeteoManager_
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager_
import com.gmail.fattazzo.meteo.preferences.widget.bollettino.BollettinoWidgetsSettingsManager
import com.gmail.fattazzo.meteo.utils.VectorUtils
import com.gmail.fattazzo.meteo.utils.icons.WeatherIconsFactory
import com.gmail.fattazzo.meteo.widget.providers.MeteoAppWidgetProvider
import com.gmail.fattazzo.meteo.widget.providers.previsione.LoadPrevisioneLocalitaTask
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author Fattazzo
 *
 *
 * Date 04/ago/2014
 */
open class BullettinPrevisioneLocalitaHorizontalProvider4x1 : MeteoAppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidget = ComponentName(context.packageName, BullettinPrevisioneLocalitaHorizontalProvider4x1::class.java.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)

        onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        Log.d("Widget", "onUpdate: BullettinPrevisioneLocalitaHorizontalProvider4x1")

        var previsione: PrevisioneLocalita? = null
        if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
            previsione = try {
                LoadPrevisioneLocalitaTask(MeteoManager_.getInstance_(context), ApplicationPreferencesManager_.getInstance_(context)).execute().get()
            } catch (e: Exception) {
                null
            }
        }

        if(previsione == null) {
            return
        }

        appWidgetIds?.forEach { appWidgetId ->

            val widgetSettingsManager = BollettinoWidgetsSettingsManager(context!!)

            val textColor = widgetSettingsManager.textColor

            val remoteViews = RemoteViews(context.packageName, R.layout.widget_prevision_horizontal_4x1)
            remoteViews.setViewVisibility(R.id.errorPrevisionHorizontalTV, if (previsione != null) View.GONE else View.VISIBLE)
            remoteViews.setTextColor(R.id.errorPrevisionHorizontalTV, textColor)
            updateBackground(remoteViews,widgetSettingsManager.background)
            registerOpenAppIntent(context,remoteViews,R.id.errorPrevisionHorizontalTV)

            remoteViews.setImageViewBitmap(R.id.widget_sync, VectorUtils.vectorToBitmap(context, R.drawable.sync))

            val giorni = previsione.previsioni?.firstOrNull()?.giorni.orEmpty()

            remoteViews.setViewVisibility(R.id.prev1, View.GONE)
            remoteViews.setViewVisibility(R.id.prev2, View.GONE)
            remoteViews.setViewVisibility(R.id.prev3, View.GONE)
            remoteViews.setViewVisibility(R.id.prev4, View.GONE)

            if (giorni.isNotEmpty()) {
                fillGiorno(giorni[0], remoteViews, R.id.prev1, R.id.giorno1TV, R.id.temp1TV, R.id.icona1Image, textColor, context, appWidgetId)
            }
            if (giorni.size > 1) {
                fillGiorno(giorni[1], remoteViews, R.id.prev2, R.id.giorno2TV, R.id.temp2TV, R.id.icona2Image, textColor, context, appWidgetId)
            }
            if (giorni.size > 2) {
                fillGiorno(giorni[2], remoteViews, R.id.prev3, R.id.giorno3TV, R.id.temp3TV, R.id.icona3Image, textColor, context, appWidgetId)
            }
            if (giorni.size > 3) {
                fillGiorno(giorni[3], remoteViews, R.id.prev4, R.id.giorno4TV, R.id.temp4TV, R.id.icona4Image, textColor, context, appWidgetId)
            }

            registerRefreshIntent(context, remoteViews, appWidgetId)

            appWidgetManager?.updateAppWidget(appWidgetId, remoteViews)
        }

    }

    private fun fillGiorno(giorno: Giorno, remoteViews: RemoteViews, layoutId: Int, giornoTV: Int, temperaturaTV: Int, iconaImage: Int, textColor: Int, context: Context, appWidgetId: Int) {
        remoteViews.setViewVisibility(layoutId, View.VISIBLE)

        if (giorno.data != null) {
            remoteViews.setTextViewText(giornoTV, DAY_FORMAT.format(giorno.data).capitalize())
            remoteViews.setTextColor(giornoTV, textColor)
        }

        var temperatura = ""
        if (giorno.temperaturaMin != null) {
            temperatura += giorno.temperaturaMin
        }
        temperatura += "/"
        if (giorno.temperaturaMax != null) {
            temperatura += giorno.temperaturaMax
        }
        temperatura += " °C"
        remoteViews.setTextViewText(temperaturaTV, temperatura)
        remoteViews.setTextColor(temperaturaTV, textColor)

        remoteViews.setInt(iconaImage, "setColorFilter", 0)
        try {
            val iconsRetriever = WeatherIconsFactory.getIconsRetriever(context)
            val icona = iconsRetriever.getIcon(giorno.idIcona)

            if (icona == null) {
                val appWidgetTarget = AppWidgetTarget(context, iconaImage, remoteViews, appWidgetId)
                Glide.with(context.applicationContext).asBitmap().load(giorno.icona).into(appWidgetTarget)
            } else {
                remoteViews.setImageViewResource(iconaImage, icona)
                if (iconsRetriever.overrideColorForWidget()) {
                    remoteViews.setInt(iconaImage, "setColorFilter", textColor)
                }
            }
        } catch (e: Exception) {
            remoteViews.setBitmap(iconaImage, "setImageBitmap", null)
        }
        registerOpenAppIntent(context, remoteViews, iconaImage)
    }

    companion object {

        val DAY_FORMAT = SimpleDateFormat("EEEE", Locale.ITALIAN)
    }
}
