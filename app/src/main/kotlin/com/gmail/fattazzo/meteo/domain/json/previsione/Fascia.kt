/*
 * Project: meteo
 * File: Fascia.kt
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

package com.gmail.fattazzo.meteo.domain.json.previsione

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/10/17
 */
open class Fascia : Serializable {

    var idPrevisioneFascia: Int? = null

    var fascia: String? = null

    @SerializedName("fasciaPer")
    var descrizione: String? = null

    @SerializedName("fasciaOre")
    var ore: String? = null

    var icona: String? = null

    var descIcona: String? = null

    @SerializedName("idPrecProb")
    var idProbabilitaPrecipitazioni: String? = null

    @SerializedName("descPrecProb")
    var descProbabilitaPrecipitazioni: String? = null

    @SerializedName("idPrecInten")
    var idIntensitaPrecipitazioni: String? = null

    @SerializedName("descPrecInten")
    var descIntensitaPrecipitazioni: String? = null

    @SerializedName("idTempProb")
    var idProbabilitaTemporali: String? = null

    @SerializedName("descTempProb")
    var descProbabilitaTemporali: String? = null

    @SerializedName("idVentoIntQuota")
    var idVentoIntensitaQuota: String? = null

    @SerializedName("descVentoIntQuota")
    var descVentoIntensitaQuota: String? = null

    @SerializedName("idVentoDirQuota")
    var idVentoDirezioneQuota: String? = null

    @SerializedName("descVentoDirQuota")
    var descVentoDirezioneQuota: String? = null

    @SerializedName("idVentoIntValle")
    var idVentoIntensitaValle: String? = null

    @SerializedName("descVentoIntValle")
    var descVentoIntensitaValle: String? = null

    @SerializedName("idVentoDirValle")
    var idVentoDirezioneValle: String? = null

    @SerializedName("descVentoDirValle")
    var descVentoDirezioneValle: String? = null

    var iconaVentoQuota: String? = null

    var zeroTermico: Int? = null
}