package com.tutorial.runningapp.stopwatch

/**
 * To provide the current timestamp of the system
 */
interface TimestampProvider {
    fun getMilliseconds() : Long
}