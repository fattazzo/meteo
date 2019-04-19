/*
 * Project: meteo
 * File: ColorPickerDialog.kt
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

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.gmail.fattazzo.meteo.R
import java.util.*

/**
 *
 * @author fattazzo
 *
 * date: 13/lug/2015
 */
class ColorPickerDialog
/**
 * Costruttore.
 *
 * @param context
 * context
 * @param initialColor
 * colore iniziale
 */
(context: Context, initialColor: Int) : Dialog(context), ColorPickerView.OnColorChangedListener, View.OnClickListener {

    private var mColorPicker: ColorPickerView? = null
    private var mOldColor: ColorPickerPanelView? = null

    private var mNewColor: ColorPickerPanelView? = null
    private var mHexVal: EditText? = null
    /**
     * @return HexValueEnabled
     */
    /**
     * @param enable
     * HexValueEnabled
     */
    var hexValueEnabled = false
        set(enable) {
            field = enable
            if (enable) {
                mHexVal!!.visibility = View.VISIBLE
                updateHexLengthFilter()
                updateHexValue(color)
            } else {
                mHexVal!!.visibility = View.GONE
            }
        }

    private var mHexDefaultTextColor: ColorStateList? = null

    private var mListener: OnColorChangedListener? = null

    /**
     * @return slider per la selezione dell'alpha visibile
     */
    /**
     * @param visible
     * `true` per visualizzare la barra per la selezione del canale alpha
     */
    var alphaSliderVisible: Boolean
        get() = mColorPicker!!.alphaSliderVisible
        set(visible) {
            mColorPicker!!.alphaSliderVisible = visible
            if (hexValueEnabled) {
                updateHexLengthFilter()
                updateHexValue(color)
            }
        }

    /**
     * @return colore
     */
    val color: Int
        get() = mColorPicker!!.color

    /**
     *
     * @author fattazzo
     *
     * date: 13/lug/2015
     */
    interface OnColorChangedListener {
        /**
         *
         * @param color
         * color
         */
        fun onColorChanged(color: Int)
    }

    init {

        init(initialColor)
    }

    /**
     * Initializzation.
     *
     * @param color
     * color
     */
    private fun init(color: Int) {
        // To fight color banding.
        window!!.setFormat(PixelFormat.RGBA_8888)

        setUp(color)

    }

    override fun onClick(v: View) {
        if (v.id == R.id.new_color_panel) {
            if (mListener != null) {
                mListener!!.onColorChanged(mNewColor!!.color)
            }
        }
        dismiss()
    }

    override fun onColorChanged(color: Int) {

        mNewColor!!.color = color

        if (hexValueEnabled) {
            updateHexValue(color)
        }

        /*
         * if (mListener != null) { mListener.onColorChanged(color); }
         */

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mOldColor!!.color = savedInstanceState.getInt("old_color")
        mColorPicker!!.setColor(savedInstanceState.getInt("new_color"), true)
    }

    override fun onSaveInstanceState(): Bundle {
        val state = super.onSaveInstanceState()
        state.putInt("old_color", mOldColor!!.color)
        state.putInt("new_color", mNewColor!!.color)
        return state
    }

    /**
     * Set a OnColorChangedListener to get notified when the color selected by the user has changed.
     *
     * @param listener
     * listener
     */
    fun setOnColorChangedListener(listener: OnColorChangedListener) {
        mListener = listener
    }

    /**
     * Setup.
     *
     * @param color
     * color
     */
    @SuppressLint("InflateParams")
    private fun setUp(color: Int) {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val layout = inflater.inflate(R.layout.dialog_color_picker, null)

        setContentView(layout)

        setTitle(R.string.dialog_color_picker)

        mColorPicker = layout.findViewById<View>(R.id.color_picker_view) as ColorPickerView
        mOldColor = layout.findViewById<View>(R.id.old_color_panel) as ColorPickerPanelView
        mNewColor = layout.findViewById<View>(R.id.new_color_panel) as ColorPickerPanelView

        mHexVal = layout.findViewById<View>(R.id.hex_val) as EditText
        mHexVal!!.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        mHexDefaultTextColor = mHexVal!!.textColors

        mHexVal!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = v.context.getSystemService(
                        Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                val s = mHexVal!!.text.toString()
                if (s.length > 5 || s.length < 10) {
                    try {
                        val c = ColorPickerPreference.convertToColorInt(s)
                        mColorPicker!!.setColor(c, true)
                        mHexVal!!.setTextColor(mHexDefaultTextColor)
                    } catch (e: IllegalArgumentException) {
                        mHexVal!!.setTextColor(Color.RED)
                    }

                } else {
                    mHexVal!!.setTextColor(Color.RED)
                }
                return@OnEditorActionListener true
            }
            false
        })

        (mOldColor!!.parent as LinearLayout).setPadding(Math.round(mColorPicker!!.drawingOffset), 0,
                Math.round(mColorPicker!!.drawingOffset), 0)

        mOldColor!!.setOnClickListener(this)
        mNewColor!!.setOnClickListener(this)
        mColorPicker!!.setOnColorChangedListener(this)
        mOldColor!!.color = color
        mColorPicker!!.setColor(color, true)

    }

    /**
     * updateHexLengthFilter.
     */
    private fun updateHexLengthFilter() {
        if (alphaSliderVisible) {
            mHexVal!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(9))
        } else {
            mHexVal!!.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(7))
        }
    }

    /**
     * updateHexValue.
     *
     * @param color
     * color
     */
    private fun updateHexValue(color: Int) {
        if (alphaSliderVisible) {
            mHexVal!!.setText(ColorPickerPreference.convertToARGB(color).toUpperCase(Locale.getDefault()))
        } else {
            mHexVal!!.setText(ColorPickerPreference.convertToRGB(color).toUpperCase(Locale.getDefault()))
        }
        mHexVal!!.setTextColor(mHexDefaultTextColor)
    }
}
