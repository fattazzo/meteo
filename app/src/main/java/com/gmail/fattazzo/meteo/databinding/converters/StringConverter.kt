/*
 * Project: meteo
 * File: StringConverter.kt
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

package com.gmail.fattazzo.meteo.databinding.converters

import android.annotation.SuppressLint
import androidx.annotation.Nullable
import java.text.SimpleDateFormat
import java.util.*

object StringConverter {

    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun capitalize(@Nullable value: String?): String = value.orEmpty().capitalize()

    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun dateToString(@Nullable value: Date?): String {

        return if (value == null) {
            ""
        } else {
            try {
                val dateFormat = SimpleDateFormat("EEEE dd/MM/yyyy", Locale.ITALIAN)
                dateFormat.format(value).capitalize()
            } catch (e: Exception) {
                ""
            }
        }
    }
}