/*
 * Project: meteo
 * File: GlideHelper.kt
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

package com.gmail.fattazzo.meteo.utils.glide

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.util.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 20/04/19
 */
object GlideHelper {

    private fun createProgressDrawable(context: Context): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        return circularProgressDrawable
    }

    fun createOptions(context: Context, addLoadingPlaceholder: Boolean, addTimeOut: Boolean, errorDrawable: Int = 0, errorColor: Int = 0): RequestOptions {
        var options = RequestOptions()

        if (addLoadingPlaceholder) {
            options = options.placeholder(createProgressDrawable(context))
        }

        if(errorDrawable != 0) {
            options = options.error(ContextCompat.getDrawable(context, errorDrawable))
        }

        if(errorColor != 0) {
            options = options.error(ColorDrawable(errorColor))
        }

        if (addTimeOut) {
            options = options.timeout(10 * 1000)
        }

        return options
    }

    fun createNoCacheOptions(context: Context, addLoadingPlaceholder: Boolean, addTimeOut: Boolean, errorDrawable: Int = 0, errorColor: Int = 0): RequestOptions {
        var options = createOptions(context, addLoadingPlaceholder, addTimeOut, errorDrawable,errorColor)

        options = options.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)

        return options
    }

    fun createDayCacheOptions(context: Context, addLoadingPlaceholder: Boolean, addTimeOut: Boolean, errorDrawable: Int = 0): RequestOptions {
        var options = createOptions(context, addLoadingPlaceholder, addTimeOut, errorDrawable)

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        options = options.signature { day }

        return options
    }
}