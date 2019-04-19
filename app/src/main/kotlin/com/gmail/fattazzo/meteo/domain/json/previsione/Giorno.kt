/*
 * Project: meteo
 * File: Giorno.kt
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
import java.util.*

/**
 *
 * @author fattazzo
 *
 * date: 26/ott/2017
 */
class Giorno : Serializable {

    var idPrevisioneGiorno: Int? = null

    @SerializedName("giorno")
    var data: Date? = null

    var idIcona: Int? = null

    var icona: String? = null

    var descIcona: String? = null

    @SerializedName("testoGiorno")
    var testo: String? = null

    @SerializedName("tMinGiorno")
    var temperaturaMin: Int? = null

    @SerializedName("tMaxGiorno")
    var temperaturaMax: Int? = null

    var fasce: List<Fascia>? = null
}