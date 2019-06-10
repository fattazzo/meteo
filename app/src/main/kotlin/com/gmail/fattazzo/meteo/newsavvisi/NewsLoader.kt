/*
 * Project: meteo
 * File: NewsLoader.kt
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

package com.gmail.fattazzo.meteo.newsavvisi

import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.appcompat.widget.DialogTitle
import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.db.News
import com.gmail.fattazzo.meteo.utils.dialog.DialogBuilder
import com.gmail.fattazzo.meteo.utils.dialog.DialogType
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 29/04/19
 */
class NewsLoader(context: Context,
                 private val backgroundProcess: Boolean = false,
                 private val postExecuteAction: () -> Unit) : AsyncTask<Void, String, Int>() {

    private var dialog: Dialog = DialogBuilder(context, DialogType.INDETERMINATE_PROGRESS).apply {
        title = "News, Avvisi e Infotraffico"
        message = "Download in corso..."
        headerIcon = R.drawable.loading
    }.build()

    override fun onPreExecute() {
        if (!backgroundProcess) {
            dialog.show()
        }
    }

    override fun doInBackground(vararg p0: Void?): Int {

        try {
            loadNews()
            loadAvvisi()
            loadInfotraffico()

        } catch (e: Exception) {
            Log.d("aaa", e.printStackTrace().toString())
            return -1
        }

        return 0
    }

    override fun onPostExecute(result: Int) {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        postExecuteAction.invoke()
    }

    private fun loadNews() {
        publishProgress("News e comunicati stampa")

        val newsNuove = mutableListOf<News>()

        val doc = Jsoup.connect(Config.NEWS_URL).get()

        val elements = doc.getElementsByClass("elenco_img")

        elements.forEach {

            val spanData = it.select("span.bold.verde")
            val link = it.select("a")
            val spanCategory = it.select("span[class=verde]")

            val news = News().apply {
                date = SimpleDateFormat("dd.M.yyyy").parse(spanData[0].text())
                category = spanCategory[0].text().replace("(", "").replace(")", "")
                title = link[0].text()
                url = link[0].attr("href")
                type = NewsAvvisiType.NEWS.ordinal
            }

            newsNuove.add(news)
        }

        News.deleteAll(NewsAvvisiType.NEWS)

        newsNuove.forEach { it.save() }
    }

    private fun loadAvvisi() {
        publishProgress("Avvisi e allerte")

        val newsNuove = mutableListOf<News>()

        val doc = Jsoup.connect(Config.AVVISI_URL).get()

        val elementsUl = doc.getElementsByClass("elenco_no_punto")

        val elementsLi = elementsUl.select("li")

        elementsLi.forEach {

            val spanData = it.select("span[class=testo]")
            val link = it.select("a")

            // fomato data: "( dd MMMM yyyy nnn kb )"
            val dataSplit = spanData[0].text().split(" ")
            val dateString = "${dataSplit[1]} ${dataSplit[2].capitalize()} ${dataSplit[3]}"

            val downloadUrl = "http://docs.google.com/gview?embedded=true&url=" +
                    "http://avvisi.protezionecivile.tn.it" +
                    link[0].attr("onclick").substringAfter("'").substringBefore("'")

            val news = News().apply {
                date = SimpleDateFormat("dd MMMM yyyy", Locale.ITALIAN).parse(dateString)
                category = "Avvisi e allerte"
                title = link[0].text()
                url = downloadUrl
                type = NewsAvvisiType.AVVISI_ALLERTE.ordinal
            }

            newsNuove.add(news)
        }

        News.deleteAll(NewsAvvisiType.AVVISI_ALLERTE)

        newsNuove.forEach { it.save() }
    }

    private fun loadInfotraffico() {
        publishProgress("Infotraffico")

        val newsNuove = mutableListOf<News>()

        val doc = Jsoup.connect(Config.INFOTRAFFICO_URL).get()

        val elementsLi = doc.select("li[class=elenco_img]")

        elementsLi.forEach {

            val spanData = it.select("span.bold.verde")
            val motivo = it.select("a")[0].text()
            val strada = it.select("span[class=strada]")[0].text()
            val dove = it.select("span[class=dove]")[0].text()

            val dateString = spanData[0].text().replace("(", "").replace(")", "")

            val news = News().apply {
                date = SimpleDateFormat("dd.MM.yyyy", Locale.ITALIAN).parse(dateString)
                category = "Infotraffico"
                title = "$motivo\n$strada - $dove"
                url = null
                type = NewsAvvisiType.INFO_TRAFFICO.ordinal
            }

            newsNuove.add(news)
        }

        News.deleteAll(NewsAvvisiType.INFO_TRAFFICO)

        newsNuove.forEach { it.save() }
    }

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)

        if(values.isNotEmpty()) {
            dialog.findViewById<DialogTitle>(R.id.alertTitle).text = values[0]
        }
    }
}