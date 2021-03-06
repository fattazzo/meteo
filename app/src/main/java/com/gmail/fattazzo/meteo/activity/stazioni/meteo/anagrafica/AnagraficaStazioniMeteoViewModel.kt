/*
 * Project: meteo
 * File: AnagraficaStazioniMeteoViewModel.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.meteo.anagrafica

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.fattazzo.meteo.app.services.StazioniService
import com.gmail.fattazzo.meteo.data.db.entities.StazioneMeteo
import com.gmail.fattazzo.meteo.utils.ioJob
import com.gmail.fattazzo.meteo.utils.uiJob
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/10/19
 */
class AnagraficaStazioniMeteoViewModel @Inject constructor(private val stazioniService: StazioniService) :
    ViewModel() {

    val caricaDisabilitate = MutableLiveData<Boolean>(false)

    val stazioniMeteo = MediatorLiveData<List<StazioneMeteo>>().apply {
        addSource(caricaDisabilitate) { caricaStazioni(false) }
    }

    val loadingData = ObservableBoolean(false)

    fun caricaStazioni(forceDownload: Boolean) {

        loadingData.set(true)
        ioJob {

            val result =
                stazioniService.caricaAnagraficaStazioniMeteo(
                    caricaDisabilitate.value!!,
                    forceDownload
                )

            uiJob {

                stazioniMeteo.postValue(result.value.orEmpty())
                loadingData.set(false)
            }
        }
    }
}