/*
 * Project: meteo
 * File: WebcamsViewerActivity.kt
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

package com.gmail.fattazzo.meteo.fragment.webcam

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.domain.xml.webcam.Webcam
import com.gmail.fattazzo.meteo.utils.glide.GlideHelper
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *
 *
 * date: 13/lug/2015
 */
@EActivity(R.layout.activity_webcams_viewer)
open class WebcamsViewerActivity : AppCompatActivity() {

    @Extra
    internal lateinit var webcam: Webcam

    @ViewById
    internal lateinit var imageContainer: PhotoView

    @ViewById
    internal lateinit var titleTV: TextView

    @AfterViews
    protected fun initViews() {
        titleTV.text = String.format("%s - Ss",webcam.localita.orEmpty(), webcam.descrizione.orEmpty())

        Glide.with(this)
                .load(webcam.link)
                .apply(GlideHelper.createNoCacheOptions(this, true, addTimeOut = false, errorColor = android.R.color.black))
                .into(imageContainer)
    }
}
