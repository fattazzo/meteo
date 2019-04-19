/*
 * Project: meteo
 * File: FasciaView.kt
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

package com.gmail.fattazzo.meteo.fragment.home.dettaglio.fasciasection

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.domain.json.previsione.Fascia
import com.gmail.fattazzo.meteo.domain.json.previsione.Giorno
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/10/17
 */
@EViewGroup(R.layout.item_fascia)
open class FasciaView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @ViewById
    internal lateinit var descrizioneTV: TextView

    @ViewById
    internal lateinit var descrizioneIconaTV: TextView

    @ViewById
    internal lateinit var probPrecTV: TextView
    @ViewById
    internal lateinit var intensitaPrecTV: TextView
    @ViewById
    internal lateinit var descTemporaliTV: TextView

    @ViewById
    internal lateinit var ventoValleDescTV: TextView
    @ViewById
    internal lateinit var ventoQuotaDescTV: TextView

    @ViewById
    internal lateinit var massimaTV: TextView
    @ViewById
    internal lateinit var minimaTV: TextView
    @ViewById
    internal lateinit var zeroTermicoTV: TextView

    @ViewById
    internal lateinit var headerLayout: LinearLayout
    @ViewById
    internal lateinit var temperatureLayout: ConstraintLayout
    @ViewById
    internal lateinit var precipitazioniLayout: ConstraintLayout
    @ViewById
    internal lateinit var ventiLayout: ConstraintLayout

    fun bind(giorno: Giorno, fascia: Fascia, sections: Sections) {

        headerLayout.visibility = View.GONE
        temperatureLayout.visibility = View.GONE
        precipitazioniLayout.visibility = View.GONE
        ventiLayout.visibility = View.GONE

        when (sections) {
            Sections.HEADER -> {
                headerLayout.visibility = View.VISIBLE
                descrizioneIconaTV.text = fascia.descIcona.orEmpty()
                descrizioneTV.text = String.format("(ore %s)", fascia.ore)
            }
            Sections.TEMPERATURE -> {
                temperatureLayout.visibility = View.VISIBLE
                massimaTV.text = String.format("%d°C", giorno.temperaturaMax)
                minimaTV.text = String.format("%d°C", giorno.temperaturaMin)
                zeroTermicoTV.text = if (fascia.zeroTermico == null) "--" else "${fascia.zeroTermico} metri"
            }
            Sections.PRECIPITAZIONI -> {
                precipitazioniLayout.visibility = View.VISIBLE
                probPrecTV.text = fascia.descProbabilitaPrecipitazioni.orEmpty()
                intensitaPrecTV.text = fascia.descIntensitaPrecipitazioni.orEmpty()
                descTemporaliTV.text = fascia.descProbabilitaTemporali.orEmpty()
            }
            Sections.VENTO -> {
                ventiLayout.visibility = View.VISIBLE
                val sbQuota = StringBuilder()
                if (fascia.descVentoIntensitaQuota.orEmpty().isNotBlank()) {
                    sbQuota.append(fascia.descVentoIntensitaQuota)
                }
                if (fascia.descVentoDirezioneQuota.orEmpty().isNotBlank()) {
                    sbQuota.append("\nDirezione ").append(fascia.descVentoDirezioneQuota)
                }
                ventoQuotaDescTV.text = sbQuota.toString()

                val sbValle = StringBuilder()
                if (fascia.descVentoIntensitaValle.orEmpty().isNotBlank()) {
                    sbValle.append(fascia.descVentoIntensitaValle)
                }
                if (fascia.descVentoDirezioneValle.orEmpty().isNotBlank()) {
                    sbValle.append("\nDirezione ").append(fascia.descVentoDirezioneValle)
                }
                ventoValleDescTV.text = sbValle.toString()
            }
        }

    }

}