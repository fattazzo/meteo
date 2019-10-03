/*
 * Project: meteo
 * File: PrevisioneView.kt
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

package com.gmail.fattazzo.meteo.activity.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.data.opendata.json.model.previsionelocalita.PrevisioneLocalita
import com.gmail.fattazzo.meteo.utils.AnimationUtils
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import org.androidannotations.annotations.res.BooleanRes
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author fattazzo
 *
 *
 * date: 26/10/17
 */
@EViewGroup(R.layout.item_previsione_localita)
open class PrevisioneView : LinearLayout {

    @ViewById
    internal lateinit var dataTV: TextView
    @ViewById
    internal lateinit var evoluzioneTempoTV: TextView

    @ViewById
    internal lateinit var infoImageView: ImageButton

    @ViewById
    internal lateinit var detailLayout: ConstraintLayout

    @ViewById
    internal lateinit var titolareTV: TextView

    @ViewById
    internal lateinit var editoreTV: TextView

    @ViewById
    internal lateinit var citazioneWebView: WebView

    @JvmField
    @BooleanRes(R.bool.show_full_evolution_text)
    var showFullEvolutionText: Boolean = false

    private var previsione: PrevisioneLocalita? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun bind(previsione: PrevisioneLocalita?, roundBorderTop: Boolean, roundBorderBottom: Boolean) {
        this.setBackgroundResource(R.drawable.border_line_left_right)
        if (roundBorderTop) {
            this.setBackgroundResource(R.drawable.border_line_round_top)
        }
        if (roundBorderBottom) {
            this.setBackgroundResource(R.drawable.border_line_round_bottom)
        }

        this.previsione = previsione
        this.visibility = if (previsione == null) View.GONE else View.VISIBLE

        infoImageView.setImageResource(R.drawable.arrow_down)

        if (previsione != null) {
            dataTV.text = SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN).format(previsione.dataPubblicazione)

            val evoluzione = if (showFullEvolutionText)
                previsione.evoluzioneBreve.orEmpty().capitalize() else
                previsione.evoluzioneBreve.orEmpty().substringBefore('.').capitalize() + "..."
            evoluzioneTempoTV.text = evoluzione

            titolareTV.text = previsione.nomeTitolare
            editoreTV.text = previsione.nomeEditore

            citazioneWebView.loadData("<html><center>" + previsione.fonteDaCitare + "</center></html>", "text/html", "UTF-8")
        }
    }

    @Click
    fun rootLayoutClicked() {
        if (detailLayout.visibility == View.GONE) openDetail() else closeDetail()
    }

    private fun closeDetail() {
        infoImageView.startAnimation(AnimationUtils.rotateAnimation(180f, 0f))

        detailLayout.animation = AnimationUtils.fadeAnimation(true)
        detailLayout.animate()
        detailLayout.visibility = View.GONE

        val evoluzione = if (showFullEvolutionText)
            previsione?.evoluzioneBreve.orEmpty().capitalize() else
            previsione?.evoluzioneBreve.orEmpty().substringBefore('.').capitalize() + "..."
        evoluzioneTempoTV.text = evoluzione
    }

    private fun openDetail() {
        infoImageView.startAnimation(AnimationUtils.rotateAnimation(0f, 180f))

        detailLayout.animation = AnimationUtils.fadeAnimation(false)
        detailLayout.visibility = View.VISIBLE
        detailLayout.animate()

        evoluzioneTempoTV.text = previsione?.evoluzioneBreve.orEmpty().capitalize()
    }
}
