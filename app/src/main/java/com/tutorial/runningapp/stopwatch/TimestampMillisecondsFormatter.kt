package com.tutorial.runningapp.stopwatch

import javax.inject.Inject

class TimestampMillisecondsFormatter @Inject constructor() {
    companion object {
        const val DEFAULT_FORMAT = "00:00:000"

        fun format(timestamp: Long): String {
            val millisecondsFormatted = (timestamp % 1000).pad(3)
            val seconds = timestamp / 1000
            val secondsFormatted = (seconds % 60).pad(2)
            val minutes = seconds / 60
            val minutesFormatted = (minutes % 60).pad(2)
            val hours = minutes / 60
            return if (hours > 0) {
                val hourFormatted = (minutes / 60).pad(2)
                "$hourFormatted:$minutesFormatted:$secondsFormatted"
            } else "$minutesFormatted:$secondsFormatted:$millisecondsFormatted"
        }

        private fun Long.pad(desiredLength: Int) = this.toString().padStart(desiredLength, '0')
    }

/*    fun format(timestamp: Long): String {
        val millisecondsFormatted = (timestamp % 1000).pad(3)
        val seconds = timestamp / 1000
        val secondsFormatted = (seconds % 60).pad(2)
        val minutes = seconds / 60
        val minutesFormatted = (minutes % 60).pad(2)
        val hours = minutes / 60
        return if (hours > 0) {
            val hourFormatted = (minutes / 60).pad(2)
            "$hourFormatted:$minutesFormatted:$secondsFormatted"
        } else "$minutesFormatted:$secondsFormatted:$millisecondsFormatted"
    }

    private fun Long.pad(desiredLength: Int) = this.toString().padStart(desiredLength, '0')*/


}