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
    private val _ticker = MutableStateFlow("")
    val ticker: StateFlow<String> = _ticker

    private val _tickerInSeconds = MutableStateFlow("")
    val tickerInSeconds: StateFlow<String> = _tickerInSeconds

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
        _ticker.value = TimestampMillisecondsFormatter.DEFAULT_FORMAT
    }

    private fun stopJob() {
        scope.coroutineContext.cancelChildren()
        job = null
    }

    private fun startJob() {
        scope.launch {
            while (isActive) {
                _ticker.value = stopWatchStateHolder.timeRepresented
                delay(20)
            }
        }

    }
}