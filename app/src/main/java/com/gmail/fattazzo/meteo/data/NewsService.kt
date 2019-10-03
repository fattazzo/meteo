/*
 * Project: meteo
 * File: NewsService.kt
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

package com.gmail.fattazzo.meteo.data

import com.gmail.fattazzo.meteo.data.news.NewsAvvisiType
import com.gmail.fattazzo.meteo.data.news.NewsDownloader
import com.gmail.fattazzo.meteo.db.News

/**
 * @author fattazzo
 *         <p/>
 *         date: 30/09/19
 */
class NewsService() {

    fun load(types: List<NewsAvvisiType>, forceDownload: Boolean): List<News> {

        val news = News.loadAll(types)

        return if (news.isEmpty() || forceDownload) {
            val downloader = NewsDownloader()

            val newsDownloaded = mutableListOf<News>()
            types.forEach {
                when (it) {
                    NewsAvvisiType.NEWS -> newsDownloaded.addAll(downloader.getNews())
                    NewsAvvisiType.AVVISI_ALLERTE -> newsDownloaded.addAll(downloader.getAvvisi())
                }
            }

            newsDownloaded
        } else {
            news
        }
    }
}