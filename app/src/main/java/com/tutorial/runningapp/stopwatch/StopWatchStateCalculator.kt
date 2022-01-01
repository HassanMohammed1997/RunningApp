package com.tutorial.runningapp.stopwatch

import javax.inject.Inject

/**
 * To calculate the state changes between Running and Paused
 */
class StopWatchStateCalculator @Inject constructor(
    private val timestampProvider: TimestampProvider,
    private val elapsedTimeCalculator: ElapsedTimeCalculator
) {

    /**
     * If the state remains unchanged, the old state is just returned
     *
     * Paused -> Running
     * In this state, Saves the current timestamp as the stopwatch start time
     * The elapsed time is reused from the old state because the stopwatch should not be running in the pause state
     *
     * Running -> Paused
     * In the elapsed time based on the stopwatch start time and the current elapsed time
     */
    fun calculateRunningState(oldState: StopWatchState): StopWatchState.Running =
        when (oldState) {
            is StopWatchState.Paused -> StopWatchState.Running(
                startTime = timestampProvider.getMilliseconds(),
                elapsedTime = oldState.elapsedTime
            )
            is StopWatchState.Running -> oldState
        }

    fun calculatePausedState(oldState: StopWatchState): StopWatchState.Paused = when (oldState) {
        is StopWatchState.Paused -> oldState
        is StopWatchState.Running -> {
            val elapsedTime = elapsedTimeCalculator.calculate(oldState)
            StopWatchState.Paused(elapsedTime = elapsedTime)
        }
    }

}

/**
 * To calculate the elapsed time
 */
class ElapsedTimeCalculator @Inject constructor(
    private val timestampProvider: TimestampProvider
) {
    /**
     * The condition check (currentTimestamp - state.startTime) should always be true, but you never know
     * elapsedTime = (current timestamp - the stopwatch start time) + the existing elapsed time
     */
    fun calculate(state: StopWatchState.Running): Long {
        val currentTimestamp = timestampProvider.getMilliseconds()
        val timePassedSinceStart = if (currentTimestamp > state.startTime)
            currentTimestamp - state.startTime
        else
            0

        return timePassedSinceStart + state.elapsedTime
    }
}