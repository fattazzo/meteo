/*
 * Project: meteo
 * File: TestAppComponent.kt
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

package com.gmail.fattazzo.meteo.app.component

import com.gmail.fattazzo.meteo.activity.BaseActivityTest
import com.gmail.fattazzo.meteo.app.module.AppModule
import com.gmail.fattazzo.meteo.app.module.ServicesModule
import com.gmail.fattazzo.meteo.app.module.viewmodel.ViewModelModule
import dagger.Component
import javax.inject.Singleton

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/10/19
 */
@Singleton
@Component(modules = [AppModule::class, ServicesModule::class, ViewModelModule::class])
interface TestAppComponent : AppComponent {

    fun inject(activity: BaseActivityTest)
}