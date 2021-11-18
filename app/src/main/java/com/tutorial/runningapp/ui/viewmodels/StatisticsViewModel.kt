package com.tutorial.runningapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.tutorial.runningapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
}