/*
 * Project: meteo
 * File: ImageListPreferenceAdapter.kt
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.gmail.fattazzo.meteo.R

internal class ImageListPreferenceAdapter(
    context: Context,
    private val resource: Int,
    private val icons: List<ImageItem>
) :
    ArrayAdapter<ImageItem>(context, resource, icons) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertViewAdapter: View

        val holder: ImageItemViewHolder

        if (convertView == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertViewAdapter = inflater.inflate(resource, parent, false)

            holder = ImageItemViewHolder()
            holder.iconName = convertViewAdapter.findViewById(R.id.iconName)
            holder.iconImage = convertViewAdapter.findViewById(R.id.iconImage)
            holder.radioButton = convertViewAdapter.findViewById(R.id.iconRadio)
            holder.position = position

            convertViewAdapter.tag = holder
        } else {
            convertViewAdapter = convertView
            holder = convertView.tag as ImageItemViewHolder
        }

        holder.iconName!!.text = icons[position].name

        val identifier = context.resources.getIdentifier(
            icons[position].file, "drawable",
            context.packageName
        )
        holder.iconImage!!.setBackgroundResource(identifier)

        holder.radioButton!!.isChecked = icons[position].isChecked

        convertViewAdapter.setOnClickListener { v ->
            val holder1 = v.tag as ImageItemViewHolder
            for (i in icons.indices) {
                icons[i].isChecked = i == holder1.position
            }
            notifyDataSetChanged()
        }

        return convertViewAdapter
    }

}