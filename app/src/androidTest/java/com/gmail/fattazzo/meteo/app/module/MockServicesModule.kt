/*
 * Project: meteo
 * File: MockServicesModule.kt
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

package com.gmail.fattazzo.meteo.app.module

import android.content.Context
import com.gmail.fattazzo.meteo.app.services.*
import com.gmail.fattazzo.meteo.preferences.PreferencesService
import io.mockk.mockk

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/10/19
 */
class MockServicesModule : ServicesModule() {

    override fun preferencesService(context: Context): PreferencesService {
        return mockk()
    }

    override fun meteoService(context: Context): MeteoService {
        return mockk()
    }

    override fun newsService(): NewsService {
        return mockk()
    }

    override fun versioniService(
        context: Context,
        preferencesService: PreferencesService
    ): VersioniService {
        return mockk()
    }

    override fun webcamService(
        context: Context,
        preferencesService: PreferencesService
    ): WebcamService {
        return mockk()
    }

    override fun stazioniService(context: Context): StazioniService {
        return mockk()
    }
}