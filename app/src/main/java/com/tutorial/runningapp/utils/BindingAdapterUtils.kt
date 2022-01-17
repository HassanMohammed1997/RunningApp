package com.tutorial.runningapp.utils

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tutorial.runningapp.stopwatch.TimestampMillisecondsFormatter
import com.tutorial.runningapp.utils.extensions.toKm
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapterUtils {
    @BindingAdapter("app:setDate")
    @JvmStatic
    fun TextView.bindDate(date: Long){
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        text = dateFormat.format(date)
    }

    @BindingAdapter("app:loadImage")
    @JvmStatic
    fun ImageView.bindImage(image: Bitmap){
        Glide.with(context).load(image).into(this)
    }

    @BindingAdapter("app:setAverageSpeed")
    @JvmStatic
    fun TextView.bindAvgSpeedInKm(speed: Float){
        text = "${speed}km/h"
    }

    @BindingAdapter("app:setDistanceInKm")
    @JvmStatic
    fun TextView.bindDistanceInKm(distanceInMeter: Int){
        text = "${distanceInMeter.toKm()}km"
    }

    @BindingAdapter("app:setCaloriesBurned")
    @JvmStatic
    fun TextView.bindCaloriesBurned(calories: Int){
        text = "${calories}kcal"
    }

    @BindingAdapter("app:setTime")
    @JvmStatic
    fun TextView.bindTime(timeInMs: Long){
        text = TimestampMillisecondsFormatter.format(timeInMs)
    }
}