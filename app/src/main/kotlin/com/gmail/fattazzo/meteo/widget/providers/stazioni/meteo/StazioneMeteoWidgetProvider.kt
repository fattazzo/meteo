/*
 * Project: meteo
 * File: StazioneMeteoWidgetProvider.kt
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

package com.gmail.fattazzo.meteo.widget.providers.stazioni.meteo

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import com.activeandroid.query.Select
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.LineData
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.db.StazioneMeteo
import com.gmail.fattazzo.meteo.domain.xml.stazioni.meteo.datistazione.IDatoStazione
import com.gmail.fattazzo.meteo.fragment.stazioni.meteo.rilevazioni.dati.grafico.DataSetBuilder
import com.gmail.fattazzo.meteo.fragment.stazioni.meteo.rilevazioni.dati.grafico.DateXAxisValueFormatter
import com.gmail.fattazzo.meteo.fragment.stazioni.meteo.rilevazioni.dati.grafico.UMValueFormatter
import com.gmail.fattazzo.meteo.manager.MeteoManager
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import com.gmail.fattazzo.meteo.widget.providers.MeteoWidgetProvider
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EReceiver
import java.lang.Exception


/**
 * @author fattazzo
 *         <p/>
 *         date: 06/12/17
 */
@EReceiver
open class StazioneMeteoWidgetProvider : MeteoWidgetProvider() {

    @Bean
    lateinit var meteoManager: MeteoManager

    @Bean
    lateinit var preferencesManager: ApplicationPreferencesManager

    @Bean
    lateinit var datasetBuilder: DataSetBuilder

