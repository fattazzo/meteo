/*
 * Project: meteo
 * File: IconThemePickerPreference.kt
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

package com.gmail.fattazzo.meteo.preferences.view.iconthemepicker

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.preference.ListPreference
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceViewHolder
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.utils.icons.WeatherIconsFactory
import java.util.*

/**
 * @author fattazzo
 *
 *
 * date: 23/02/16
 */
class IconThemePickerPreference(context: Context, attrs: AttributeSet) :
    ListPreference(context, attrs) {
    private val defaultIconFile: String = "meteo_trentino"

    private var icon1: ImageView? = null
    private var icon2: ImageView? = null
    private var icon3: ImageView? = null

    private var iconName: Array<CharSequence>? = null
    private var icons: MutableList<IconThemeItem>? = null
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private val resources: Resources = context.resources
    private var selectedIconPath: String =
        preferences.getString(context.resources.getString(R.string.pref_key_iconsTheme), null)
            ?: defaultIconFile

    private var summary: TextView? = null

    private fun getEntry(value: String): String {
        val entries = resources.getStringArray(R.array.iconThemeName)
        val values = resources.getStringArray(R.array.iconThemeKey)
        val index = listOf(*values).indexOf(value)
        return try {
            entries[index]
        } catch (e: Exception) {
            entries[0]
        }

    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        val view = holder.itemView

        view.setPadding(50, 50, 50, 50)

        val iconIV = view.findViewById<ImageView>(R.id.icon)
        iconIV.setImageDrawable(icon)

        //selectedIconPath = preferences.getString(context.resources.getString(R.string.pref_key_iconsTheme), defaultIconFile)

        icon1 = view.findViewById(R.id.iconSelected1)
        icon2 = view.findViewById(R.id.iconSelected2)
        icon3 = view.findViewById(R.id.iconSelected3)
        updateIcon()

        summary = view.findViewById(R.id.icon_theme_pref_summary)
        summary!!.text = getEntry(selectedIconPath)
    }

    override fun onClick() {
        val builder = AlertDialog.Builder(context)
        builder.setNegativeButton("Cancel", null)
        builder.setPositiveButton("Ok") { dialog, _ ->
            if (icons != null) {
                for (i in iconName!!.indices) {
                    val item = icons!![i]
                    if (item.isChecked) {

                        persistString(item.path)
                        callChangeListener(item.path)

                        selectedIconPath = item.path
                        updateIcon()

                        summary!!.text = item.name

                        break
                    }
                }
            }
            dialog.dismiss()
        }

        iconName = entries
        val iconPath = entryValues

        check(
            !(iconName == null || iconPath == null
                    || iconName!!.size != iconPath.size)
        ) { "ListPreference requires an entries array " + "and an entryValues array which are both the same length" }

        val selectedIcon = getPersistedString("meteo_trentino")

        /**
         * String selectedIcon = preferences.getString(
         * context.getResources().getString(R.string.pref_key_iconsTheme),
         * "meteo_trentino");
         */

        icons = ArrayList()

        for (i in iconName!!.indices) {
            val isSelected = selectedIcon == iconPath[i]
            val item = IconThemeItem(
                iconName!![i],
                iconPath[i],
                isSelected
            )
            icons!!.add(item)
        }

        val adapter =
            IconThemeListPreferenceAdapter(
                context, R.layout.icon_theme_item_picker, icons!!
            )
        builder.setAdapter(adapter, null)
        builder.create().show()
    }

    private fun updateIcon() {
        val iconsRetriever = WeatherIconsFactory.getIconsRetriever(selectedIconPath)

        icon1!!.setImageResource(iconsRetriever.getIconDemo1())
        icon1!!.tag = selectedIconPath

        icon2!!.setImageResource(iconsRetriever.getIconDemo2())
        icon2!!.tag = selectedIconPath

        icon3!!.setImageResource(iconsRetriever.getIconDemo3())
        icon3!!.tag = selectedIconPath
    }
}