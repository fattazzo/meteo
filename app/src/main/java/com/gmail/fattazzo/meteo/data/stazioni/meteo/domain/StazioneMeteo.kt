/*
 * Project: meteo
 * File: StazioneMeteo.kt
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

package com.gmail.fattazzo.meteo.data.stazioni.meteo.domain

import com.gmail.fattazzo.meteo.data.xml.converters.DateConverter
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import org.simpleframework.xml.convert.Convert
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/11/17
 */
@Root(name = "pointOfMeasureInfo")
class StazioneMeteo {

    @field:Element(name = "code")
    var codice: String? = null

    @field:Element(name = "name")
    var nome: String? = null

    @field:Element(name = "shortname")
    var nomeBreve: String? = null

    @field:Element(name = "elevation")
    var quota: Int? = null

    @field:Element(name = "latitude")
    var latitudine: Double? = null

    @field:Element(name = "longitude")
    var longitudine: Double? = null

    @field:Element(name = "east")
    var est: Double? = null

    @field:Element(name = "north")
    var nord: Double? = null

    @field:Element(name = "startdate")
    @field:Convert(DateConverter::class)
    var startdate: Date? = null

    @field:Element(name = "enddate", required = false)
    @field:Convert(DateConverter::class)
    var enddate: Date? = null
}