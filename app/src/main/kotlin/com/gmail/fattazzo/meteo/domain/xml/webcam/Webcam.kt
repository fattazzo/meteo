/*
 * Project: meteo
 * File: Webcam.kt
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

package com.gmail.fattazzo.meteo.domain.xml.webcam

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.io.Serializable

/**
 * @author fattazzo
 *
 *
 * date: 13/lug/2015
 */
@Root(name = "webcam")
class Webcam : Serializable {

    @field:Attribute(name = "id")
    var id: Int = 0

    @field:Attribute(name = "localita")
    var localita: String? = null

    @field:Attribute(name = "zona", required = false)
    var descrizione: String? = null

    @field:Element(name = "link")
    var link: String? = null

    @field:Element(name = "ratio", required = false)
    var ratio: String? = "1:1"

    var showInWidget = false

    var favorite = false

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val other = other as Webcam?
        return id == other!!.id
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + id
        return result
    }

    /*
     * @see java.lang.Object#toString()
     */
    override fun toString(): String = "Webcam [descrizione=$descrizione]"

    companion object {

        private const val serialVersionUID = 2716843895314981689L
    }

}
