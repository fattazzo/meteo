/*
 * Project: meteo
 * File: Fasce.kt
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

package com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Fasce: Serializable {

    @SerializedName("idPrevisioneFascia")
    @Expose
    var idPrevisioneFascia: Int? = null
    @SerializedName("fascia")
    @Expose
    var fascia: String? = null
    @SerializedName("fasciaPer")
    @Expose
    var fasciaPer: String? = null
    @SerializedName("fasciaOre")
    @Expose
    var fasciaOre: String? = null
    @SerializedName("icona")
    @Expose
    var icona: String? = null
    @SerializedName("descIcona")
    @Expose
    var descIcona: String? = null
    @SerializedName("idPrecProb")
    @Expose
    var idPrecProb: String? = null
    @SerializedName("descPrecProb")
    @Expose
    var descPrecProb: String? = null
    @SerializedName("idPrecInten")
    @Expose
    var idPrecInten: String? = null
    @SerializedName("descPrecInten")
    @Expose
    var descPrecInten: String? = null
    @SerializedName("idTempProb")
    @Expose
    var idTempProb: String? = null
    @SerializedName("descTempProb")
    @Expose
    var descTempProb: String? = null
    @SerializedName("idVentoIntQuota")
    @Expose
    var idVentoIntQuota: String? = null
    @SerializedName("descVentoIntQuota")
    @Expose
    var descVentoIntQuota: String? = null
    @SerializedName("idVentoDirQuota")
    @Expose
    var idVentoDirQuota: String? = null
    @SerializedName("descVentoDirQuota")
    @Expose
    var descVentoDirQuota: String? = null
    @SerializedName("idVentoIntValle")
    @Expose
    var idVentoIntValle: String? = null
    @SerializedName("descVentoIntValle")
    @Expose
    var descVentoIntValle: String? = null
    @SerializedName("idVentoDirValle")
    @Expose
    var idVentoDirValle: String? = null
    @SerializedName("descVentoDirValle")
    @Expose
    var descVentoDirValle: String? = null
    @SerializedName("iconaVentoQuota")
    @Expose
    var iconaVentoQuota: String? = null
    @SerializedName("zeroTermico")
    @Expose
    var zeroTermico: Int? = null
    @SerializedName("limiteNevicate")
    @Expose
    var limiteNevicate: Int? = null

    fun getIdIcona(): Int? {
        if(icona.isNullOrBlank()) {
            return null
        }

        return icona?.substringAfterLast('/')?.substringAfter('_')?.substringBefore('.')?.toIntOrNull()
    }

}
