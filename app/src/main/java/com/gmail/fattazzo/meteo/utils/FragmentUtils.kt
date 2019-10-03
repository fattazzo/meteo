/*
 * Project: meteo
 * File: FragmentUtils.kt
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

package com.gmail.fattazzo.meteo.utils


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.fragment.BaseFragment

/**
 * @author fattazzo
 *
 *
 * date: 20/10/17
 */
class FragmentUtils {

    companion object {

        @JvmOverloads
        fun replace(activity: AppCompatActivity, fragment: Fragment, containerResId: Int = R.id.container) {
            val transaction = activity.supportFragmentManager.beginTransaction()
            transaction.replace(containerResId, fragment, fragment.javaClass.simpleName.replace("_", "")).commit()

            if (fragment is BaseFragment) {
                activity.setTitle(fragment.getTitleResId())
            }
        }

        @JvmOverloads
        fun add(activity: AppCompatActivity, fragment: Fragment, containerResId: Int = R.id.container) {
            val transaction = activity.supportFragmentManager.beginTransaction()
            transaction.add(containerResId, fragment, fragment.javaClass.simpleName.replace("_", "")).commit()
        }
    }
}