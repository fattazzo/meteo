/*
 * Project: meteo
 * File: ColorPickerPanelView.kt
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

package com.gmail.fattazzo.meteo.preferences.view.colorpicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * This class draws a panel which which will be filled with a color which can be set. It can be used to show the
 * currently selected color which you will get from the [ColorPickerView].
 *
 * @author Daniel Nilsson
 */
class ColorPickerPanelView
/**
 * Costruttore.
 *
 * @param context
 * context
 * @param attrs
 * attributi
 * @param defStyle
 * style
 */
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    private var mDensity = 1f

    /**
     * @return Get the color of the border surrounding the panel.
     */
    /**
     * Set the color of the border surrounding the panel.
     *
     * @param color
     * colore
     */
    private var borderColor = -0x919192
        set(color) {
            field = color
            invalidate()
        }
    /**
     * @return Get the color currently show by this view.
     */
    /**
     * Set the color that should be shown by this view.
     *
     * @param color
     * colore
     */
    var color = -0x1000000
        set(color) {
            field = color
            invalidate()
        }

    private var mBorderPaint: Paint? = null
    private var mColorPaint: Paint? = null

    private var mDrawingRect: RectF? = null
    private var mColorRect: RectF? = null

    private var mAlphaPattern: AlphaPatternDrawable? = null

    init {
        init()
    }

    /**
     * Init.
     */
    private fun init() {
        mBorderPaint = Paint()
        mColorPaint = Paint()
        mDensity = context.resources.displayMetrics.density
    }

    override fun onDraw(canvas: Canvas) {

        val rect = mColorRect

        if (BORDER_WIDTH_PX > 0) {
            mBorderPaint!!.color = borderColor
            canvas.drawRect(mDrawingRect!!, mBorderPaint!!)
        }

        if (mAlphaPattern != null) {
            mAlphaPattern!!.draw(canvas)
        }

        mColorPaint!!.color = color

        canvas.drawRect(rect!!, mColorPaint!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mDrawingRect = RectF()
        mDrawingRect!!.left = paddingLeft.toFloat()
        mDrawingRect!!.right = (w - paddingRight).toFloat()
        mDrawingRect!!.top = paddingTop.toFloat()
        mDrawingRect!!.bottom = (h - paddingBottom).toFloat()

        setUpColorRect()

    }

    /**
     * Setup color.
     */
    private fun setUpColorRect() {
        val dRect = mDrawingRect

        val left = dRect!!.left + BORDER_WIDTH_PX
        val top = dRect.top + BORDER_WIDTH_PX
        val bottom = dRect.bottom - BORDER_WIDTH_PX
        val right = dRect.right - BORDER_WIDTH_PX

        mColorRect = RectF(left, top, right, bottom)

        mAlphaPattern = AlphaPatternDrawable((5 * mDensity).toInt())

        mAlphaPattern!!.setBounds(Math.round(mColorRect!!.left), Math.round(mColorRect!!.top), Math.round(mColorRect!!.right),
                Math.round(mColorRect!!.bottom))
    }

    companion object {

        /**
         * The width in pixels of the border surrounding the color panel.
         */
        private const val BORDER_WIDTH_PX = 1f
    }
}
/**
 * Costruttore.
 *
 * @param context
 * context
 */
/**
 * Costruttore.
 *
 * @param context
 * context
 * @param attrs
 * attributi
 */
