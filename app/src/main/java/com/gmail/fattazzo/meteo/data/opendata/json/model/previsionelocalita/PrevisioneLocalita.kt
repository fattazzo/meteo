/*
 * Project: meteo
 * File: PrevisioneLocalita.kt
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
import java.util.*

class PrevisioneLocalita {

    @SerializedName("fonte-da-citare")
    @Expose
    var fonteDaCitare: String? = null
    @SerializedName("codice-ipa-titolare")
    @Expose
    var codiceIpaTitolare: String? = null
    @SerializedName("nome-titolare")
    @Expose
    var nomeTitolare: String? = null
    @SerializedName("codice-ipa-editore")
    @Expose
    var codiceIpaEditore: String? = null
    @SerializedName("nome-editore")
    @Expose
    var nomeEditore: String? = null
    @SerializedName("dataPubblicazione")
    @Expose
    var dataPubblicazione: Date? = null
    @SerializedName("idPrevisione")
    @Expose
    var idPrevisione: Int? = null
    @SerializedName("evoluzione")
    @Expose
    var evoluzione: String? = null
    @SerializedName("evoluzioneBreve")
    @Expose
    var evoluzioneBreve: String? = null
    @SerializedName("AllerteList")
    @Expose
    var allerteList: List<Any>? = null
    @SerializedName("previsione")
    @Expose
    var previsione: List<Previsione>? = null

}
