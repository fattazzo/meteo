/*
 * Project: meteo
 * File: ColorPickerPreference.kt
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

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.preference.Preference
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

/**
 * A preference type that allows a user to choose a time.
 *
 * @author Sergey Margaritov
 */
class ColorPickerPreference : Preference, Preference.OnPreferenceClickListener, ColorPickerDialog.OnColorChangedListener {

    private var mView: View? = null
    private var mDialog: ColorPickerDialog? = null

    private var mValue = Color.BLACK

    private var mDensity = 0f

    private var mAlphaSliderEnabled = false

    private var mHexValueEnabled = false

    /**
     * @return preview bitmap
     */
    private// 30dip
    val previewBitmap: Bitmap
        get() {
            val d = (mDensity * 31).toInt()
            val color = mValue
            val bm = Bitmap.createBitmap(d, d, Config.ARGB_8888)
            val w = bm.width
            val h = bm.height
            var c = color
            for (i in 0 until w) {
                for (j in i until h) {
                    c = if (i <= 1 || j <= 1 || i >= w - 2 || j >= h - 2) Color.GRAY else color
                    bm.setPixel(i, j, c)
                    if (i != j) {
                        bm.setPixel(j, i, c)
                    }
                }
            }

            return bm
        }

    /**
     *
     * @author fattazzo
     *
     * date: 13/lug/2015
     */
    private class SavedState : Preference.BaseSavedState {
        internal lateinit var dialogBundle: Bundle

        /**
         * @param source
         * source
         */
        constructor(source: Parcel) : super(source) {
            dialogBundle = source.readBundle(javaClass.classLoader)
        }

        /**
         * @param superState
         * state
         */
        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeBundle(dialogBundle)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState = SavedState(`in`)

                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }

    /**
     * COstruttore.
     *
     * @param context
     * context
     */
    constructor(context: Context) : super(context) {
        init(null)
    }

    /**
     * Costruttore.
     *
     * @param context
     * context
     * @param attrs
     * attributi
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

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
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    /**
     * Init.
     *
     * @param attrs
     * attributes
     */
    private fun init(attrs: AttributeSet?) {
        mDensity = context.resources.displayMetrics.density
        onPreferenceClickListener = this
        if (attrs != null) {
            mAlphaSliderEnabled = attrs.getAttributeBooleanValue(null, "alphaSlider", false)
            mHexValueEnabled = attrs.getAttributeBooleanValue(null, "hexValue", false)
        }
    }

    override fun onBindView(view: View) {
        super.onBindView(view)
        mView = view
        setPreviewColor()
    }

    override fun onColorChanged(color: Int) {
        if (isPersistent) {
            persistInt(color)
        }
        mValue = color
        setPreviewColor()
        try {
            onPreferenceChangeListener.onPreferenceChange(this, color)
        } catch (e: NullPointerException) {
            Log.d(ColorPickerPreference::class.java.name, "NPE on onColorChanged")
        }

    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any = a.getColor(index, Color.BLACK)

    override fun onPreferenceClick(preference: Preference): Boolean {
        showDialog(null)
        return false
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state == null || state !is SavedState) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state)
            return
        }

        val myState = state as SavedState?
        super.onRestoreInstanceState(myState!!.superState)
        showDialog(myState.dialogBundle)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        if (mDialog == null || !mDialog!!.isShowing) {
            return superState
        }

        val myState = SavedState(superState)
        myState.dialogBundle = mDialog!!.onSaveInstanceState()
        return myState
    }

    override fun onSetInitialValue(restoreValue: Boolean, defaultValue: Any?) {
        onColorChanged(if (restoreValue) getPersistedInt(mValue) else defaultValue as Int)
    }

    /**
     * Toggle Alpha Slider visibility (by default it's disabled).
     *
     * @param enable
     * enale
     */
    fun setAlphaSliderEnabled(enable: Boolean) {
        mAlphaSliderEnabled = enable
    }

    /**
     * Toggle Hex Value visibility (by default it's disabled).
     *
     * @param enable
     * enable
     */
    fun setHexValueEnabled(enable: Boolean) {
        mHexValueEnabled = enable
    }

    /**
     * Set preview color.
     */
    private fun setPreviewColor() {
        if (mView == null) {
            return
        }
        val iView = ImageView(context)
        val widgetFrameView = mView!!.findViewById<View>(android.R.id.widget_frame) as LinearLayout
        widgetFrameView.visibility = View.VISIBLE
        widgetFrameView.setPadding(widgetFrameView.paddingLeft, widgetFrameView.paddingTop,
                (mDensity * 8).toInt(), widgetFrameView.paddingBottom)
        // remove already create preview image
        val count = widgetFrameView.childCount
        if (count > 0) {
            widgetFrameView.removeViews(0, count)
        }
        widgetFrameView.addView(iView)
        widgetFrameView.minimumWidth = 0
        iView.setBackgroundDrawable(AlphaPatternDrawable((5 * mDensity).toInt()))
        iView.setImageBitmap(previewBitmap)
    }

    /**
     * Show dialog.
     *
     * @param state
     * state
     */
    private fun showDialog(state: Bundle?) {
        mDialog = ColorPickerDialog(context, mValue)
        mDialog!!.setOnColorChangedListener(this)
        if (mAlphaSliderEnabled) {
            mDialog!!.alphaSliderVisible = true
        }
        if (mHexValueEnabled) {
            mDialog!!.hexValueEnabled = true
        }
        if (state != null) {
            mDialog!!.onRestoreInstanceState(state)
        }
        mDialog!!.show()
    }

    companion object {

        /**
         * For custom purposes. Not used by ColorPickerPreferrence
         *
         * @param color
         * color
         * @return argb color
         */
        fun convertToARGB(color: Int): String {
            var alpha = Integer.toHexString(Color.alpha(color))
            var red = Integer.toHexString(Color.red(color))
            var green = Integer.toHexString(Color.green(color))
            var blue = Integer.toHexString(Color.blue(color))

            if (alpha.length == 1) {
                alpha = "0" + alpha
            }

            if (red.length == 1) {
                red = "0" + red
            }

            if (green.length == 1) {
                green = "0" + green
            }

            if (blue.length == 1) {
                blue = "0" + blue
            }

            return "#" + alpha + red + green + blue
        }

        /**
         * For custom purposes. Not used by ColorPickerPreferrence
         *
         * @param argb
         * argb color
         * @return color
         */
        fun convertToColorInt(argb: String): Int {
            var argb = argb

            if (!argb.startsWith("#")) {
                argb = "#" + argb
            }

            return Color.parseColor(argb)
        }

        /**
         * For custom purposes. Not used by ColorPickerPreference
         *
         * @param color
         * color
         * @return A string representing the hex value of color, without the alpha value
         */
        fun convertToRGB(color: Int): String {
            var red = Integer.toHexString(Color.red(color))
            var green = Integer.toHexString(Color.green(color))
            var blue = Integer.toHexString(Color.blue(color))

            if (red.length == 1) {
                red = "0" + red
            }

            if (green.length == 1) {
                green = "0" + green
            }

            if (blue.length == 1) {
                blue = "0" + blue
            }

            return "#" + red + green + blue
        }
    }
}
