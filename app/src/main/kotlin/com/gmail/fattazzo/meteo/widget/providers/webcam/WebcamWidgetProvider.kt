/*
 * Project: meteo
 * File: WebcamWidgetProvider.kt
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

package com.gmail.fattazzo.meteo.widget.providers.webcam

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.AppWidgetTarget
import com.bumptech.glide.signature.ObjectKey
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.manager.MeteoManager_
import com.gmail.fattazzo.meteo.preferences.widget.bollettino.BollettinoWidgetsSettingsManager
import com.gmail.fattazzo.meteo.utils.VectorUtils
import com.gmail.fattazzo.meteo.widget.providers.MeteoAppWidgetProvider

/**
 * @author fattazzo
 *
 *
 * date: 24/lug/2015
 */
class WebcamWidgetProvider : MeteoAppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action === AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED) {
            return
        }

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidget = ComponentName(context.packageName, WebcamWidgetProvider::class.java.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)

        onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        Log.d("Widget", "onUpdate: WebcamWidgetProvider")

        val webcam = MeteoManager_.getInstance_(context).caricaWebcam()
        val webcamWidget = WebcamWidgetManager.getNextWebcam(context, webcam)

        appWidgetIds?.forEach { appWidgetId ->

            val remoteViews = RemoteViews(context?.packageName, R.layout.widget_webcam_4x4)
            val widgetSettingsManager = BollettinoWidgetsSettingsManager(context!!)
            updateBackground(remoteViews, widgetSettingsManager.background, R.id.widget_webcam_root_panel)
            remoteViews.setImageViewBitmap(R.id.widget_sync, VectorUtils.vectorToBitmap(context, R.drawable.sync))
            registerRefreshIntent(context, remoteViews, appWidgetId)
            registerRefreshIntent(context, remoteViews, appWidgetId, R.id.widget_webcam_image)

            if (webcamWidget != null) {
                remoteViews.setTextViewText(R.id.widget_webcam_title_text, webcamWidget.descrizione)

                val appWidgetTarget = AppWidgetTarget(context, R.id.widget_webcam_image, remoteViews, appWidgetId)
                Glide.with(context.applicationContext)
                        .setDefaultRequestOptions(RequestOptions().apply {
                            error(R.drawable.browser_error)
                            signature(ObjectKey(SystemClock.currentThreadTimeMillis()))
                            diskCacheStrategy(DiskCacheStrategy.NONE)
                            skipMemoryCache(true)
                        })
                        .asBitmap()
                        .thumbnail(0.1f)
                        .override(480, 342)
                        .load(webcamWidget.link)
                        .into(appWidgetTarget)
            } else {
                remoteViews.setBitmap(R.id.widget_webcam_image, "setImageBitmap", null)
                remoteViews.setTextViewText(R.id.widget_webcam_title_text, "Nessuna webcam configurata.")
            }

            appWidgetManager?.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    companion object {
        const val WEBCAM_CURRENT_ID = "webcamCurrentId"
        const val WEBCAM_CURRENT_LINK_INDEX = "webcamCurrentLinkIndex"
    }
}
