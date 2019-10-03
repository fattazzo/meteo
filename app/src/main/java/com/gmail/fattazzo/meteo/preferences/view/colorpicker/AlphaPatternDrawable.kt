/*
 * Project: meteo
 * File: AlphaPatternDrawable.kt
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

package com.gmail.fattazzo.meteo.preferences.view.colorpicker

import android.graphics.*
import android.graphics.Bitmap.Config
import android.graphics.drawable.Drawable

/**
 * This drawable that draws a simple white and gray chessboard pattern. It's pattern you will often see as a background
 * behind a partly transparent image in many applications.
 *
 * @author Daniel Nilsson
 */
class AlphaPatternDrawable
/**
 * Costruttore.
 *
 * @param rectangleSize
 * size
 */
(rectangleSize: Int) : Drawable() {

    private var mRectangleSize = 10

    private val mPaint = Paint()
    private val mPaintWhite = Paint()
    private val mPaintGray = Paint()

    private var numRectanglesHorizontal: Int = 0
    private var numRectanglesVertical: Int = 0

    /**
     * Bitmap in which the pattern will be cahched.
     */
    private var mBitmap: Bitmap? = null

    init {
        mRectangleSize = rectangleSize
        mPaintWhite.color = -0x1
        mPaintGray.color = -0x343435
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(mBitmap!!, null, bounds, mPaint)
    }

    /**
     * This will generate a bitmap with the pattern as big as the rectangle we were allow to draw on. We do this to
     * chache the bitmap so we don't need to recreate it each time draw() is called since it takes a few milliseconds.
     */
    private fun generatePatternBitmap() {

        if (bounds.width() <= 0 || bounds.height() <= 0) {
            return
        }

        mBitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Config.ARGB_8888)
        val canvas = Canvas(mBitmap!!)

        val r = Rect()
        var verticalStartWhite = true
        for (i in 0..numRectanglesVertical) {

            var isWhite = verticalStartWhite
            for (j in 0..numRectanglesHorizontal) {

                r.top = i * mRectangleSize
                r.left = j * mRectangleSize
                r.bottom = r.top + mRectangleSize
                r.right = r.left + mRectangleSize

                canvas.drawRect(r, if (isWhite) mPaintWhite else mPaintGray)

                isWhite = !isWhite
            }

            verticalStartWhite = !verticalStartWhite

        }

    }

    override fun getOpacity(): Int = PixelFormat.UNKNOWN

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)

        val height = bounds.height()
        val width = bounds.width()

        numRectanglesHorizontal = Math.ceil((width / mRectangleSize).toDouble()).toInt()
        numRectanglesVertical = Math.ceil((height / mRectangleSize).toDouble()).toInt()

        generatePatternBitmap()

    }

    override fun setAlpha(alpha: Int) {
        throw UnsupportedOperationException("Alpha is not supported by this drawwable.")
    }

    override fun setColorFilter(cf: ColorFilter?) {
        throw UnsupportedOperationException("ColorFilter is not supported by this drawwable.")
    }

}