    override fun doUpdate(remoteViews: RemoteViews, context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?, forRevalidate: Boolean) {

        if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
            val handleTimer = Handler()
            handleTimer.postDelayed({
                for (widgetId in appWidgetIds) {

                    val datiStazione = if (forRevalidate && StazioneMeteoWidgetCache.datiStazione != null) {
                        StazioneMeteoWidgetCache.datiStazione
                    } else {
                        try {
                            LoadDatiStazioneMeteoTask(meteoManager, preferencesManager).execute().get()
                        } catch (e: Exception) {
                            null
                        }
                    }

                    StazioneMeteoWidgetCache.datiStazione = datiStazione

                    remoteViews.setViewVisibility(R.id.errorStazioneMeteoTV, if (datiStazione != null) View.GONE else View.VISIBLE)
                    remoteViews.setViewVisibility(R.id.titleTV, if (datiStazione == null) View.GONE else View.VISIBLE)
                    remoteViews.setViewVisibility(R.id.chartImage, if (datiStazione == null) View.GONE else View.VISIBLE)
                    remoteViews.setViewVisibility(R.id.tipoDatoTV, if (datiStazione == null) View.GONE else View.VISIBLE)

                    remoteViews.setTextColor(R.id.errorStazioneMeteoTV, getWidgetsSettingsManager(context).textColor)
                    remoteViews.setTextColor(R.id.titleTV, getWidgetsSettingsManager(context).textColor)
                    remoteViews.setTextColor(R.id.tipoDatoTV, getWidgetsSettingsManager(context).textColor)

                    if (datiStazione != null) {
                        val stazioneMeteo = Select().from(StazioneMeteo::class.java).where("codice = ?", preferencesManager.getCodiceStazioneMeteoWidget()).executeSingle<StazioneMeteo>()
                        remoteViews.setTextViewText(R.id.titleTV, if (stazioneMeteo != null) stazioneMeteo.nome else datiStazione.codiceStazione)

                        val prefs = context.getSharedPreferences(XML_CONFIG, 0)
                        var dati: List<IDatoStazione>?


                        val deviceTypeId = prefs.getInt("widget_" + widgetId + "_tipoDato", 0)
                        if (forRevalidate) {
                            dati = when (deviceTypeId) {
                                0 -> datiStazione.temperature
                                1 -> datiStazione.precipitazioni
                                2 -> datiStazione.venti
                                3 -> datiStazione.radiazioni
                                4 -> datiStazione.umidita
                                5 -> datiStazione.neve
                                else -> datiStazione.temperature
                            }
                            remoteViews.setTextViewText(R.id.tipoDatoTV, context.resources.getStringArray(R.array.tipo_dato_stazione)[deviceTypeId])
                            prefs.edit().putInt("widget_" + widgetId + "_tipoDato", if (deviceTypeId < 5) deviceTypeId + 1 else 0).commit()
                        } else {
                            dati = datiStazione.temperature
                            prefs.edit().putInt("widget_" + widgetId + "_tipoDato", 1).commit()
                            remoteViews.setTextViewText(R.id.tipoDatoTV, context.resources.getStringArray(R.array.tipo_dato_stazione)[0])
                        }

                        val chart = createChart(dati, context)

                        val b = Bitmap.createBitmap(chart.measuredWidth, chart.measuredHeight, Bitmap.Config.ARGB_8888)
                        val c = Canvas(b)
                        chart.layout(0, 0, chart.measuredWidth, chart.measuredHeight)
                        chart.draw(c)

                        remoteViews.setBitmap(R.id.chartImage, "setImageBitmap", b)


                        val intent = Intent(context, this.javaClass)
                        intent.action = MeteoWidgetProvider.WIDGET_REVALIDATE
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                        remoteViews.setOnClickPendingIntent(R.id.chartImage, pendingIntent)
                    }

                    appWidgetManager.updateAppWidget(widgetId, remoteViews)
                }
            }, 1000)
        }
    }

    private fun createChart(datiParam: List<IDatoStazione>?, context: Context): LineChart {
        val dataSetList = datasetBuilder.buildDataset(datiParam,false)

        val lineChart = LineChart(context)
        lineChart.minimumWidth = 600
        lineChart.minimumHeight = 500
        lineChart.viewPortHandler.setChartDimens(600f, 500f)
        lineChart.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        lineChart.layout(0, 0, lineChart.measuredWidth, lineChart.measuredHeight)

        val typedValue = TypedValue()
        context.resources.getValue(R.dimen.widget_stazione_meteo_chart_font_size, typedValue, true)
        val textSize = typedValue.float

        val l = lineChart.legend
        l.formSize = 5f
        l.form = Legend.LegendForm.CIRCLE
        l.textSize = textSize
        l.textColor = getWidgetsSettingsManager(context).textColor

        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = DateXAxisValueFormatter()
        xAxis.granularity = 1f
        xAxis.textSize = textSize
        xAxis.textColor = getWidgetsSettingsManager(context).textColor

        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.valueFormatter = UMValueFormatter(datiParam?.firstOrNull()?.unitaMisura.orEmpty())
        lineChart.axisLeft.textSize = textSize
        lineChart.axisLeft.textColor = getWidgetsSettingsManager(context).textColor

        lineChart.setNoDataText("Nessun dato presente")
        lineChart.setNoDataTextColor(getWidgetsSettingsManager(context).textColor)

        val charDescription = Description()
        charDescription.text = ""
        lineChart.description = charDescription

        if(dataSetList.size > 0) {
            val data = LineData(dataSetList)
            lineChart.data = data
        }
        lineChart.invalidate()

        return lineChart
    }

    override fun getOpenAppResourceView(): IntArray? = intArrayOf(R.id.errorStazioneMeteoTV, R.id.openAppImage)

    override fun getRemoteViewsLayoutResource(): Int = R.layout.widget_stazione_meteo

    override fun updateTextColor(remoteViews: RemoteViews, textColor: Int) {
        // Update text color not supported, click on refresh icon
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray?) {
        val prefs = context.getSharedPreferences(XML_CONFIG, 0)

        if (appWidgetIds != null) {
            for(id in appWidgetIds) {
                prefs.edit().remove("widget_" + id + "_tipoDato").commit()
            }
        }

        super.onDeleted(context, appWidgetIds)
    }

    companion object {

        const val XML_CONFIG = "stazioni.meteo.widget.config"
    }
}