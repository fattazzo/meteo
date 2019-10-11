/*
 * Project: meteo
 * File: NewsDownloader.kt
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

package com.gmail.fattazzo.meteo.data.news

import android.annotation.SuppressLint
import com.gmail.fattazzo.meteo.Config
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 30/09/19
 */
class NewsDownloader {

    @SuppressLint("DefaultLocale")
    fun getAvvisi(): List<News> {
        val avvisi = mutableListOf<News>()

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

            val news = News(
                SimpleDateFormat("dd MMMM yyyy", Locale.ITALIAN).parse(dateString),
                "Avvisi e allerte",
                link[0].text(),
                downloadUrl,
                NewsAvvisiType.AVVISI_ALLERTE.ordinal
            )

            avvisi.add(news)
        }

        return avvisi
    }

    @SuppressLint("SimpleDateFormat")
    fun getNews(): List<News> {
        val newsNuove = mutableListOf<News>()

        val response = Jsoup.connect(Config.NEWS_URL).execute()

        val doc = Jsoup.parse(response.body())

        val elements = doc.getElementsByClass("elenco_img")

        elements.forEach {

            val spanData = it.select("span.bold.verde")
            val link = it.select("a")
            val spanCategory = it.select("span[class=verde]")

            val news = News(
                SimpleDateFormat("dd.M.yyyy").parse(spanData[0].text()),
                spanCategory[0].text().replace("(", "").replace(")", ""),
                link[0].text(),
                link[0].attr("href"),
                NewsAvvisiType.NEWS.ordinal
            )

            newsNuove.add(news)
        }

        return newsNuove
    }
}