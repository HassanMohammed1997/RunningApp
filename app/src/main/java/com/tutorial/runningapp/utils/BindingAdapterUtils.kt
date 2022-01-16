package com.tutorial.runningapp.utils

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tutorial.runningapp.stopwatch.TimestampMillisecondsFormatter
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapterUtils {
    @BindingAdapter("app:setDate", "app:dateFormat", requireAll = false)
    @JvmStatic
    fun TextView.bindDate(date: Long, format: String = "dd.MM.yy"){
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
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
        text = "${distanceInMeter / 1000}km"
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