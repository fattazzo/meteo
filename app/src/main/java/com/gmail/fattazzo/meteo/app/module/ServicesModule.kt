/*
 * Project: meteo
 * File: ServicesModule.kt
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
import com.gmail.fattazzo.meteo.data.*
import com.gmail.fattazzo.meteo.preferences.PreferencesService
import com.gmail.fattazzo.meteo.utils.OpenForTesting
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@OpenForTesting
@Module
class ServicesModule {

    @Provides
    @Singleton
    fun preferencesService(context: Context): PreferencesService {
        return PreferencesService(context)
    }

    @Provides
    @Singleton
    fun meteoService(context: Context): MeteoService {
        return MeteoService(context)
    }

    @Provides
    @Singleton
    fun newsService(): NewsService {
        return NewsService()
    }

    @Provides
    @Singleton
    fun versioniService(context: Context, preferencesService: PreferencesService): VersioniService {
        return VersioniService(context, preferencesService)
    }

    @Provides
    @Singleton
    fun webcamService(context: Context, preferencesService: PreferencesService): WebcamService {
        return WebcamService(context, preferencesService)
    }

    @Provides
    @Singleton
    fun stazioniService(context: Context): StazioniService {
        return StazioniService(context)
    }

    @Provides
    @Singleton
    fun radarService(): RadarService {
        return RadarService()
    }
}