/*
 * Project: meteo
 * File: AppRater.kt
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

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.utils.dialog.DialogBuilder
import com.gmail.fattazzo.meteo.utils.dialog.DialogType

/**
 * @author fattazzo
 *
 *
 * date: 29/04/18
 */
object AppRater {

    private const val DAYS_UNTIL_PROMPT = 3 //Min number of days
    private const val LAUNCHES_UNTIL_PROMPT = 3 //Min number of launches

    private const val APPRATE_PREFS_FILE = "apprater"

    private const val DONT_SHOW_AGAIN_PREFS = "dontshowagain"
    private const val LAUNCH_COUNT_PREFS = "launch_count"
    private const val DATA_FIRST_LAUNCH_PREFS = "date_firstlaunch"

    fun rate(mContext: Context, forceOpen: Boolean = false) {
        val prefs = mContext.getSharedPreferences(APPRATE_PREFS_FILE, 0)
        if (!forceOpen && prefs.getBoolean(DONT_SHOW_AGAIN_PREFS, false)) {
            return
        }

        val editor = prefs.edit()

        if (forceOpen) {
            showRateDialog(mContext, editor)
        } else {
            // Increment launch counter
            val launchCount = prefs.getLong(LAUNCH_COUNT_PREFS, 0) + 1
            editor.putLong(LAUNCH_COUNT_PREFS, launchCount)

            // Get date of first launch
            var dateFirstLaunch: Long? = prefs.getLong(DATA_FIRST_LAUNCH_PREFS, 0)
            if (dateFirstLaunch == 0L) {
                dateFirstLaunch = System.currentTimeMillis()
                editor.putLong(DATA_FIRST_LAUNCH_PREFS, dateFirstLaunch)
            }

            // Wait at least n days before opening
            if (launchCount >= LAUNCHES_UNTIL_PROMPT) {
                if (System.currentTimeMillis() >= dateFirstLaunch!! + DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000) {
                    showRateDialog(mContext, editor)
                }
            }
        }

        editor.apply()
    }

    private fun showRateDialog(mContext: Context, editor: SharedPreferences.Editor?) {
        val appName = mContext.getString(R.string.app_name)

        DialogBuilder(mContext, DialogType.BUTTONS)
                .apply {
                    title = mContext.getString(R.string.apprate_rate_app, appName)
                    message = mContext.getString(R.string.apprate_rate_text, appName)

                    positiveText = R.string.apprate_rate
                    positiveAction = object : DialogBuilder.OnClickListener {
                        override fun onClick(dialog: Dialog?) {
                            dialog?.dismiss()
                            mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${mContext.applicationContext.packageName}")))
                        }
                    }

                    negativeText = R.string.apprate_no_thanks
                    negativeAction = object : DialogBuilder.OnClickListener {
                        override fun onClick(dialog: Dialog?) {
                            if (editor != null) {
                                editor.putBoolean(DONT_SHOW_AGAIN_PREFS, true)
                                editor.commit()
                            }
                            dialog?.dismiss()
                        }
                    }

                    neutralText = R.string.apprate_remind_me_later
                }
                .build()
                .show()
    }
}