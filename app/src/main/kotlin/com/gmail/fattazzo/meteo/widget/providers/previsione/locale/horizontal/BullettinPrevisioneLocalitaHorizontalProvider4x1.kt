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
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.domain.json.previsione.Giorno
import com.gmail.fattazzo.meteo.domain.json.previsione.PrevisioneLocalita
import com.gmail.fattazzo.meteo.manager.MeteoManager
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import com.gmail.fattazzo.meteo.utils.LoadBitmapTask
import com.gmail.fattazzo.meteo.widget.providers.MeteoWidgetProvider
import com.gmail.fattazzo.meteo.widget.providers.previsione.LoadPrevisioneLocalitaTask
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EReceiver
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Fattazzo
 *
 *
 * Date 04/ago/2014
 */
@EReceiver
open class BullettinPrevisioneLocalitaHorizontalProvider4x1 : MeteoWidgetProvider() {

    @Bean
    lateinit var meteoManager: MeteoManager

    @Bean
    lateinit var preferencesManager: ApplicationPreferencesManager

    override fun getOpenAppResourceView(): IntArray? = intArrayOf(R.id.icona1Image, R.id.icona2Image, R.id.icona3Image, R.id.icona4Image, R.id.errorPrevisionHorizontalTV)

    override fun getRemoteViewsLayoutResource(): Int = R.layout.widget_prevision_horizontal_4x1

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("BB", "onUpdate horizontal 4x1")
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun doUpdate(remoteViews: RemoteViews, context: Context,
                          appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?, forRevalidate: Boolean) {

        if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
            for (widgetId in appWidgetIds) {

                val previsione: PrevisioneLocalita? = try {
                    LoadPrevisioneLocalitaTask(meteoManager, preferencesManager).execute().get()
                } catch (e: Exception) {
                    null
                }
                remoteViews.setViewVisibility(R.id.errorPrevisionHorizontalTV, if (previsione != null) View.GONE else View.VISIBLE)

                val giorni = previsione?.previsioni?.firstOrNull()?.giorni.orEmpty()

                remoteViews.setViewVisibility(R.id.prev1, View.GONE)
                remoteViews.setViewVisibility(R.id.prev2, View.GONE)
                remoteViews.setViewVisibility(R.id.prev3, View.GONE)
                remoteViews.setViewVisibility(R.id.prev4, View.GONE)

                val textColor = getWidgetsSettingsManager(context).textColor
                if (giorni.isNotEmpty()) {
                    fillGiorno(giorni[0], remoteViews, R.id.prev1, R.id.giorno1TV, R.id.temp1TV, R.id.icona1Image, textColor)
                }
                if (giorni.size > 1) {
                    fillGiorno(giorni[1], remoteViews, R.id.prev2, R.id.giorno2TV, R.id.temp2TV, R.id.icona2Image, textColor)
                }
                if (giorni.size > 2) {
                    fillGiorno(giorni[2], remoteViews, R.id.prev3, R.id.giorno3TV, R.id.temp3TV, R.id.icona3Image, textColor)
                }
                if (giorni.size > 3) {
                    fillGiorno(giorni[3], remoteViews, R.id.prev4, R.id.giorno4TV, R.id.temp4TV, R.id.icona4Image, textColor)
                }

                appWidgetManager.updateAppWidget(widgetId, remoteViews)
            }
        }
    }

    override fun updateTextColor(remoteViews: RemoteViews, textColor: Int) {

    }

    private fun fillGiorno(giorno: Giorno, remoteViews: RemoteViews, layoutId: Int, giornoTV: Int, temperaturaTV: Int, iconaImage: Int, textColor: Int) {
        remoteViews.setViewVisibility(layoutId, View.VISIBLE);

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

        try {
            remoteViews.setBitmap(iconaImage, "setImageBitmap", LoadBitmapTask().execute(giorno.icona).get())
        } catch (e: Exception) {
            remoteViews.setBitmap(iconaImage, "setImageBitmap", null)
        }
    }

    companion object {

        val DAY_FORMAT = SimpleDateFormat("EEEE", Locale.ITALIAN)
    }
}
