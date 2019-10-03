/*
 * Project: meteo
 * File: ImageListPreference.kt
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

package com.gmail.fattazzo.meteo.preferences.view.imagelist

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.preference.ListPreference
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceViewHolder
import com.gmail.fattazzo.meteo.R
import java.util.*

/**
 * @author fattazzo
 *
 *
 * date: 13/lug/2015
 */
class ImageListPreference(context: Context, attrs: AttributeSet) : ListPreference(context, attrs) {

    private var icon: RelativeLayout? = null
    private var iconName: Array<CharSequence>? = null
    private var icons: MutableList<ImageItem>? = null
    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private val resources: Resources = context.resources
    private val defaultImageFile: String = "widget_shape_transparent_40_black"
    private var selectedIconFile: String = preferences.getString(key, null) ?: defaultImageFile

    private var summary: TextView? = null

    /**
     * @param value value
     * @return entry
     */
    private fun getEntry(value: String): String {
        val index = listOf(*entryValues).indexOf(value)
        return entries[index].toString()
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        val view = holder.itemView

        val iconIV = view.findViewById<ImageView>(R.id.icon)
        iconIV.setImageDrawable(getIcon())

        icon = view.findViewById(R.id.iconSelected)
        updateIcon()

        summary = view.findViewById(R.id.summaryTheme)
        summary!!.text = getEntry(selectedIconFile)
    }

    override fun onClick() {
        val builder = AlertDialog.Builder(context)
        builder.setNegativeButton("Cancel", null)
        builder.setPositiveButton("Ok") { dialog, _ ->
            if (icons != null) {
                for (i in iconName!!.indices) {
                    val item = icons!![i]
                    if (item.isChecked) {

                        persistString(item.file)
                        callChangeListener(item.file)

                        selectedIconFile = item.file
                        updateIcon()

                        summary!!.text = item.name

                        break
                    }
                }
            }
            dialog.dismiss()
        }

        iconName = entries
        val iconFile = entryValues

        check(!(iconName == null || iconFile == null || iconName!!.size != iconFile.size)) { "ListPreference requires an entries array " + "and an entryValues array which are both the same length" }

        val selectedIcon = getPersistedString(defaultImageFile)

        icons = ArrayList()

        for (i in iconName!!.indices) {
            val isSelected = selectedIcon == iconFile[i]
            val item = ImageItem(
                iconName!![i],
                iconFile[i],
                isSelected
            )
            icons!!.add(item)
        }

        val customListPreferenceAdapter =
            ImageListPreferenceAdapter(
                context,
                R.layout.preferences_image_list_item_adapter, icons!!
            )
        builder.setAdapter(customListPreferenceAdapter, null)
        builder.create().show()
    }

    /**
     * Update the icon.
     */
    private fun updateIcon() {
        val identifier = resources.getIdentifier(selectedIconFile, "drawable", context.packageName)

        icon!!.setBackgroundResource(identifier)
        icon!!.tag = selectedIconFile
    }
}
