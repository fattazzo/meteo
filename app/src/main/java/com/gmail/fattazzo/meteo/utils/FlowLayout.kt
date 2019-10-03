/*
 * Project: meteo
 * File: FlowLayout.kt
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
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.gmail.fattazzo.meteo.R

/**
 *
 * @author fattazzo
 *
 * date: 13/lug/2015
 */
class FlowLayout : ViewGroup {
    private var horizontalSpacing = 0
    private var verticalSpacing = 0
    private var orientation = 0

    private var debugDraw = false

    /**
     *
     * @author fattazzo
     *
     * date: 13/lug/2015
     */
    class LayoutParams : ViewGroup.LayoutParams {
        var x: Int = 0
        var y: Int = 0
        var horizontalSpacing = NO_SPACING
        var verticalSpacing = NO_SPACING
        var newLine = false

        /**
         * Costruttore.
         *
         * @param context
         * context
         * @param attributeSet
         * attributi
         */
        constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
            this.readStyleParameters(context, attributeSet)
        }

        /**
         * Costruttore.
         *
         * @param width
         * larghezza
         * @param height
         * altezza
         */
        constructor(width: Int, height: Int) : super(width, height)

        /**
         * Costruttore.
         *
         * @param layoutParams
         * layoutParams
         */
        constructor(layoutParams: ViewGroup.LayoutParams) : super(layoutParams)

        /**
         * @return horizontalSpacing
         */
        fun horizontalSpacingSpecified(): Boolean {
            return horizontalSpacing != NO_SPACING
        }

        /**
         *
         * @param context
         * context
         * @param attributeSet
         * attribute
         */
        private fun readStyleParameters(context: Context, attributeSet: AttributeSet) {
            val a =
                context.obtainStyledAttributes(attributeSet, R.styleable.FlowLayout_LayoutParams)
            try {
                horizontalSpacing = a.getDimensionPixelSize(
                    R.styleable.FlowLayout_LayoutParams_layout_horizontalSpacing, NO_SPACING
                )
                verticalSpacing = a.getDimensionPixelSize(
                    R.styleable.FlowLayout_LayoutParams_layout_verticalSpacing,
                    NO_SPACING
                )
                newLine = a.getBoolean(R.styleable.FlowLayout_LayoutParams_layout_newLine, false)
            } finally {
                a.recycle()
            }
        }

        /**
         * Set the position.
         *
         * @param paramX
         * x value
         * @param paramY
         * y value
         */
        fun setPosition(paramX: Int, paramY: Int) {
            this.x = paramX
            this.y = paramY
        }

        /**
         * @return verticalSpacing
         */
        fun verticalSpacingSpecified(): Boolean {
            return verticalSpacing != NO_SPACING
        }

