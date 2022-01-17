package com.tutorial.runningapp.utils

import android.content.Context
import androidx.annotation.LayoutRes
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.tutorial.runningapp.data.db.RunEntity
import com.tutorial.runningapp.stopwatch.TimestampMillisecondsFormatter
import com.tutorial.runningapp.utils.extensions.toKm
import kotlinx.android.synthetic.main.custom_marker_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
    context: Context,
    @LayoutRes layoutId: Int,
    val runs: List<RunEntity>,
) : MarkerView(context, layoutId) {
    override fun getOffset(): MPPointF = MPPointF(-width / 2f, -height.toFloat())

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e == null) return

        val currentRunID = e.x.toInt()
        val run = runs[currentRunID]

        tvDate.text = SimpleDateFormat("dd.MM.yy", Locale.getDefault()).format(run.timestamp)

        val avgSpeed = "${run.averageSpeedInKMH}km/h"
        tvAvgSpeed.text = avgSpeed

        val distanceInKm = "${run.distanceInMeters.toKm()}km"
        tvDistance.text = distanceInKm


        tvDuration.text = TimestampMillisecondsFormatter.format(run.timeInMs)

        val caloriesBurned = "${run.caloriesBurned}kcal"
        tvCaloriesBurned.text = caloriesBurned
    }
}