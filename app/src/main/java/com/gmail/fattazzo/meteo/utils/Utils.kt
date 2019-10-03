/*
 * Project: meteo
 * File: Utils.kt
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

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.widget.TextView
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.util.concurrent.atomic.AtomicBoolean


/**
 * @author fattazzo
 *         <p/>
 *         date: 06/12/17
 */
@EBean(scope = EBean.Scope.Singleton)
open class Utils {

    @RootContext
    internal lateinit var context: Context

    /**
     * Open link in external activity.
     *
     * @param link link to open
     */
    fun openLink(link: String?) {
        if (link.orEmpty().isNotBlank()) {
            val i = Intent(Intent.ACTION_VIEW)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.data = Uri.parse(link)
            context.startActivity(i)
        }
    }

    companion object {

        fun justify(textView: TextView) {

            val isJustify = AtomicBoolean(false)

            val textString = textView.text.toString()

            val textPaint = textView.paint

            val builder = SpannableStringBuilder()

            textView.post {
                if (!isJustify.get()) {

                    val lineCount = textView.lineCount
                    val textViewWidth = textView.width

                    for (i in 0 until lineCount) {

                        val lineStart = textView.layout.getLineStart(i)
                        val lineEnd = textView.layout.getLineEnd(i)

                        val lineString = textString.substring(lineStart, lineEnd)

                        if (i == lineCount - 1) {
                            builder.append(SpannableString(lineString))
                            break
                        }

                        val trimSpaceText = lineString.trim { it <= ' ' }
                        val removeSpaceText = lineString.replace(" ".toRegex(), "")

                        val removeSpaceWidth = textPaint.measureText(removeSpaceText)
                        val spaceCount = (trimSpaceText.length - removeSpaceText.length).toFloat()

                        val eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount

                        val spannableString = SpannableString(lineString)
                        for (j in 0 until trimSpaceText.length) {
                            val c = trimSpaceText[j]
                            if (c == ' ') {
                                val drawable = ColorDrawable(0x00ffffff)
                                drawable.setBounds(0, 0, eachSpaceWidth.toInt(), 0)
                                val span = ImageSpan(drawable)
                                spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }
                        }

                        builder.append(spannableString)
                    }

                    textView.text = builder
                    isJustify.set(true)
                }
            }
        }
    }
}