        companion object {
            private const val NO_SPACING = -1
        }
    }

    /**
     * Costruttore.
     *
     * @param context
     * context
     */
    constructor(context: Context) : super(context) {

        this.readStyleParameters(context, null)
    }

    /**
     * Costruttore.
     *
     * @param context
     * context
     * @param attributeSet
     * attributi
     */
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {

        this.readStyleParameters(context, attributeSet)
    }

    /**
     * Costruttore.
     *
     * @param context
     * context
     * @param attributeSet
     * attributi
     * @param defStyle
     * style
     */
    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {

        this.readStyleParameters(context, attributeSet)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    /**
     * @param color
     * color
     * @return paint
     */
    private fun createPaint(color: Int): Paint {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = color
        paint.strokeWidth = 2.0f
        return paint
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        val more = super.drawChild(canvas, child, drawingTime)
        this.drawDebugInfo(canvas, child)
        return more
    }

    /**
     *
     * @param canvas
     * canvas
     * @param child
     * child
     */
    private fun drawDebugInfo(canvas: Canvas, child: View) {
        if (!debugDraw) {
            return
        }

        val childPaint = this.createPaint(-0x100)
        val layoutPaint = this.createPaint(-0xff0100)
        val newLinePaint = this.createPaint(-0x10000)

        val lp = child.layoutParams as LayoutParams

        if (lp.horizontalSpacing > 0) {
            val x = child.right.toFloat()
            val y = child.top + child.height / 2.0f
            canvas.drawLine(x, y, x + lp.horizontalSpacing, y, childPaint)
            canvas.drawLine(
                x + lp.horizontalSpacing - 4.0f,
                y - 4.0f,
                x + lp.horizontalSpacing,
                y,
                childPaint
            )
            canvas.drawLine(
                x + lp.horizontalSpacing - 4.0f,
                y + 4.0f,
                x + lp.horizontalSpacing,
                y,
                childPaint
            )
        } else if (this.horizontalSpacing > 0) {
            val x = child.right.toFloat()
            val y = child.top + child.height / 2.0f
            canvas.drawLine(x, y, x + this.horizontalSpacing, y, layoutPaint)
            canvas.drawLine(
                x + this.horizontalSpacing - 4.0f,
                y - 4.0f,
                x + this.horizontalSpacing,
                y,
                layoutPaint
            )
            canvas.drawLine(
                x + this.horizontalSpacing - 4.0f,
                y + 4.0f,
                x + this.horizontalSpacing,
                y,
                layoutPaint
            )
        }

        if (lp.verticalSpacing > 0) {
            val x = child.left + child.width / 2.0f
            val y = child.bottom.toFloat()
            canvas.drawLine(x, y, x, y + lp.verticalSpacing, childPaint)
            canvas.drawLine(
                x - 4.0f,
                y + lp.verticalSpacing - 4.0f,
                x,
                y + lp.verticalSpacing,
                childPaint
            )
            canvas.drawLine(
                x + 4.0f,
                y + lp.verticalSpacing - 4.0f,
                x,
                y + lp.verticalSpacing,
                childPaint
            )
        } else if (this.verticalSpacing > 0) {
            val x = child.left + child.width / 2.0f
            val y = child.bottom.toFloat()
            canvas.drawLine(x, y, x, y + this.verticalSpacing, layoutPaint)
            canvas.drawLine(
                x - 4.0f,
                y + this.verticalSpacing - 4.0f,
                x,
                y + this.verticalSpacing,
                layoutPaint
            )
            canvas.drawLine(
                x + 4.0f,
                y + this.verticalSpacing - 4.0f,
                x,
                y + this.verticalSpacing,
                layoutPaint
            )
        }

        if (lp.newLine) {
            if (orientation == HORIZONTAL) {
                val x = child.left.toFloat()
                val y = child.top + child.height / 2.0f
                canvas.drawLine(x, y - 6.0f, x, y + 6.0f, newLinePaint)
            } else {
                val x = child.left + child.width / 2.0f
                val y = child.top.toFloat()
                canvas.drawLine(x - 6.0f, y, x + 6.0f, y, newLinePaint)
            }
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun generateLayoutParams(attributeSet: AttributeSet): LayoutParams {
        return LayoutParams(context, attributeSet)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): LayoutParams {
        return LayoutParams(p)
    }

    /**
     * @param lp
     * layout params
     * @return h spacing
     */
    private fun getHorizontalSpacing(lp: LayoutParams): Int {
        return if (lp.horizontalSpacingSpecified()) {
            lp.horizontalSpacing
        } else {
            horizontalSpacing
        }
    }

    /**
     * @param lp
     * layout params
     * @return v spacing
     */
    private fun getVerticalSpacing(lp: LayoutParams): Int {
        return if (lp.verticalSpacingSpecified()) {
            lp.verticalSpacing
        } else {
            verticalSpacing
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            val lp = child.layoutParams as LayoutParams
            child.layout(lp.x, lp.y, lp.x + child.measuredWidth, lp.y + child.measuredHeight)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeWidth =
            MeasureSpec.getSize(widthMeasureSpec) - this.paddingRight - this.paddingLeft
        val sizeHeight =
            MeasureSpec.getSize(heightMeasureSpec) - this.paddingTop - this.paddingBottom

        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)

        val size: Int
        val mode: Int

        if (orientation == HORIZONTAL) {
            size = sizeWidth
            mode = modeWidth
        } else {
            size = sizeHeight
            mode = modeHeight
        }

        var lineThicknessWithSpacing = 0
        var lineThickness = 0
        var lineLengthWithSpacing = 0
        var lineLength: Int

        var prevLinePosition = 0

        var controlMaxLength = 0
        var controlMaxThickness = 0

        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }

            val lp = child.layoutParams as LayoutParams

            child.measure(
                getChildMeasureSpec(
                    widthMeasureSpec,
                    this.paddingLeft + this.paddingRight,
                    lp.width
                ),
                getChildMeasureSpec(
                    heightMeasureSpec,
                    this.paddingTop + this.paddingBottom,
                    lp.height
                )
            )

            val hSpacing = this.getHorizontalSpacing(lp)
            val vSpacing = this.getVerticalSpacing(lp)

            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            val childLength: Int
            val childThickness: Int
            val spacingLength: Int
            val spacingThickness: Int

            if (orientation == HORIZONTAL) {
                childLength = childWidth
                childThickness = childHeight
                spacingLength = hSpacing
                spacingThickness = vSpacing
            } else {
                childLength = childHeight
                childThickness = childWidth
                spacingLength = vSpacing
                spacingThickness = hSpacing
            }

            lineLength = lineLengthWithSpacing + childLength
            lineLengthWithSpacing = lineLength + spacingLength

            val newLine = lp.newLine || mode != MeasureSpec.UNSPECIFIED && lineLength > size
            if (newLine) {
                prevLinePosition += lineThicknessWithSpacing

                lineThickness = childThickness
                lineLength = childLength
                lineThicknessWithSpacing = childThickness + spacingThickness
                lineLengthWithSpacing = lineLength + spacingLength
            }

            lineThicknessWithSpacing =
                lineThicknessWithSpacing.coerceAtLeast(childThickness + spacingThickness)
            lineThickness = lineThickness.coerceAtLeast(childThickness)

            val posX: Int
            val posY: Int
            if (orientation == HORIZONTAL) {
                posX = paddingLeft + lineLength - childLength
                posY = paddingTop + prevLinePosition
            } else {
                posX = paddingLeft + prevLinePosition
                posY = paddingTop + lineLength - childHeight
            }
            lp.setPosition(posX, posY)

            controlMaxLength = controlMaxLength.coerceAtLeast(lineLength)
            controlMaxThickness = prevLinePosition + lineThickness
        }

        /* need to take paddings into account */
        if (orientation == HORIZONTAL) {
            controlMaxLength += paddingLeft + paddingRight
            controlMaxThickness += paddingBottom + paddingTop
        } else {
            controlMaxLength += paddingBottom + paddingTop
            controlMaxThickness += paddingLeft + paddingRight
        }

        if (orientation == HORIZONTAL) {
            this.setMeasuredDimension(
                View.resolveSize(controlMaxLength, widthMeasureSpec),
                View.resolveSize(controlMaxThickness, heightMeasureSpec)
            )
        } else {
            this.setMeasuredDimension(
                View.resolveSize(controlMaxThickness, widthMeasureSpec),
                View.resolveSize(controlMaxLength, heightMeasureSpec)
            )
        }
    }

    /**
     *
     * @param context
     * context
     * @param attributeSet
     * attribute
     */
    private fun readStyleParameters(context: Context, attributeSet: AttributeSet?) {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.FlowLayout)
        try {
            horizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, 0)
            verticalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, 0)
            orientation = a.getInteger(R.styleable.FlowLayout_orientations, HORIZONTAL)
            debugDraw = a.getBoolean(R.styleable.FlowLayout_debugDraw, false)
        } finally {
            a.recycle()
        }
    }

    companion object {

        const val HORIZONTAL = 0
    }
}
