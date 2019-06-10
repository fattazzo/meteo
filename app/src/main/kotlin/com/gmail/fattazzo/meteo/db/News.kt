/*
 * Project: meteo
 * File: News.kt
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

package com.gmail.fattazzo.meteo.db

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import com.gmail.fattazzo.meteo.newsavvisi.NewsAvvisiType
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 29/04/19
 */
class News : Model() {

    @Column
    var date: Date? = null

    @Column
    var category: String? = null

    @Column
    var title: String? = null

    @Column
    var url: String? = null

    @Column
    var type: Int? = null

    companion object {

        fun loadAll(types: List<NewsAvvisiType> = listOf()): List<News> {

            var query = Select().from(News::class.java)

            types.forEach {
                query = query.or("type == ?",it.ordinal)
            }

            return query.orderBy("date desc").execute()
        }

        fun deleteAll(newsAvvisiType: NewsAvvisiType) {
            Delete().from(News::class.java).where("type = ?", newsAvvisiType.ordinal).execute<News>()
        }
    }
}