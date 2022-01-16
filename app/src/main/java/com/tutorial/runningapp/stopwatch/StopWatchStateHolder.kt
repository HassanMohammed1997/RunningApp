package com.tutorial.runningapp.stopwatch

import javax.inject.Inject

class StopWatchStateHolder @Inject constructor(
    private val stopWatchStateCalculator: StopWatchStateCalculator,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
    private val timestampMillisecondsFormatter: TimestampMillisecondsFormatter
) {
    var currentState: StopWatchState = StopWatchState.Paused(0)
        private set

    fun start() {
        currentState = stopWatchStateCalculator.calculateRunningState(currentState)
    }

    fun pause() {
        currentState = stopWatchStateCalculator.calculatePausedState(currentState)
    }

    fun stop() {
        currentState = StopWatchState.Paused(0)
    }

    val timeRepresented: String
        get() {
            val elapsedTime = when (val currentState = currentState) {
                is StopWatchState.Paused -> currentState.elapsedTime
                is StopWatchState.Running -> elapsedTimeCalculator.calculate(currentState)
            }

            return TimestampMillisecondsFormatter.format(elapsedTime)
        }

    val timeInMilli: Long
        get() {
            return when (val currentState = currentState) {
                is StopWatchState.Paused -> currentState.elapsedTime
                is StopWatchState.Running -> elapsedTimeCalculator.calculate(currentState)
            }
        }
}