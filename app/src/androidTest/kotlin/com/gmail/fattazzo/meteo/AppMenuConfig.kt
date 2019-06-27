/*
 * Project: meteo
 * File: AppMenu.kt
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

package com.gmail.fattazzo.meteo

/**
 * @author fattazzo
 *         <p/>
 *         date: 24/06/19
 */
enum class AppMenuConfig(val resTextId: Int, val resId: Int) {
    HOME(R.string.nav_home,R.id.nav_home),
    BOLL_PROB(R.string.nav_boll_prob,R.id.nav_boll_prob),
    NEWS(R.string.nav_news_e_allerte,R.id.nav_news_e_allerte),
    STAZIONI(R.string.nav_stazioni_meteo,R.id.nav_stazioni_meteo),
    NEVE_VALANGHE(R.string.nav_stazioni_neve,R.id.nav_stazioni_neve),
    RADAR(R.string.nav_radar,R.id.nav_radar),
    WEBCAM(R.string.nav_webcam,R.id.nav_webcam),
    IMPOSTAZIONI(R.string.nav_preferences,R.id.nav_preferences),
    VERSION_INFO(R.string.nav_version_info,R.id.nav_version_info),
    ABOUT(R.string.nav_about,R.id.nav_about)
}