package com.tutorial.runningapp.stopwatch

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class StopwatchListOrchestrator @Inject constructor(
    private val stopWatchStateHolder: StopWatchStateHolder,
    private val scope: CoroutineScope
) {

    private var job: Job? = null
    private val _tickerInMilli = MutableStateFlow(0L)
    val tickerInMilli: StateFlow<Long> = _tickerInMilli

    fun start() {
        if (job == null) startJob()
        stopWatchStateHolder.start()
    }

    fun pause() {
        stopWatchStateHolder.pause()
        stopJob()
    }

    fun stop() {
        stopWatchStateHolder.stop()
        stopJob()
        clearValue()
    }

    private fun clearValue() {
        _tickerInMilli.value = 0L
    }

    private fun stopJob() {
        scope.coroutineContext.cancelChildren()
        job = null
    }

    private fun startJob() {
        scope.launch {
            while (isActive) {
                _tickerInMilli.value = stopWatchStateHolder.timeInMilli
                delay(20)
            }
        }

    }
}