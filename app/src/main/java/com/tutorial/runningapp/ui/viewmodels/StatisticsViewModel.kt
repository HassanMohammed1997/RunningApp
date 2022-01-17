package com.tutorial.runningapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.tutorial.runningapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val totalTimeInMs = repository.getTotalTimeInMs()
    val totalAvgSpeed = repository.getTotalAverageSpeed()
    val totalCaloriesBurned = repository.getTotalCaloriesBurned()
    val totalDistance = repository.getTotalDistance()
    val runSortedByDate = repository.getAllRunsSortedByDate()

}