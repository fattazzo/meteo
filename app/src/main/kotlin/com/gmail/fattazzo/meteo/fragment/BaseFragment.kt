/*
 * Project: meteo
 * File: BaseFragment.kt
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

package com.gmail.fattazzo.meteo.fragment

import android.app.Dialog
import androidx.fragment.app.Fragment
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.utils.dialog.DialogBuilder
import com.gmail.fattazzo.meteo.utils.dialog.DialogType
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.UiThread


/**
 * @author fattazzo
 *         <p/>
 *         date: 26/10/17
 */
@EFragment
abstract class BaseFragment : Fragment() {

    private var dialog: Dialog? = null

    @UiThread(propagation = UiThread.Propagation.ENQUEUE)
    open fun openIndeterminateDialog(title: String) {
        if (activity != null && (dialog == null || !dialog!!.isShowing)) {
            if (dialog != null) {
                if (dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
                dialog = null
            }

            dialog = DialogBuilder(activity!!, DialogType.INDETERMINATE_PROGRESS)
                    .apply {
                        this.titleResId = R.string.wait
                        message = title
                        headerIcon = R.drawable.loading
                    }
                    .build()
            dialog!!.show()
        }
    }

    @UiThread(propagation = UiThread.Propagation.ENQUEUE)
    open fun closeIndeterminateDialog() {
        if (activity != null && dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
            dialog = null
        }
    }

    open fun backPressed(): Boolean = false

    abstract fun getTitleResId(): Int

    override fun onStop() {
        super.onStop()
        if (activity != null && dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
            dialog = null
        }
    }

    override fun onResume() {
        super.onResume()
        if (getTitleResId() != 0) {
            activity?.setTitle(getTitleResId())
        }
    }
}