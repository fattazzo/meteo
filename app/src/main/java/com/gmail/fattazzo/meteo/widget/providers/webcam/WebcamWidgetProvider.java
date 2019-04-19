/*
 * Project: meteo
 * File: WebcamWidgetProvider
 *
 * Created by fattazzo
 * Copyright © 2018 Gianluca Fattarsi. All rights reserved.
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

package com.gmail.fattazzo.meteo.widget.providers.webcam;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.gmail.fattazzo.meteo.R;
import com.gmail.fattazzo.meteo.domain.xml.webcam.Webcams;
import com.gmail.fattazzo.meteo.preferences.widget.webcam.WebcamWidgetsSettingsManager;
import com.gmail.fattazzo.meteo.utils.LoadBitmapTask;
import com.gmail.fattazzo.meteo.widget.providers.MeteoWidgetProvider;

/**
 * @author fattazzo
 *         <p>
 *         date: 24/lug/2015
 */
public class WebcamWidgetProvider extends MeteoWidgetProvider {

    public static final String WEBCAM_CURRENT_ID = "webcamCurrentId";
    public static final String WEBCAM_CURRENT_LINK_INDEX = "webcamCurrentLinkIndex";

    private WebcamWidgetsSettingsManager widgetsSettingsManager;

    @Override
    protected void doUpdate(RemoteViews remoteViews, Context context, AppWidgetManager appWidgetManager,
                            int[] appWidgetIds, boolean forRevalidate) {

        if (appWidgetIds != null && appWidgetIds.length > 0) {

            Intent intent = new Intent(context, this.getClass());
            intent.setAction(MeteoWidgetProvider.WIDGET_REVALIDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_sync, pendingIntent);

            Webcams webcam = getMeteoManager(context).caricaWebcam();
            WebcamWidget webcamWidget;
            if (forRevalidate) {
                webcamWidget = WebcamWidgetManager.getCurrentWebcam(context, webcam.getWebcams());
            } else {
                webcamWidget = WebcamWidgetManager.getNextWebcam(context, webcam);
            }

            for (final int widgetId : appWidgetIds) {

                if (webcamWidget != null) {
                    // riduco la dimensione dell'immagine altrimenti incorro in problemi di OutOfMemory
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    LoadBitmapTask loadBitbamTask = (LoadBitmapTask) new LoadBitmapTask(options).execute(webcamWidget
                            .getLink());

                    Bitmap bitmapResult;
                    try {
                        bitmapResult = loadBitbamTask.get();
                    } catch (Exception e) {
                        bitmapResult = null;
                    }

                    remoteViews.setBitmap(R.id.widget_webcam_image, "setImageBitmap", bitmapResult);
                    remoteViews.setTextViewText(R.id.widget_webcam_title_text, webcamWidget.getDescrizione());
                } else {
                    remoteViews.setBitmap(R.id.widget_webcam_image, "setImageBitmap", null);
                    remoteViews.setTextViewText(R.id.widget_webcam_title_text, "Nessuna webcam configurata.");
                }

                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
    }

    @Override
    protected int[] getOpenAppResourceView() {
        return null;
    }

    @Override
    protected int getRefreshResourceView() {
        // utilizzo il next button per andare alla prossima webcam facendo eseguire un refresh al widget
        return R.id.widget_webcam_next_button;
    }

    @Override
    protected int getRemoteViewsLayoutResource() {
        return R.layout.widget_webcam_4x4;
    }

    @Override
    protected WebcamWidgetsSettingsManager getWidgetsSettingsManager(Context context) {
        if (widgetsSettingsManager == null) {
            widgetsSettingsManager = new WebcamWidgetsSettingsManager(context);
        }

        return widgetsSettingsManager;
    }

    @Override
    protected void updateBackground(RemoteViews remoteViews, int backgroundColor) {
        remoteViews.setInt(R.id.widget_webcam_root_panel, "setBackgroundResource", backgroundColor);
    }

    @Override
    protected void updateTextColor(RemoteViews remoteViews, int textColor) {
        remoteViews.setTextColor(R.id.widget_webcam_title_text, textColor);
    }

}
