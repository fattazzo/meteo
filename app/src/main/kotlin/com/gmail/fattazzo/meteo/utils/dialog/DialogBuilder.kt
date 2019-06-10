/*
 * Project: meteo
 * File: DialogBuilder.kt
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

package com.gmail.fattazzo.meteo.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.DialogTitle
import androidx.core.content.ContextCompat
import com.gmail.fattazzo.meteo.R


/**
 * @author fattazzo
 *         <p/>
 *         date: 18/04/19
 */
class DialogBuilder(val context: Context, val type: DialogType) {

    interface OnClickListener {
        fun onClick(dialog: Dialog?)
    }

    private val defaultButtonAction: OnClickListener = object : OnClickListener {
        override fun onClick(dialog: Dialog?) {
            if (dialog != null && dialog.isShowing) {
                dialog.dismiss()
            }
        }
    }

    // title & message
    var titleResId: Int = 0
    var title: String? = null
    var messageResId: Int = 0
    var message: String? = null

    // Positive button
    var positiveText: Int = 0
    var positiveAction: OnClickListener? = null
    var positiveColor: Int = R.color.colorPrimaryDark

    // Negative button
    var negativeText: Int = 0
    var negativeAction: OnClickListener? = null
    var negativeColor: Int = android.R.color.holo_red_dark

    // Neutral button
    var neutralText: Int = 0
    var neutralAction: OnClickListener? = null
    var neutralColor: Int = android.R.color.holo_blue_dark

    // Colors
    var headerColor: Int = R.color.colorPrimaryDark
    var footerColor: Int = R.color.border_bg

    // icon
    var headerIcon: Int = R.drawable.help_white
    var headerIconColor: Int = android.R.color.white

    fun build(): Dialog {
        return when (type) {
            DialogType.BUTTONS -> buildButtonDialog()
            DialogType.INDETERMINATE_PROGRESS -> buildIndeterminateDialog()
        }
    }

    private fun buildIndeterminateDialog(): Dialog = buildBaseDialog(R.layout.dialog_indeterminate)

    private fun buildButtonDialog(): Dialog {
        val dialog = buildBaseDialog(R.layout.dialog_buttons)

        if (positiveText != 0) {
            val okButton = dialog.findViewById<Button>(R.id.button1)
            okButton.setText(positiveText)
            okButton.visibility = View.VISIBLE
            okButton.setOnClickListener { (positiveAction ?: defaultButtonAction).onClick(dialog) }
            okButton.setTextColor(ContextCompat.getColor(context, positiveColor))
            assignStrokeColor(dialog, R.id.button1, positiveColor)
        }

        if (negativeText != 0) {
            val cancelButton = dialog.findViewById<Button>(R.id.button2)
            cancelButton.setText(negativeText)
            cancelButton.visibility = View.VISIBLE
            cancelButton.setOnClickListener {
                (negativeAction ?: defaultButtonAction).onClick(dialog)
            }
            cancelButton.setTextColor(ContextCompat.getColor(context, negativeColor))
            assignStrokeColor(dialog, R.id.button2, negativeColor)
        }

        if (neutralText != 0) {
            val neutralButton = dialog.findViewById<Button>(R.id.button3)
            neutralButton.setText(neutralText)
            neutralButton.visibility = View.VISIBLE
            neutralButton.setOnClickListener {
                (neutralAction ?: defaultButtonAction).onClick(dialog)
            }
            neutralButton.setTextColor(ContextCompat.getColor(context, neutralColor))
            assignStrokeColor(dialog, R.id.button3, neutralColor)
        }

        return dialog
    }

    private fun buildBaseDialog(layoutId: Int): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(layoutId)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        assignBgColor(dialog, R.id.topPanel, headerColor)
        assignBgColor(dialog, R.id.buttonPanel, footerColor)

        dialog.findViewById<ImageView>(R.id.icon).setImageResource(headerIcon)
        dialog.findViewById<ImageView>(R.id.icon).setColorFilter(ContextCompat.getColor(context, headerIconColor))

        if (titleResId != 0) {
            dialog.findViewById<DialogTitle>(R.id.alertTitle).setText(titleResId)
        }
        if (title != null) {
            dialog.findViewById<DialogTitle>(R.id.alertTitle).text = title
        }

        if (messageResId != 0) {
            dialog.findViewById<TextView>(R.id.message).setText(messageResId)
        }
        if (message != null) {
            dialog.findViewById<TextView>(R.id.message).text = message
        }
        return dialog
    }

    private fun assignBgColor(dialog: Dialog, viewResId: Int, colorResId: Int) {

        when (val background = dialog.findViewById<View>(viewResId).background) {
            is ShapeDrawable -> background.paint.color = ContextCompat.getColor(context, colorResId)
            is GradientDrawable -> background.setColor(ContextCompat.getColor(context, colorResId))
            is ColorDrawable -> background.color = ContextCompat.getColor(context, colorResId)
        }
    }

    private fun assignStrokeColor(dialog: Dialog, viewResId: Int, colorResId: Int) {

        when (val background = dialog.findViewById<View>(viewResId).background) {
            //is ShapeDrawable -> background.paint.color = ContextCompat.getColor(context, colorResId)
            is GradientDrawable -> background.setStroke(3, ContextCompat.getColor(context, colorResId))
            //is ColorDrawable -> background.color = ContextCompat.getColor(context, colorResId)
        }
    }
}