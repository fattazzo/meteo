/*
 * Project: meteo
 * File: WebcamsAdapter.kt
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

package com.gmail.fattazzo.meteo.fragment.webcam

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.gmail.fattazzo.meteo.R
import com.gmail.fattazzo.meteo.domain.xml.webcam.Webcam
import com.gmail.fattazzo.meteo.preferences.widget.webcam.WebcamWidgetsSettingsManager
import com.squareup.picasso.Picasso


class WebcamsAdapter(private val context: Context, private val webcams: List<Webcam>) : RecyclerView.Adapter<WebcamsAdapter.ViewHolder>(), View.OnClickListener {

    private val webcamWidgetsSettingsManager: WebcamWidgetsSettingsManager = WebcamWidgetsSettingsManager(context)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): WebcamsAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_webcam, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: WebcamsAdapter.ViewHolder, i: Int) {

        val webcam = webcams[i]

        viewHolder.localitaTV.text = webcam.localita
        viewHolder.descrizioneTV.text = webcam.descrizione

        val color = if (webcam.showInWidget) R.color.webcam_widget else android.R.color.black
        changeTint(viewHolder.widgetImageButton, R.drawable.star, color)
        viewHolder.widgetImageButton.setOnClickListener { _ ->
            webcamWidgetsSettingsManager.updateWebcamWidgetIds(webcam.id)
            webcam.showInWidget = !webcam.showInWidget
            val colorW = if (webcam.showInWidget) R.color.webcam_widget else android.R.color.black
            changeTint(viewHolder.widgetImageButton, R.drawable.star, colorW)
        }

        val colorF = if (webcam.favorite) R.color.webcam_favorite else android.R.color.black
        changeTint(viewHolder.favoriteImageButton, R.drawable.favorite, colorF)
        viewHolder.favoriteImageButton.setOnClickListener { _ ->
            webcamWidgetsSettingsManager.updateWebcamFavoriteIds(webcam.id)
            webcam.favorite = !webcam.favorite
            val colorFa = if (webcam.favorite) R.color.webcam_favorite else android.R.color.black
            changeTint(viewHolder.favoriteImageButton, R.drawable.favorite, colorFa)
        }

        val width = if (webcam.ratio == "16:9") (100 * 1.77).toInt() else 100
        val height = 100

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Picasso.get().load(webcam.link).resize(width, height).centerCrop().placeholder(circularProgressDrawable).into(viewHolder.thumbImageView)

        viewHolder.thumbImageView.setTag(R.id.thumbImageView, webcam)
        viewHolder.thumbImageView.setOnClickListener(this)

        val constraintSet = ConstraintSet()
        constraintSet.clone(viewHolder.rootLayout)
        constraintSet.setDimensionRatio(R.id.thumbImageView, webcam.ratio)
        constraintSet.applyTo(viewHolder.rootLayout)
    }

    override fun getItemViewType(position: Int): Int = if (webcams[position].ratio == "16:9") 2 else 1

    override fun getItemCount(): Int = webcams.size

    override fun onClick(v: View) {
        if (v.id == R.id.thumbImageView) {
            WebcamsViewerActivity_.intent(context).webcam(v.getTag(R.id.thumbImageView) as Webcam?).start()
        }
    }

    private fun changeTint(image: ImageView, drawableResId: Int, tintResId: Int) {
        val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, drawableResId)!!)
        image.setImageDrawable(drawable)

        val color = ContextCompat.getColor(context, tintResId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(drawable, color)

        } else {
            drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val localitaTV: TextView = view.findViewById(R.id.localitaTV)
        internal val descrizioneTV: TextView = view.findViewById(R.id.descrizioneTV)
        internal val thumbImageView: ImageView = view.findViewById(R.id.thumbImageView)
        internal val widgetImageButton: ImageView = view.findViewById(R.id.widgetImageButton)
        internal val favoriteImageButton: ImageView = view.findViewById(R.id.favoriteImageButton)
        internal val rootLayout: ConstraintLayout = view.findViewById(R.id.rootLayout)
    }

}