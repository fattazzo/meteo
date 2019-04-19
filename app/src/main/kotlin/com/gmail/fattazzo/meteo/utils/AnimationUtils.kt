/*
 * Project: meteo
 * File: AnimationUtils.kt
 *
 * Created by fattazzo
 * Copyright © 2018 Gianluca Fattarsi. All rights reserved.
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

package com.gmail.fattazzo.meteo.utils

import android.view.animation.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 27/10/17
 */
class AnimationUtils {

    companion object {

        fun rotateAnimation(fromDegrees : Float, toDegrees : Float, duration : Long = 500) : Animation {
            val rotate: Animation = RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotate.startOffset = 0
            rotate.interpolator = LinearInterpolator()
            rotate.duration = duration
            rotate.fillAfter = true
            return rotate
        }

        fun fadeAnimation(out : Boolean, duration: Long = 500) : Animation {
            val fade = if(out) AlphaAnimation(1f, 0f) else AlphaAnimation(0f, 1f)
            fade.interpolator = AccelerateInterpolator()
            fade.startOffset = 0
            fade.duration = duration
            return fade
        }
    }
}