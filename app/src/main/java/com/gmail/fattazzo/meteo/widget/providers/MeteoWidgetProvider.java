/*
 * Project: meteo
 * File: MeteoAppWidgetProvider
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

package com.gmail.fattazzo.meteo.widget.providers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.gmail.fattazzo.meteo.R;
import com.gmail.fattazzo.meteo.activity.SplashActivity_;
import com.gmail.fattazzo.meteo.manager.MeteoManager;
import com.gmail.fattazzo.meteo.manager.MeteoManager_;
import com.gmail.fattazzo.meteo.preferences.widget.WidgetSettingsManager;
import com.gmail.fattazzo.meteo.preferences.widget.bollettino.BollettinoWidgetsSettingsManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author fattazzo
 * <p>
 * date: 27/ago/2014
 */
public abstract class MeteoWidgetProvider extends AppWidgetProvider {

    // evento di update standard del widget
    public static final String AUTO_UPDATE = "com.gmail.fattazzo.meteo.widget.AUTO_UPDATE";
    // evento di update delle settings del widget
    public static final String UPDATE_SETTINGS = "com.gmail.fattazzo.meteo.widget.UPDATE_SETTINGS";

    public static final String WIDGET_REVALIDATE = "com.gmail.fattazzo.meteo.widget.WIDGET_REVALIDATE";

    public static final String DEFAULT_VALUE = "-.-";
    public static final String WIDGETS_PREF = "WIDGETS_PREF";
    protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
    private MeteoManager meteoManager;
    private boolean revalidate;

    private BollettinoWidgetsSettingsManager widgetsSettingsManager;

