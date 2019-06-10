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
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.SplashActivity_
import com.gmail.fattazzo.meteo.utils.VectorUtils
import com.gmail.fattazzo.meteo.widget.providers.MeteoAppWidgetProvider


/**
 * @author Fattazzo
 *
 *
 * Date 04/ago/2014
 */
class BullettinPrevisioneLocalitaTodayProvider4x1 : MeteoAppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Widget", "onReceive: BullettinPrevisioneLocalitaTodayProvider4x1 - ${intent.action}")

            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisAppWidget = ComponentName(context.packageName, BullettinPrevisioneLocalitaTodayProvider4x1::class.java.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)

            onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        Log.d("Widget", "onUpdate: BullettinPrevisioneLocalitaTodayProvider4x1")

        appWidgetIds?.forEach { appWidgetId ->
            val intent = Intent(context, PrevisioneLocaleGridWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }

            val rv = RemoteViews(context?.packageName, R.layout.widget_prevision_today_4x1).apply {
                setRemoteAdapter(R.id.evoluzioneListView, intent)
                setEmptyView(R.id.evoluzioneListView, R.id.errorTV)
                setImageViewBitmap(R.id.widget_sync, VectorUtils.vectorToBitmap(context!!, R.drawable.sync))
                registerRefreshIntent(context, this, appWidgetId)
                registerOpenAppIntent(context, this, R.id.errorTV)

                val templateIntent = Intent(context, SplashActivity_::class.java)
                templateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                val templatePendingIntent = PendingIntent.getActivity(
                        context, 0, templateIntent, 0)
                setPendingIntentTemplate(R.id.evoluzioneListView, templatePendingIntent)
            }
            appWidgetManager?.updateAppWidget(appWidgetId, rv)
        }

        appWidgetManager?.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.evoluzioneListView)
    }
}
