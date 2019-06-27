/*
 * Project: meteo
 * File: NewsFragment.kt
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

package com.gmail.fattazzo.meteo.fragment.news


import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.db.News
import com.gmail.fattazzo.meteo.fragment.BaseFragment
import com.gmail.fattazzo.meteo.fragment.home.HomeFragment_
import com.gmail.fattazzo.meteo.newsavvisi.NewsAvvisiType
import com.gmail.fattazzo.meteo.newsavvisi.NewsLoader
import com.gmail.fattazzo.meteo.utils.FragmentUtils
import org.androidannotations.annotations.*


@OptionsMenu(R.menu.news_menu)
@EFragment(R.layout.fragment_news)
open class NewsFragment : BaseFragment() {

    @JvmField
    @InstanceState
    var newsLoaded: Boolean = false

    @JvmField
    @InstanceState
    var newsType: Boolean = true

    @JvmField
    @InstanceState
    var avvisiType: Boolean = true

    @ViewById
    protected lateinit var newsRV: RecyclerView

    @ViewById
    protected lateinit var newsSwitch: Switch
    @ViewById
    protected lateinit var avvisiSwitch: Switch

    @AfterViews
    protected fun afterView() {

        newsSwitch.isChecked = newsType
        avvisiSwitch.isChecked = avvisiType

        if (!newsLoaded) {
            NewsLoader(context!!, false) {
                newsLoaded = true
                loadData()
            }.execute()
        } else {
            loadData()
        }
    }

    @Background
    open fun loadData() {
        val types = mutableListOf<NewsAvvisiType>()

        if(newsSwitch.isChecked) types.add(NewsAvvisiType.NEWS)
        if(avvisiSwitch.isChecked) types.add(NewsAvvisiType.AVVISI_ALLERTE)

        refreshViewData(News.loadAll(types))
    }

    @UiThread
    open fun refreshViewData(data: List<News>) {
        newsRV.adapter = NewsAdapter(context!!, data)
        newsRV.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    @Click(value = [R.id.newsSwitch, R.id.avvisiSwitch])
    fun switchClicked() {
        newsType = newsSwitch.isChecked
        avvisiType = avvisiSwitch.isChecked

        loadData()
    }

    @OptionsItem
    fun refreshAction() {
        newsLoaded = false
        afterView()
    }

    override fun getTitleResId(): Int = R.string.nav_news_e_allerte

    override fun backPressed(): Boolean {
        FragmentUtils.replace(activity as AppCompatActivity, HomeFragment_.builder().build())
        return true
    }
}