    /**
     * Cancella dall' AlarmManager l'evento per l'update del widget.
     *
     * @param context context
     */
    private void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createClockTickIntent(context));
    }

    /**
     * Crea l'intent da far lanciare all' AlarmManager.
     *
     * @param context context
     * @return inten creato
     */
    private PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent(context, this.getClass());
        intent.setAction(AUTO_UPDATE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Esegue l'update della vista.
     *
     * @param remoteViews      vista
     * @param context          context
     * @param appWidgetManager {@link AppWidgetManager}
     * @param appWidgetIds     app widget ids
     * @param forRevalidate    revalidate only option
     */
    protected abstract void doUpdate(RemoteViews remoteViews, Context context, AppWidgetManager appWidgetManager,
                                     int[] appWidgetIds, boolean forRevalidate);

    /**
     * @param context context
     * @return {@link MeteoManager}
     */
    protected MeteoManager getMeteoManager(Context context) {
        if (meteoManager == null) {
            meteoManager = MeteoManager_.getInstance_(context);
        }

        return meteoManager;
    }

    /**
     * Risorsa a cui associare l'azione di apertura dell'applicazione.
     *
     * @return id risorsa
     */
    protected abstract @Nullable
    int[] getOpenAppResourceView();

    /**
     * Risorsa a cui associare l'azione di refresh del widget.
     *
     * @return id risorsa
     */
    protected int getRefreshResourceView() {
        return R.id.widget_sync;
    }

    /**
     * Risorsa del layout del widget.
     *
     * @return id risorsa
     */
    protected abstract int getRemoteViewsLayoutResource();

    /**
     * @param context context
     * @return {@link WidgetSettingsManager}
     */
    protected WidgetSettingsManager getWidgetsSettingsManager(Context context) {
        if (widgetsSettingsManager == null) {
            widgetsSettingsManager = new BollettinoWidgetsSettingsManager(context);
        }

        return widgetsSettingsManager;
    }

    /**
     * Verifica se esistono istanze del widget.
     *
     * @param context context
     * @return <code>true</code> se esistono istanze
     */
    private boolean hasInstances(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, this.getClass()));
        return appWidgetIds.length > 0;
    }

    @Override
    public void onDisabled(Context context) {
        // rimuovo l'evento dall' AlarmManager.
        if (!hasInstances(context)) {
            cancelAlarm(context);
        }
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        // faccio partire l'evento schedulato
        startAlarm(context);
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), intent.getComponent().getClassName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

        super.onReceive(context, intent);

        revalidate = false;
        // se ho un evento standard di update chiamo la onUpdate del privider
        switch (intent.getAction()) {
            case AUTO_UPDATE:
                onUpdate(context, appWidgetManager, appWidgetIds);
                break;
            case UPDATE_SETTINGS:
                // nel caso di un update delle settings restarto l'AlaramManager e aggiorno il layout del widget
                restartAlarm(context);
                onUpdate(context, appWidgetManager, appWidgetIds);
                break;
            case WIDGET_REVALIDATE:
                revalidate = true;
                onUpdate(context, appWidgetManager, appWidgetIds);
                break;
            case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                startAlarm(context);
                break;
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), getRemoteViewsLayoutResource());

        // Register refresh intent. Non prendo solo gli id dei widget che mi arrivano ma tutti gli id dei widget della
        // classe. Questo perchè potrei avere più widget della classe e in questo modo li aggiorno tutti.
        registerRefreshIntent(context, remoteViews,
                appWidgetManager.getAppWidgetIds(new ComponentName(context, this.getClass())));

        // Register open app intent
        registerOpenAppIntent(context, remoteViews);

        updateBackground(remoteViews, getWidgetsSettingsManager(context).getBackground());
        updateTextColor(remoteViews, getWidgetsSettingsManager(context).getTextColor());
        doUpdate(remoteViews, context, appWidgetManager, appWidgetIds, revalidate);
    }

    /**
     * Registra l'intent per aprire l'app sulla view specificata nel metodo
     * {@link MeteoAppWidgetProvider#getOpenAppResourceView()}.
     *
     * @param context     context
     * @param remoteViews remoteView
     */
    private void registerOpenAppIntent(Context context, RemoteViews remoteViews) {
        if (getOpenAppResourceView() != null && getOpenAppResourceView().length > 0) {
            for (int viewId : getOpenAppResourceView()) {
                Intent intent = new Intent(context, SplashActivity_.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                remoteViews.setOnClickPendingIntent(viewId, pendingIntent);
            }
        }
    }

    /**
     * Registra l'intent per eseguire il refresh del widget sulla view specificata nel metodo
     * {@link MeteoAppWidgetProvider#getRefreshResourceView()}.
     *
     * @param context      context
     * @param remoteViews  remoteView
     * @param appWidgetIds id dei widgets
     */
    private void registerRefreshIntent(Context context, RemoteViews remoteViews, int[] appWidgetIds) {

        Intent intent = new Intent(context, this.getClass());

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(getRefreshResourceView(), pendingIntent);
    }

    /**
     * Ferma e rilancia l'evento per eseguire l'update dei widget.
     *
     * @param context context
     */
    protected void restartAlarm(Context context) {
        cancelAlarm(context);
        startAlarm(context);
    }

    /**
     * Lancia l'evento schedulato per eseguire l'update dei widgets.
     *
     * @param context context
     */
    private void startAlarm(Context context) {
        int updateInterval = getWidgetsSettingsManager(context).getUpdateInterval();

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar current = Calendar.getInstance();
        am.setRepeating(AlarmManager.RTC, current.getTimeInMillis() + updateInterval, updateInterval,
                createClockTickIntent(context));
    }

    /**
     * Aggiorna il background del widget.
     *
     * @param remoteViews     view
     * @param backgroundColor colore
     */
    protected void updateBackground(RemoteViews remoteViews, int backgroundColor) {
        remoteViews.setInt(R.id.widget_background_layout, "setBackgroundResource", backgroundColor);
    }

    /**
     * Aggiorna il foreground del widget.
     *
     * @param remoteViews view
     * @param textColor   colore
     */
    protected abstract void updateTextColor(RemoteViews remoteViews, int textColor);

    /**
     * Medoto chiamato per aggiornare il layout dei widget.
     *
     * @param context          context
     * @param appWidgetManager app widget manager
     * @param appWidgetIds     widgets id
     */
    protected void updateWidgetLayout(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), getRemoteViewsLayoutResource());
        updateBackground(remoteViews, getWidgetsSettingsManager(context).getBackground());
        updateTextColor(remoteViews, getWidgetsSettingsManager(context).getTextColor());

        for (final int widgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
