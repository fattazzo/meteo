/*
 * Project: meteo
 * File: DettaglioGiornoActivityTest.kt
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

package com.gmail.fattazzo.meteo.activity.main.dettaglio

import android.content.Intent
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.rule.ActivityTestRule
import com.gmail.fattazzo.meteo.activity.BaseActivityTest
import com.gmail.fattazzo.meteo.config.PrevisioneConfig
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.Giorni
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.PrevisioneLocalita
import com.gmail.fattazzo.meteo.data.opendata.previsione.PrevisioneLocalitaMockData
import org.hamcrest.CoreMatchers.`is`
import org.junit.Rule
import org.junit.Test

/**
 * @author fattazzo
 *         <p/>
 *         date: 11/10/19
 */
class DettaglioGiornoActivityTest : BaseActivityTest() {

    @Rule
    @JvmField
    var rule: ActivityTestRule<DettaglioGiornoActivity> =
        ActivityTestRule(DettaglioGiornoActivity::class.java, true, false)

    private fun getGiorni(): List<Giorni> =
        PrevisioneLocalitaMockData.fromJson<PrevisioneLocalita>(PrevisioneConfig.DATA_OK_TRENTO_10_10_2019).previsione!![0].giorni!!

    private fun openDettaglioGiorno(giorno: Giorni) {

        val intent = Intent(getTargetApplication(), DettaglioGiornoActivity::class.java)
        intent.putExtra(DettaglioGiornoActivity.GIORNO_EXTRA, giorno)

        rule.launchActivity(intent)
    }

    @Test
    fun verificaFasce() {
        getGiorni().forEach {
            openDettaglioGiorno(it)
            DettaglioGiornoPageObject.checkFasceView(it)
            rule.finishActivity()
        }
    }

    @Test
    fun verificaTitolo() {
        getGiorni().forEach {
            openDettaglioGiorno(it)
            assertThat(rule.activity.viewModel.giorno.value!!.data, `is`(it.data))
            DettaglioGiornoPageObject.checkDataView(it)
            rule.finishActivity()
        }
    }

    @Test
    fun selezioneFascia() {

        val giorno = getGiorni()[1]

        openDettaglioGiorno(giorno)

        giorno.fasce!!.forEachIndexed { index, fascia ->
            DettaglioGiornoPageObject.clickFasciaHeader(index)
            DettaglioGiornoPageObject.checkDatiFasciaView(giorno, fascia)
        }
    }
}