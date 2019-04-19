/*
 * Project: meteo
 * File: DatiStazione.kt
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

package com.gmail.fattazzo.meteo.domain.xml.stazioni.meteo.datistazione

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * @author fattazzo
 *
 *
 * date: 13/lug/2015
 */
@Root(name = "lastData")
class DatiStazione {

    var codiceStazione: String? = null

    @field:ElementList(name = "temperature_list")
    var temperature: List<TemperaturaAriaStazione>? = null

    @field:ElementList(name = "precipitation_list")
    var precipitazioni: List<PrecipitazioneStazione>? = null

    @field:ElementList(name = "wind_list")
    var venti: List<VentoStazione>? = null

    @field:ElementList(name = "global_radiation_list")
    var radiazioni: List<RadiazioneStazione>? = null

    @field:ElementList(name = "relative_humidity_list")
    var umidita: List<UmiditaStazione>? = null

    @field:ElementList(name = "snow_depth_list")
    var neve: List<NeveStazione>? = null

}
