/*
 * Project: meteo
 * File: StazioneMeteoWidgetProvider.kt
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

package com.gmail.fattazzo.meteo.widget.providers.stazioni.meteo

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.LineData
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.rilevazioni.dati.grafico.DataSetBuilder_
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.rilevazioni.dati.grafico.DateXAxisValueFormatter
import com.gmail.fattazzo.meteo.activity.stazioni.meteo.rilevazioni.dati.grafico.UMValueFormatter
import com.gmail.fattazzo.meteo.app.MeteoApplication
import com.gmail.fattazzo.meteo.app.services.StazioniService
import com.gmail.fattazzo.meteo.data.db.AppDatabase
import com.gmail.fattazzo.meteo.data.db.entities.StazioneMeteo
import com.gmail.fattazzo.meteo.data.stazioni.meteo.domain.datistazione.IDatoStazione
import com.gmail.fattazzo.meteo.preferences.PreferencesService
import com.gmail.fattazzo.meteo.utils.VectorUtils
import com.gmail.fattazzo.meteo.widget.providers.MeteoAppWidgetProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import javax.inject.Inject


/**
 * @author fattazzo
 *         <p/>
 *         date: 06/12/17
 */
open class StazioneMeteoWidgetProvider : MeteoAppWidgetProvider() {

    @Inject
    lateinit var preferencesService: PreferencesService

