/*
 * Project: meteo
 * File: NewsViewModel.kt
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

package com.gmail.fattazzo.meteo.activity.news

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.fattazzo.meteo.data.NewsService
import com.gmail.fattazzo.meteo.data.news.News
import com.gmail.fattazzo.meteo.data.news.NewsAvvisiType
import com.gmail.fattazzo.meteo.utils.ioJob
import com.gmail.fattazzo.meteo.utils.uiJob
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 28/09/19
 */
class NewsViewModel @Inject constructor(private val newsService: NewsService) : ViewModel() {

    val newsAttive = MutableLiveData<Boolean>(true)
    val avvisiAttivi = MutableLiveData<Boolean>(true)

    val news = MediatorLiveData<List<News>>().apply {
        addSource(newsAttive) { loadNews() }
        addSource(avvisiAttivi) { loadNews() }
    }

    val loadingData = ObservableBoolean(false)

    fun loadNews() {

        loadingData.set(true)

        ioJob {

            val types = mutableListOf<NewsAvvisiType>()
            if (newsAttive.value == true) types.add(NewsAvvisiType.NEWS)
            if (avvisiAttivi.value == true) types.add(NewsAvvisiType.AVVISI_ALLERTE)

            val newsLoaded = newsService.load(types)

            uiJob {
                news.postValue(newsLoaded)
                loadingData.set(false)
            }
        }
    }
}