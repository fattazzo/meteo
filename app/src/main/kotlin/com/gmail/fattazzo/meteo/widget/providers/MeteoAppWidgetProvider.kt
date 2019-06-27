/*
 * Project: meteo
 * File: MeteoAppWidgetProviderder.kt
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

package com.gmail.fattazzo.meteo.widget.providers

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.SplashActivity_

/**
 * @author fattazzo
 *         <p/>
 *         date: 07/06/19
 */
abstract class MeteoAppWidgetProvider: AppWidgetProvider() {

    protected fun registerRefreshIntent(context: Context, remoteViews: RemoteViews, appWidgetId: Int, refreshViewId: Int = R.id.widget_sync) {

        val intent = Intent(context, this::class.java)

        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews.setOnClickPendingIntent(refreshViewId, pendingIntent)
    }

    protected fun registerOpenAppIntent(context: Context, remoteViews: RemoteViews, viewId: Int) {
        val intent = Intent(context, SplashActivity_::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        remoteViews.setOnClickPendingIntent(viewId, pendingIntent)
    }

    protected fun updateBackground(remoteViews: RemoteViews, backgroundColor: Int, backgroundResId : Int = R.id.widget_background_layout) {
        remoteViews.setInt(backgroundResId, "setBackgroundResource", backgroundColor)
    }

    companion object {

        const val UPDATE = "com.gmail.fattazzo.meteo.widget.UPDATE"

        const val WIDGETS_PREF = "WIDGETS_PREF"
    }
}