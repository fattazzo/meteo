/*
 * Project: meteo
 * File: IconThemeListPreferenceAdapter.kt
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.utils.icons.WeatherIconsFactory

/**
 * @author fattazzo
 *
 *
 * date: 23/02/16
 */
class IconThemeListPreferenceAdapter(
    context: Context,
    private val resource: Int,
    private val icons: List<IconThemeItem>
) : ArrayAdapter<IconThemeItem>(context, resource, icons) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertViewAdapter: View

        val holder: IconThemeViewHolder
        if (convertView == null) {
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewAdapter = inflater.inflate(resource, parent, false)

            holder = IconThemeViewHolder()
            holder.themeName = convertViewAdapter
                .findViewById<View>(R.id.icon_theme_name) as TextView
            holder.image1 = convertViewAdapter
                .findViewById<View>(R.id.icon_theme_image1) as ImageView
            holder.image2 = convertViewAdapter
                .findViewById<View>(R.id.icon_theme_image2) as ImageView
            holder.image3 = convertViewAdapter
                .findViewById<View>(R.id.icon_theme_image3) as ImageView
            holder.radioButton = convertViewAdapter
                .findViewById<View>(R.id.icon_theme_radio) as RadioButton

            convertViewAdapter.tag = holder
        } else {
            convertViewAdapter = convertView
            holder = convertViewAdapter.tag as IconThemeViewHolder
        }

        holder.themeName!!.text = icons[position].name

        val iconsRetriever = WeatherIconsFactory.getIconsRetriever(icons[position].path)

        holder.image1!!.setImageResource(iconsRetriever.getIconDemo1())
        holder.image2!!.setImageResource(iconsRetriever.getIconDemo2())
        holder.image3!!.setImageResource(iconsRetriever.getIconDemo3())

        holder.radioButton!!.isChecked = icons[position].isChecked

        convertViewAdapter.setOnClickListener {
            for (i in icons.indices) {
                icons[i].isChecked = i == position
            }
            notifyDataSetChanged()
        }

        return convertViewAdapter
    }
}