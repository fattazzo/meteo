/*
 * Project: meteo
 * File: DialogStazioneMeteoBuilder.kt
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

package com.gmail.fattazzo.meteo.activity.stazioni.meteo.anagrafica

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.data.db.entities.StazioneMeteo
import com.gmail.fattazzo.meteo.utils.dialog.DialogBuilder
import com.gmail.fattazzo.meteo.utils.dialog.DialogType
import java.text.DateFormat
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 08/10/19
 */
object DialogStazioneMeteoBuilder {

    fun build(context: Context, stazioneMeteo: StazioneMeteo): Dialog {

        val dialogMessage = StringBuilder()
        dialogMessage.append("Codice: ").append(stazioneMeteo.codice.orEmpty()).append("\n")
        dialogMessage.append("Nome breve: ").append(stazioneMeteo.nomeBreve.orEmpty()).append("\n")
        dialogMessage.append("Quota: ").append(stazioneMeteo.quota).append("\n")
        dialogMessage.append("Latitudine: ").append(stazioneMeteo.latitudine).append("\n")
        dialogMessage.append("Longitudine: ").append(stazioneMeteo.longitudine).append("\n")
        dialogMessage.append("Est: ").append(stazioneMeteo.est).append("\n")
        dialogMessage.append("Nord: ").append(stazioneMeteo.nord).append("\n")

        val dateFormat = DateFormat.getDateInstance()
        dialogMessage.append("Inizio rilevazioni: ")
            .append(dateFormat.format(stazioneMeteo.startdate)).append("\n")
        stazioneMeteo.enddate?.let {
            dialogMessage.append("Fine rilevazioni: ")
                .append(dateFormat.format(stazioneMeteo.enddate))
        }

        return DialogBuilder(context!!, DialogType.BUTTONS)
            .apply {
                title = stazioneMeteo.nome.orEmpty()
                message = dialogMessage.toString()

                headerIcon = R.drawable.stazione

                positiveText = android.R.string.ok

                negativeText = R.string.street_view
                negativeAction = object : DialogBuilder.OnClickListener {
                    override fun onClick(dialog: Dialog?) {
                        val coordinates = String.format(
                            Locale.ENGLISH,
                            "google.streetview:cbll=%f,%f",
                            stazioneMeteo.latitudine, stazioneMeteo.longitudine
                        )
                        val gmmIntentUri = Uri.parse(coordinates)
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.`package` = "com.google.android.apps.maps"
                        context.startActivity(mapIntent)
                    }
                }

                neutralText = R.string.google_maps
                neutralAction = object : DialogBuilder.OnClickListener {
                    override fun onClick(dialog: Dialog?) {
                        val coordinates = String.format(
                            Locale.ENGLISH,
                            "geo:%f,%f?q=%f,%f(%s)",
                            stazioneMeteo.latitudine,
                            stazioneMeteo.longitudine,
                            stazioneMeteo.latitudine,
                            stazioneMeteo.longitudine,
                            stazioneMeteo.nome + " - " + stazioneMeteo.nomeBreve
                        )
                        val gmmIntentUri = Uri.parse(coordinates)
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.`package` = "com.google.android.apps.maps"
                        context.startActivity(mapIntent)
                    }
                }
            }
            .build()
    }
}