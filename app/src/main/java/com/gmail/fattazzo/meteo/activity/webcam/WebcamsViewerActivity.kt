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

package com.gmail.fattazzo.meteo.activity.webcam

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.data.webcam.Webcam
import com.gmail.fattazzo.meteo.utils.glide.GlideHelper

/**
 * @author fattazzo
 *
 *
 * date: 13/lug/2015
 */
class WebcamsViewerActivity : AppCompatActivity() {

    companion object {
        const val WEBCAM_EXTRA = "webcamExtra"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_webcams_viewer)

        if (intent.extras?.containsKey(WEBCAM_EXTRA) == true) {
            val webcam = intent.extras!!.getSerializable(WEBCAM_EXTRA) as Webcam

            findViewById<TextView>(R.id.titleTV).text =
                String.format("%s - Ss", webcam.localita.orEmpty(), webcam.descrizione.orEmpty())

            Glide.with(this)
                .load(webcam.link)
                .apply(
                    GlideHelper.createNoCacheOptions(
                        this,
                        true,
                        addTimeOut = false,
                        errorColor = android.R.color.black
                    )
                )
                .into(findViewById(R.id.imageContainer))
        }
    }
}
