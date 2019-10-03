/*
 * Project: meteo
 * File: VersioniService.kt
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

package com.gmail.fattazzo.meteo.data

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import android.preference.PreferenceManager
import android.text.Html
import android.util.Log
import com.gmail.fattazzo.meteo.BuildConfig
import com.gmail.fattazzo.meteo.Config
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.activity.changelog.ChangeLogActivity
import com.gmail.fattazzo.meteo.preferences.PreferencesService
import com.gmail.fattazzo.meteo.utils.dialog.DialogBuilder
import com.gmail.fattazzo.meteo.utils.dialog.DialogType
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

/**
 *
 * @author fattazzo
 *
 * date: 13/lug/2015
 */
class VersioniService @Inject constructor(
    private val context: Context,
    private val preferencesService: PreferencesService
) {

    /**
     * @return restituisce l'ultimo numero di versione dell'app
     */
    // non potrà essere rilanciata in quanto il nome versione sarà sempre presente nel manifest
    private val lastAppVersion: String
        get() {
            return try {
                context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: NameNotFoundException) {
                "-1"
            }

        }

    /**
     * @return restituisce la lista del changelog delle versioni
     */
    fun caricaVersioni(): String {

        var result: String

        val lastVersionName = preferencesService.getLastRunVersionName()

        var stream: InputStream? = null
        var inputReader: InputStreamReader? = null
        var reader: BufferedReader? = null
        try {
            stream = context.assets.open("Changelog.txt")
            inputReader = InputStreamReader(stream!!)
            reader = BufferedReader(inputReader)

            result = parseReader(reader, lastVersionName)
        } catch (e: IOException) {
            result = "Changelog non disponibile"
        } finally {
            try {
                if (stream != null) {
                    stream.close()
                }
                if (inputReader != null) {
                    inputReader.close()
                }
                if (reader != null) {
                    reader.close()
                }
            } catch (e: IOException) {
                Log.e(TAG, "Errore durante la chiusura del file")
            }

        }

        return result
    }

    /**
     * @return `true` se l'ultima versione utilizzata dell'app non coincide con l'ultima rilasciata
     */
    fun checkShowVersionChangelog(): Boolean {

        val lastAppVersion = lastAppVersion

        val lastVersionName = preferencesService.getLastRunVersionName()

        return lastVersionName != lastAppVersion
    }

    /**
     * Esegue il parse del reader delle versioni e mette in grassetto tutte le voci delle versioni maggiori di quella
     * passata ocme parametro.
     *
     * @param reader
     * reader
     * @param lastVersionName
     * versione di riferimento
     * @return changelog
     */
    private fun parseReader(reader: BufferedReader, lastVersionName: String): String {
        var result = StringBuilder(1000)

        try {
            var line: String? = null
            while (({ line = reader.readLine(); line }()) != null) {

                if (line!!.startsWith("versione")) {
                    result.append("<u>")
                    result.append(line)
                    result.append("</u>")
                } else {
                    result.append(line)
                }
                result.append("<br>")

            }
        } catch (e: Exception) {
            result = StringBuilder("Changelog non disponibile")
        }

        return result.toString()
    }

    fun showVersionInfo(context: Context) {

        DialogBuilder(context, DialogType.CUSTOM)
            .apply {
                title = "Versione ${BuildConfig.VERSION_NAME}"
                headerIcon = R.drawable.info_white
                customLayout = R.layout.dialog_version_info
                positiveText = android.R.string.ok

                neutralText = R.string.versioni_precedenti
                neutralAction = object : DialogBuilder.OnClickListener {
                    override fun onClick(dialog: Dialog?) {
                        dialog?.dismiss()
                        context.startActivity(Intent(context, ChangeLogActivity::class.java))
                    }
                }
            }
            .build()
            .show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(Config.KEY_LAST_RUN_VERSION_NAME, lastAppVersion)
        editor.apply()
    }

    /**
     * Visualizza il dialog contenente il changelog delle versioni.
     */
    fun showVersionChangelog() {

        DialogBuilder(context, DialogType.BUTTONS)
            .apply {
                title = "Note di rilascio"
                message = Html.fromHtml(caricaVersioni()).toString()

                positiveText = android.R.string.ok
            }
            .build()
            .show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(Config.KEY_LAST_RUN_VERSION_NAME, lastAppVersion)
        editor.apply()
    }

    companion object {

        private const val TAG = "VersioniManager"
    }
}
