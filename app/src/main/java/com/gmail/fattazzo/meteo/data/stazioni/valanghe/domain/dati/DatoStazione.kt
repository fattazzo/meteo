/*
 * Project: meteo
 * File: DatoStazione.kt
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

package com.gmail.fattazzo.meteo.data.stazioni.valanghe.domain.dati

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author fattazzo
 *
 * date: 01/feb/2016
 */
@Root(name = "rilievo_neve")
class DatoStazione {

    @field:Element(name = "dataMis")
    var dataMisurazione: Date? = null

    @field:Element(name = "codStaz")
    var codiceStazione: String? = null

    @field:Element(name = "ww")
    var condizioniTempo: String? = null

    @field:Element(name = "n")
    var nuvolosita: String? = null

    @field:Element(name = "v")
    var visibilita: String? = null

    @field:Element(name = "vq1")
    var ventoQuotaTipo: String? = null

    @field:Element(name = "vq2")
    var ventoQuotaLocalizzazione: String? = null

    @field:Element(name = "ta")
    var temperaturaAria: String? = null

    @field:Element(name = "tmin")
    var temperaturaMinima: String? = null

    @field:Element(name = "tmax")
    var temperaturaMassima: String? = null

    @field:Element(name = "hs")
    var altezzaNeve: String? = null

    @field:Element(name = "hn")
    var altezzaNeveFresca: String? = null

    @field:Element(name = "fi")
    var densitaNeve: String? = null

    @field:Element(name = "t10")
    var temperaturaNeve10: String? = null

    @field:Element(name = "t30")
    var temperaturaNeve30: String? = null

    @field:Element(name = "pr")
    var penetrazioneSonda: String? = null

    @field:Element(name = "cs")
    var stratoSuperficiale: String? = null

    @field:Element(name = "s")
    var rugositaSuperficiale: String? = null

    @field:Element(name = "b")
    var brinaSuperficie: String? = null

    companion object {

        const val XML_BEAN_NAME = "rilievo_neve"
        val XMLPROP: List<String> = ArrayList(Arrays.asList("dataMis", "codStaz", "ww", "n", "v", "vq1", "vq2", "ta", "tmin", "tmax", "hs", "hn", "fi", "t10", "t30", "pr", "cs", "s", "b"))

        val DATA_FORMAT: DateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
    }
}
