/*
 * Project: meteo
 * File: RadarPageObject.kt
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

package com.gmail.fattazzo.meteo.activity.radar

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.matcher.Matcher.withRecyclerView

/**
 * @author fattazzo
 *         <p/>
 *         date: 11/10/19
 */
object RadarPageObject {

    fun checkRadarRecyclerView(radars: List<RadarModel>) {

        radars.forEachIndexed { index, radar ->

            // Scroll to position
            onView(withId(R.id.radarRecyclerView)).perform(
                scrollToPosition<RadarAdapter.RadarViewHolder>(
                    index
                )
            )

            // check views
            onView(withRecyclerView(R.id.radarRecyclerView).atPositionOnView(index, R.id.titleTV))
                .check(matches(withText(radar.title)))
        }
    }
}