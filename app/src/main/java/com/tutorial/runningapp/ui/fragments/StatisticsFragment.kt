package com.tutorial.runningapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tutorial.runningapp.R
import com.tutorial.runningapp.stopwatch.TimestampMillisecondsFormatter
import com.tutorial.runningapp.ui.viewmodels.StatisticsViewModel
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

    }

}