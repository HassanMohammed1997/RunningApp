package com.tutorial.runningapp.stopwatch

sealed class StopWatchState {
    /**
     * The paused state contains an elapsed time used in order to preserve the time between
     * stopwatch state changes
     */
    data class Paused(
        val elapsedTime: Long
    ) : StopWatchState()

    /**
     * The Running state contains information about the stopwatch was started and what
     * the current elapsed time is
     * At the beginning, the elapsed time will be 0 and every the stopwatch is paused and started this value changes
     */
    data class Running(
        val startTime: Long,
        val elapsedTime: Long
    ) : StopWatchState()
}