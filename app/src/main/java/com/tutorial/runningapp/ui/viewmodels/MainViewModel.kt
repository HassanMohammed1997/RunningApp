package com.tutorial.runningapp.ui.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorial.runningapp.data.db.RunEntity
import com.tutorial.runningapp.enums.SortTypes
import com.tutorial.runningapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val runSortedByDate = repository.getAllRunsSortedByDate()
    private val runsSortedByDistance = repository.getAllRunsSortedByDistance()
    private val runsSortedByCaloriesBurned = repository.getAllRunsSortedByCaloriesBurned()
    private val runsSortedByTimeInMillis = repository.getAllRunsSortedByTimeInMs()
    private val runsSortedByAvgSpeed = repository.getAllRunsSortedBySpeed()

    val runs = MediatorLiveData<List<RunEntity>>()

    var sortType = SortTypes.DATE


    init {
        runs.addSource(runSortedByDate) { result ->
            if (sortType == SortTypes.DATE) {
                result?.let { runs.value = it }
            }

        }

        runs.addSource(runsSortedByAvgSpeed) { result ->
            if (sortType == SortTypes.AVG_SPEED) {
                result?.let { runs.value = it }
            }

        }

        runs.addSource(runsSortedByCaloriesBurned) { result ->
            if (sortType == SortTypes.CALORIES_BURNED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByDistance) { result ->
            if (sortType == SortTypes.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMillis) { result ->
            if (sortType == SortTypes.RUNNING_TIME) {
                result?.let { runs.value = it }
            }
        }
    }

    fun sort(sortType: SortTypes) = when (sortType) {
        SortTypes.DATE -> runSortedByDate.value?.let { runs.value = it }
        SortTypes.RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runs.value = it }
        SortTypes.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
        SortTypes.DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
        SortTypes.CALORIES_BURNED -> runsSortedByCaloriesBurned.value?.let { runs.value = it }
    }.also { this.sortType = sortType }

    fun insertRun(run: RunEntity) = viewModelScope.launch(Dispatchers.IO) { repository.insertRun(run) }


}