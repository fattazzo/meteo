/*
 * Project: meteo
 * File: AboutActivityIntentBuilder.kt
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

package com.gmail.fattazzo.meteo.activity.about

import android.content.Context
import android.content.Intent
import com.gmail.fattazzo.aboutlibrary.activity.AboutActivity
import com.gmail.fattazzo.aboutlibrary.builder.AboutButtonBuilder
import com.gmail.fattazzo.aboutlibrary.builder.AboutViewBuilder
import com.gmail.fattazzo.aboutlibrary.builder.Action
import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.changelog.ChangeLogActivity
import com.gmail.fattazzo.meteo.utils.dialog.DialogBuilder
import com.gmail.fattazzo.meteo.utils.dialog.DialogType
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/10/19
 */
object AboutActivityIntentBuilder {

    fun build(context: Context): Intent {

        val aboutViewBuilder = AboutViewBuilder()
            .withInfoUrl(Config.PROJECTS_INFO_URL)
            .withAppId(context.applicationContext.packageName)
            .withFlatStyleButtons(true)
            .withAdditionalAppButtons(
                listOf(
                    buildReportIssueButtonBuilder(),
                    buildChangeLogButtonBuilder()
                )
            )
            .withLang(Locale.getDefault().language)
            .withExcludeThisAppFromProjects(true)

        return Intent(context, AboutActivity::class.java)
            .apply { putExtra(AboutActivity.EXTRA_BUILDER, aboutViewBuilder) }
    }

    private fun buildReportIssueButtonBuilder(): AboutButtonBuilder {
        return AboutButtonBuilder()
            .withText(R.string.report_error)
            .withDrawable(R.drawable.bug)
            .withFlatStyle(true)
            .withAction(object : Action {
                override fun run(context: Context) {
                    DialogBuilder(context, DialogType.BUTTONS).apply {
                        headerIcon = R.drawable.info_white
                        message =
                            "Per segnalare un errore o richiedere nuove funzionalità puoi contattarmi usando il modo che preferisci tra quelli presenti nella sezione 'Autore' di questa pagina."
                        positiveText = android.R.string.ok
                    }.build().show()
                }
            })
            .withTextColor(android.R.color.holo_red_dark)
    }

    private fun buildChangeLogButtonBuilder(): AboutButtonBuilder {
        return AboutButtonBuilder()
            .withText(R.string.changelog)
            .withDrawable(R.drawable.format_list)
            .withFlatStyle(true)
            .withAction(object : Action {
                override fun run(context: Context) {
                    context.startActivity(Intent(context, ChangeLogActivity::class.java))
                }
            })
    }
}