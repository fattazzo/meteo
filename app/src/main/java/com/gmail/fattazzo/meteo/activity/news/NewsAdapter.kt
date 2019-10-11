/*
 * Project: meteo
 * File: NewsAdapter.kt
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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.data.news.News
import com.gmail.fattazzo.meteo.data.news.NewsAvvisiType
import kotlinx.android.synthetic.main.item_news.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Vipul Asri on 13-12-2018.
 */

class NewsAdapter(private val context: Context, private val mFeedList: List<News>) :
    RecyclerView.Adapter<NewsAdapter.TimeLineViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TimeLineViewHolder(
            layoutInflater.inflate(R.layout.item_news, parent, false),
            viewType
        )
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = mFeedList[position]

        holder.date.text =
            SimpleDateFormat("dd MMMM yyyy", Locale.ITALIAN).format(timeLineModel.date)
        holder.category.text = timeLineModel.category
        holder.message.text = timeLineModel.title
        if (timeLineModel.url.orEmpty().isNotBlank()) {
            holder.newsContainerLayout.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                i.data = Uri.parse(timeLineModel.url)
                context.startActivity(i)
            }
        }
        val newsDrawableResId = when (NewsAvvisiType.values()[timeLineModel.type!!]) {
            NewsAvvisiType.NEWS -> R.drawable.news_news
            NewsAvvisiType.AVVISI_ALLERTE -> R.drawable.news_allerta
        }

        holder.timeline.marker = ContextCompat.getDrawable(context, newsDrawableResId)
    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) :
        RecyclerView.ViewHolder(itemView) {

        val date: TextView = itemView.dateTV
        val category: TextView = itemView.categoryTV
        val message: TextView = itemView.titleTV
        val timeline: TimelineView = itemView.timeline
        val newsContainerLayout: LinearLayout = itemView.newsContainerLayout

        init {
            timeline.initLine(viewType)
        }
    }

}