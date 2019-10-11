/*
 * Project: meteo
 * File: MainViewModel.kt
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

package com.gmail.fattazzo.meteo.activity.main

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.fattazzo.meteo.data.MeteoService
import com.gmail.fattazzo.meteo.data.db.entities.Localita
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.PrevisioneLocalita
import com.gmail.fattazzo.meteo.preferences.PreferencesService
import com.gmail.fattazzo.meteo.utils.ioJob
import com.gmail.fattazzo.meteo.utils.uiJob
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/09/19
 */
open class MainViewModel @Inject constructor(
    private var meteoService: MeteoService,
    private var preferencesService: PreferencesService
) : ViewModel() {

    private val TAG = MainViewModel::class.simpleName

    val localita = MutableLiveData<List<Localita>>(null)
    var localitaSelezionata = MutableLiveData<String>()

    val loadingPrevisione: ObservableBoolean = ObservableBoolean(false)
    val loadingLocalita: ObservableBoolean = ObservableBoolean(false)

    val previsioneLocalita = MediatorLiveData<PrevisioneLocalita>().apply {
        addSource(localitaSelezionata) {
            caricaPrevisione(false)
        }
        addSource(localita) {
            caricaPrevisione(false)
        }
    }

    fun caricaPrevisione(forceDownload: Boolean) {
        if (forceDownload || (!loadingPrevisione.get() && localitaSelezionata.value != null && !isCurrentPrevisioneForLocalita(
                localitaSelezionata.value
            )
                    )
        ) {
            loadingPrevisione.set(true)
            ioJob {

                Log.d(TAG, "Carico per la località ${localitaSelezionata.value!!}")
                val result = meteoService.caricaPrevisioneLocalita(localitaSelezionata.value!!)

                uiJob {
                    Log.d(TAG, "Caricata")
                    previsioneLocalita.postValue(result.value)
                    loadingPrevisione.set(false)
                }
            }
        }
    }

    fun caricaLocalita(forceDownload: Boolean) {

        if (!forceDownload && (loadingLocalita.get() || !localita.value.isNullOrEmpty())) {
            return
        }

        loadingLocalita.set(true)
        ioJob {

            val loc = meteoService.caricaLocalita(forceDownload)

            uiJob {
                localita.postValue(loc)

                if (localitaSelezionata.value == null) {
                    localitaSelezionata.postValue(preferencesService.getNomeLocalitaPreferita())
                }

                loadingLocalita.set(false)
            }
        }
    }

    private fun isCurrentPrevisioneForLocalita(nomeLocalita: String?): Boolean {

        if (previsioneLocalita.value == null) {
            return false
        }

        val previsioni = previsioneLocalita.value!!.previsione.orEmpty()

        return previsioni.isNotEmpty() && previsioni[0].localita == nomeLocalita
    }
}