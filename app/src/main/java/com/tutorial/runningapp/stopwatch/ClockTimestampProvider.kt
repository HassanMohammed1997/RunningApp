package com.tutorial.runningapp.stopwatch

import javax.inject.Inject

class ClockTimestampProvider @Inject constructor() : TimestampProvider {
    override fun getMilliseconds(): Long {
        return System.currentTimeMillis()
    }
}