    @Inject
    lateinit var stazioniService: StazioniService

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action === AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED) {
            return
        }

        if (intent.action != ACTION_CHANGE_TIPO_DATO) {

        }

        if (intent.action === AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val prefs = context.getSharedPreferences(XML_CONFIG, 0)
            prefs.edit().putInt(
                "widget_" + intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    -1
                ) + "_tipoDato", 0
            ).commit()
            StazioneMeteoWidgetCache.datiStazione = null
        }

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidget =
            ComponentName(context.packageName, StazioneMeteoWidgetProvider::class.java.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)

        onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        Log.d("Widget", "onUpdate: StazioneMeteoWidgetProvider")

        val datiStazione =
            if (StazioneMeteoWidgetCache.datiStazione != null) StazioneMeteoWidgetCache.datiStazione else
                try {
                    LoadDatiStazioneMeteoTask(WeakReference(context)).execute().get()
                } catch (e: Exception) {
                    null
                }

        if (datiStazione?.codiceStazione == null) {
            return
        }

        StazioneMeteoWidgetCache.datiStazione = datiStazione

        appWidgetIds?.forEach { appWidgetId ->


            val remoteViews = RemoteViews(context?.packageName, R.layout.widget_stazione_meteo)
            remoteViews.setViewVisibility(
                R.id.errorStazioneMeteoTV,
                if (datiStazione != null) View.GONE else View.VISIBLE
            )
            remoteViews.setViewVisibility(
                R.id.titleTV,
                if (datiStazione == null) View.GONE else View.VISIBLE
            )
            remoteViews.setViewVisibility(
                R.id.chartImage,
                if (datiStazione == null) View.GONE else View.VISIBLE
            )
            remoteViews.setViewVisibility(
                R.id.tipoDatoTV,
                if (datiStazione == null) View.GONE else View.VISIBLE
            )
            registerOpenAppIntent(context!!, remoteViews, R.id.errorStazioneMeteoTV)
            registerOpenAppIntent(context, remoteViews, R.id.openAppImage)

            remoteViews.setImageViewBitmap(
                R.id.widget_sync,
                VectorUtils.vectorToBitmap(context, R.drawable.sync)
            )

            (context.applicationContext as MeteoApplication).appComponent.inject(this@StazioneMeteoWidgetProvider)

            remoteViews.setTextColor(
                R.id.errorStazioneMeteoTV,
                preferencesService.getWidgetsTextColor()
            )
            remoteViews.setTextColor(R.id.titleTV, preferencesService.getWidgetsTextColor())
            remoteViews.setTextColor(R.id.tipoDatoTV, preferencesService.getWidgetsTextColor())
            updateBackground(remoteViews, preferencesService.getWidgetsBackground())

            if (datiStazione != null) {
                val stazioneMeteo: StazioneMeteo = runBlocking {
                    withContext(Dispatchers.IO) {
                        AppDatabase(context).stazioniMeteoDao()
                            .load(preferencesService.getCodiceStazioneMeteoWidget())
                    }
                }
                remoteViews.setTextViewText(
                    R.id.titleTV,
                    if (stazioneMeteo != null) stazioneMeteo.nome else datiStazione.codiceStazione
                )

                val prefs = context.getSharedPreferences(XML_CONFIG, 0)
                var dati: List<IDatoStazione>?


                val deviceTypeId = prefs.getInt("widget_" + appWidgetId + "_tipoDato", 0)
                dati = when (deviceTypeId) {
                    0 -> datiStazione.temperature
                    1 -> datiStazione.precipitazioni
                    2 -> datiStazione.venti
                    3 -> datiStazione.radiazioni
                    4 -> datiStazione.umidita
                    5 -> datiStazione.neve
                    else -> datiStazione.temperature
                }
                remoteViews.setTextViewText(
                    R.id.tipoDatoTV,
                    context.resources.getStringArray(R.array.tipo_dato_stazione)[deviceTypeId]
                )
                prefs.edit().putInt(
                    "widget_" + appWidgetId + "_tipoDato",
                    if (deviceTypeId < 5) deviceTypeId + 1 else 0
                ).commit()

                val chart = createChart(dati, context, preferencesService)

                val b = Bitmap.createBitmap(
                    chart.measuredWidth,
                    chart.measuredHeight,
                    Bitmap.Config.ARGB_8888
                )
                val c = Canvas(b)
                chart.layout(0, 0, chart.measuredWidth, chart.measuredHeight)
                chart.draw(c)

                remoteViews.setBitmap(R.id.chartImage, "setImageBitmap", b)


                val intent = Intent(context, this.javaClass)
                intent.action = ACTION_CHANGE_TIPO_DATO
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                remoteViews.setOnClickPendingIntent(R.id.chartImage, pendingIntent)
            }

            registerRefreshIntent(context, remoteViews, appWidgetId)

            appWidgetManager?.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    private fun createChart(
        datiParam: List<IDatoStazione>?,
        context: Context,
        preferencesService: PreferencesService
    ): LineChart {
        val dataSetList = DataSetBuilder_.getInstance_(context).buildDataset(datiParam, false)

        val lineChart = LineChart(context)
        lineChart.minimumWidth = 600
        lineChart.minimumHeight = 500
        lineChart.viewPortHandler.setChartDimens(600f, 500f)
        lineChart.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        lineChart.layout(0, 0, lineChart.measuredWidth, lineChart.measuredHeight)

        val typedValue = TypedValue()
        context.resources.getValue(R.dimen.widget_stazione_meteo_chart_font_size, typedValue, true)
        val textSize = typedValue.float

        val l = lineChart.legend
        l.formSize = 5f
        l.form = Legend.LegendForm.CIRCLE
        l.textSize = textSize
        l.textColor = preferencesService.getWidgetsTextColor()

        val xAxis = lineChart.xAxis
        xAxis.valueFormatter =
            DateXAxisValueFormatter()
        xAxis.granularity = 1f
        xAxis.textSize = textSize
        xAxis.textColor = preferencesService.getWidgetsTextColor()

        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.valueFormatter =
            UMValueFormatter(
                datiParam?.firstOrNull()?.unitaMisura.orEmpty()
            )
        lineChart.axisLeft.textSize = textSize
        lineChart.axisLeft.textColor = preferencesService.getWidgetsTextColor()

        lineChart.setNoDataText("Nessun dato presente")
        lineChart.setNoDataTextColor(preferencesService.getWidgetsTextColor())

        val charDescription = Description()
        charDescription.text = ""
        lineChart.description = charDescription

        if (dataSetList.size > 0) {
            val data = LineData(dataSetList)
            lineChart.data = data
        }
        lineChart.invalidate()

        return lineChart
    }

    companion object {

        const val XML_CONFIG = "stazioni.meteo.widget.config"

        const val ACTION_CHANGE_TIPO_DATO =
            "com.gmail.fattazzo.meteo.widget.WIDGET_CHANGE_TIPO_DATO_ACTION"
    }
}