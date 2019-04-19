/*
 * Project: meteo
 * File: LegendaDatiNeveManager.kt
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

package com.gmail.fattazzo.meteo.fragment.stazioni.valanghe.rilevazioni

import android.content.Context
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.preferences.ApplicationPreferencesManager
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.util.*
import java.util.regex.Pattern

/**
 *
 * @author fattazzo
 *
 * date: 02/feb/2016
 */
@EBean(scope = EBean.Scope.Singleton)
open class LegendaDatiNeveManager {

    @RootContext
    internal lateinit var context: Context

    @Bean
    internal lateinit var preferencesManager: ApplicationPreferencesManager

    internal val mapCondMeteo: Map<String, String> by lazy { getMap(R.array.snow_data_ww) }
    internal val mapNuvolosita: Map<String, String> by lazy { getMap(R.array.snow_data_n) }
    internal val mapVisibilita: Map<String, String> by lazy { getMap(R.array.snow_data_v) }
    internal val mapVentoQuotaTipo: Map<String, String> by lazy { getMap(R.array.snow_data_vq1) }
    internal val mapVentoQuotaFenomeni: Map<String, String> by lazy { getMap(R.array.snow_data_vq2) }
    internal val mapStratoSuperficiale: Map<String, String> by lazy { getMap(R.array.snow_data_cs) }
    internal val mapRugositaSuperficiale: Map<String, String> by lazy { getMap(R.array.snow_data_s) }
    internal val mapBrinaSuperficie: Map<String, String> by lazy { getMap(R.array.snow_data_b) }

    /**
     * Carica l'array con id specificato e ritorna la mappa dei sui valori.
     *
     * @param arrayId
     * id array
     * @return map
     */
    private fun getMap(arrayId: Int): Map<String, String> {
        val nArray = context.resources.getStringArray(arrayId)

        return parseArray(nArray)
    }

    /**
     * Esegue il parse della temperatura.
     * Per temperature negative si aggiunge il valore 50.
     * Alcuni esempi:
     * -5,00° = 55
     * -5,30° = 55
     * -12,70° = 63
     * +0,00° = 00
     * +1,20° = 01
     * +1,80° = 02
     *
     * Rilievo non possibile = //
     * Casi particolari da 0,0° fino a +0,4° si cifra 00, da -0,1° fino a -0,4° si cifra 50.
     *
     * @param temperatura dato della temperatura
     * @return parse
     */
    fun parseTemperatura(temperatura: String?): String {
        var result = context.resources.getString(R.string.snow_section_air_temperature_nodata)

        if (temperatura != null && "//" != temperatura) {
            // Si dovrebbero applicare le formule descritte nel javadoc del metodo per ricavare la temperatura ma
            // nel file sono riportate normalmente
            result = temperatura.trim { it <= ' ' } + "°C"
        }

        return result
    }

    /**
     * Viene letta sull'asta nivometrica e codificata in centimetri.
     *
     * Esempi:
     * 5 cm = 005
     * 8,3 cm = 008
     * 120 cm = 120
     *
     * @param altezzaString altezza in formato string
     * @return parse
     */
    fun parseAltezzaMantoNevoso(altezzaString: String?): String {

        if (altezzaString == null || altezzaString.trim { it <= ' ' }.isEmpty()) {
            return context.resources.getString(R.string.snow_section_air_temperature_nodata)
        }

        val altezza = try {
            Integer.parseInt(altezzaString)
        } catch (e: Exception) {
            0
        }

        return altezza.toString() + " cm"
    }

    /**
     * Neve fresca caduta fra due osservazioni successive e misurata sulla tavoletta.
     * La tavoletta va ripulita dopo ogni misura.
     *
     * Esempi:
     * 20cm = 020
     * 120cm = 120
     * meno di 0,5cm (tracce) = 999
     *
     * Rilievo non possibile = ///
     *
     * Se nelle ore precedenti è piovuto sulla neve, la prima cifra del gruppo sarà 8.
     *
     * Esempi:
     * pioggia su 40cm di neve fresca = 840
     * con meno di 0.5cm (tracce) = 899
     * con più di 98cm di neve fresca = 8//
     *
     * il valore si indicherà nel testo in chiaro
     * assenza di neve fresca ma piove sulla neve = 800
     *
     * @param altezzaString altezza in formato string
     * @return parse
     */
    fun parseAltezzaNeveFresca(altezzaString: String?): String {

        if (altezzaString == null || altezzaString.trim { it <= ' ' }.isEmpty() || "///" == altezzaString.trim { it <= ' ' }) {
            return context.resources.getString(R.string.snow_section_air_temperature_nodata)
        }

        if ("8//" == altezzaString.trim { it <= ' ' }) {
            return "Pioggia con più di 98cm di neve fresca"
        }

        if ("800" == altezzaString.trim { it <= ' ' }) {
            return "Assenza di neve fresca ma piove sulla neve"
        }

        val altezza = try {
            Integer.parseInt(altezzaString)
        } catch (e: Exception) {
            0
        }

        return when {
            altezza == 999 -> "Meno di 0,5cm"
            altezza == 899 -> "Pioggia con meno di 0,5cm di neve fresca"
            altezza > 800 -> "Pioggia su " + (altezza - 800) + "cm di neve fresca"
            else -> (altezza - if (altezza == 0) 0 else 800).toString() + "cm di neve fresca"
        }
    }

    /**
     * Trasforma l'array dei valori della legenda in una mappa.
     *
     * @param arrayOfValues
     * array
     * @return mappa
     */
    private fun parseArray(arrayOfValues: Array<String>): Map<String, String> {
        val resultMap = HashMap<String, String>()

        arrayOfValues
                .map { arrayOfValue -> arrayOfValue.split(Pattern.quote(SEPARATOR).toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray() }
                .forEach { resultMap[it[0]] = it[1] }

        return resultMap
    }

    companion object {

        private const val SEPARATOR = "|"
    }
}
