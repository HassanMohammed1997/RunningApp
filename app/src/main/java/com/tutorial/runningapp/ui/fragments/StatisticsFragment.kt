package com.tutorial.runningapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.tutorial.runningapp.R
import com.tutorial.runningapp.stopwatch.TimestampMillisecondsFormatter
import com.tutorial.runningapp.ui.viewmodels.StatisticsViewModel
import com.tutorial.runningapp.utils.CustomMarkerView
import com.tutorial.runningapp.utils.toKm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private val statisticsViewModel: StatisticsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        statisticsViewModel.totalTimeInMs.observe(viewLifecycleOwner){
            it?.let {
                val totalTimeRun = TimestampMillisecondsFormatter.format(it)
                tvTotalTime.text = totalTimeRun
            }
        }

        statisticsViewModel.totalDistance.observe(viewLifecycleOwner){
            it?.let {
                val km = it.toKm()
                val totalDistance = round(km * 10) / 10f
                val totalDistanceString = "${totalDistance}km"
                tvTotalDistance.text = totalDistanceString
            }
        }

        statisticsViewModel.totalAvgSpeed.observe(viewLifecycleOwner){
            it?.let {
                val avgSpeed = round(it * 10) / 10f
                val avgSpeedString = "${avgSpeed}km/h"
                tvAverageSpeed.text = avgSpeedString
            }
        }


        statisticsViewModel.totalCaloriesBurned.observe(viewLifecycleOwner){
            it?.let {
                val totalCalories = "${it}kcal"
                tvTotalCalories.text = totalCalories
            }
        }

        statisticsViewModel.runSortedByDate.observe(viewLifecycleOwner){
            it?.let {
                val allAvgSpeed = it.indices.map { i -> BarEntry(i.toFloat(), it[i].averageSpeedInKMH) }
                val barDataSet = BarDataSet(allAvgSpeed, getString(R.string.avg_speed_over_time)).apply {
                    valueTextColor = Color.WHITE
                    color = getColor(R.color.colorAccent)
                }
                barChart.apply {
                    data = BarData(barDataSet)
                    marker = CustomMarkerView(requireContext(), R.layout.custom_marker_view, it.reversed())
                    invalidate()
                }
            }
        }

    }

    private fun setupBarChart(){
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        barChart.apply {
            description.text = getString(R.string.avg_speed_over_time)
            legend.isEnabled = false
        }
    }

}