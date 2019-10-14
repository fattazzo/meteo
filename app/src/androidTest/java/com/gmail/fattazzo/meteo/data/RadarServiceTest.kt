/*
 * Project: meteo
 * File: RadarServiceTest.kt
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

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.gmail.fattazzo.meteo.app.services.RadarService
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import java.net.URL


/**
 * @author fattazzo
 *         <p/>
 *         date: 11/10/19
 */
class RadarServiceTest {

    @Test
    fun checkDuplicatedId() {

        val radarService = RadarService()
        val radars = radarService.loadRadars()

        val distinctIds = radars.map { it.id }.distinct()

        assertThat(distinctIds.size, `is`(radars.size))
    }

    @Test
    fun checkConnectionAvaliable() {

        RadarService().loadRadars().forEach {
            try {
                val radarUrl = URL(it.url)
                val connection = radarUrl.openConnection()
                connection.connectTimeout = 30000
                connection.connect()
                println("Radar: ${it.title}. Connection OK")
            } catch (e: Exception) {
                MatcherAssert.assertThat("No connection for radar ${it.title}", false)
            }
        }
    }

    @Test
    fun loadRadars() {

        assertThat(RadarService().loadRadars().size, `is`(Matchers.greaterThan(0)))
    }
}