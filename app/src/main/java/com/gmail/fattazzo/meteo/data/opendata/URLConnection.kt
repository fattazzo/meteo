/*
 * Project: meteo
 * File: URLConnection.kt
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

package com.gmail.fattazzo.meteo.data.opendata

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 *
 * @author fattazzo
 *
 * date: 13/lug/2015
 */
object URLConnection {

    private const val TIME_OUT = 15000 // 15sec

    /**
     * Carica l'input stream dall url specificato.
     *
     * @param stringUrl
     * url
     * @return input stream caricato
     * @throws Exception
     * eccezione generica sul caricamento dell'input stream
     */
    fun getInputStreamFromURL(stringUrl: String): InputStream {
        val input = URL(stringUrl)
        val conn = input.openConnection() as HttpURLConnection
        conn.connectTimeout = TIME_OUT
        conn.readTimeout = TIME_OUT

        return conn.inputStream
    }
}
