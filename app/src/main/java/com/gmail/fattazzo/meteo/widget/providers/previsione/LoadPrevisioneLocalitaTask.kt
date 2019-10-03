/*
 * Project: meteo
 * File: LoadPrevisioneLocalitaTask.kt
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

package com.gmail.fattazzo.meteo.widget.providers.previsione

import android.content.Context
import android.os.AsyncTask
import com.gmail.fattazzo.meteo.app.MeteoApplication
import com.gmail.fattazzo.meteo.data.MeteoService
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.PrevisioneLocalita
import com.gmail.fattazzo.meteo.preferences.PreferencesService
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 02/11/17
 */
class LoadPrevisioneLocalitaTask(
    private val context: WeakReference<Context?>,
    private val onComplete: ((previsioneLocalita: PrevisioneLocalita?) -> Unit)? = null
) : AsyncTask<Void, Void, PrevisioneLocalita>() {

    @Inject
    @JvmField
    var meteoService: MeteoService? = null

    @Inject
    @JvmField
    var preferencesService: PreferencesService? = null

    override fun onPreExecute() {
        super.onPreExecute()

        (context.get()?.applicationContext as MeteoApplication).appComponent.inject(this)
    }

    override fun doInBackground(vararg p0: Void?): PrevisioneLocalita? {
        return try {
            meteoService?.caricaPrevisioneLocalita(
                preferencesService?.getNomeLocalitaPreferita() ?: "TRENTO",
                true
            )?.value
        } catch (e: Exception) {
            null
        }
    }

    override fun onCancelled() {
        super.onCancelled()
        onComplete?.invoke(null)
    }

    override fun onCancelled(result: PrevisioneLocalita?) {
        super.onCancelled(result)
        onComplete?.invoke(result)
    }

    override fun onPostExecute(result: PrevisioneLocalita?) {
        super.onPostExecute(result)
        onComplete?.invoke(result)
    }
}