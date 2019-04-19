/*
 * Project: meteo
 * File: BullettinPrevisioneLocalitaTodayProvider4x1.kt
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
package com.gmail.fattazzo.meteo.widget.providers.previsione.locale

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.SplashActivity_
import com.gmail.fattazzo.meteo.manager.MeteoManager
import com.gmail.fattazzo.meteo.widget.providers.MeteoWidgetProvider
import com.gmail.fattazzo.meteo.widget.providers.previsione.fascia.corrente.FasceProvider
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
open class BullettinPrevisioneLocalitaTodayProvider4x1 : MeteoWidgetProvider() {

    @Bean
    lateinit var meteoManager: MeteoManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == FasceProvider.OPEN_APP_ACTION) {
            val intentActivity = Intent(context, SplashActivity_::class.java)
            intentActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intentActivity)
        }
        super.onReceive(context, intent)
    }

    override fun getOpenAppResourceView(): IntArray? = intArrayOf(R.id.errorTV)

    override fun getRemoteViewsLayoutResource(): Int = R.layout.widget_prevision_today_4x1

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("BB", "onUpdate 4x1")
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun doUpdate(remoteViews: RemoteViews, context: Context,
                          appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?, forRevalidate: Boolean) {

        val calendar = Calendar.getInstance()

        if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {

            val dateFormatter = SimpleDateFormat("HH:mm", Locale.ITALIAN)

            for (widgetId in appWidgetIds) {
                val intent = Intent(context, PrevisioneLocaleGridWidgetService::class.java)
                intent.putExtra(PrevisioneLocaleGridRemoteViewsFactory.EXTRA_DATA, dateFormatter.format(calendar.time))
                intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
                remoteViews.setRemoteAdapter(R.id.evoluzioneListView, intent)
                remoteViews.setEmptyView(R.id.evoluzioneListView, R.id.errorTV)

                val showResultIntent = Intent(context, this.javaClass)
                showResultIntent.action = OPEN_APP_ACTION
                val showResultPendingIntent = PendingIntent.getBroadcast(context, 0, showResultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                remoteViews.setPendingIntentTemplate(R.id.evoluzioneListView, showResultPendingIntent)

                appWidgetManager.updateAppWidget(widgetId, remoteViews)
            }
        }
    }

    override fun updateTextColor(remoteViews: RemoteViews, textColor: Int) {

    }

    companion object {
        const val OPEN_APP_ACTION = "openAppAction"
    }
}
