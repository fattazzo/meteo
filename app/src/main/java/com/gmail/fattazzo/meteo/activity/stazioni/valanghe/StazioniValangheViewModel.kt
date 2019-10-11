/*
 * Project: meteo
 * File: StazioniValangheViewModel.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.valanghe

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.fattazzo.meteo.data.StazioniService
import com.gmail.fattazzo.meteo.data.db.entities.StazioneValanghe
import com.gmail.fattazzo.meteo.data.stazioni.valanghe.domain.dati.DatoStazione
import com.gmail.fattazzo.meteo.utils.ioJob
import com.gmail.fattazzo.meteo.utils.uiJob
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 02/10/19
 */
class StazioniValangheViewModel @Inject constructor(private val stazioniService: StazioniService) :
    ViewModel() {

    val stazioniValanghe = MutableLiveData<List<StazioneValanghe>>()

    val codiceStazioneSelezionata = MutableLiveData<String>("")

    val datiStazione = MediatorLiveData<List<DatoStazione>>().apply {
        addSource(codiceStazioneSelezionata) { caricaDatiStazione() }
    }

    fun caricaAnagrafica(forceDownload: Boolean) {

        ioJob {

            val result = stazioniService.caricaAnagraficaStazioniValanghe(forceDownload)

            uiJob {

                stazioniValanghe.postValue(result.value)
            }
        }
    }

    private fun caricaDatiStazione() {

        if (codiceStazioneSelezionata.value.orEmpty().isNotBlank()) {

            ioJob {

                val result = stazioniService.caricaDatiStazioneValanghe()

                uiJob {

                    datiStazione.postValue(result.value)
                }
            }
        }
    }
}