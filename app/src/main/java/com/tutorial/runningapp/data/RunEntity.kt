package com.tutorial.runningapp.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class RunEntity(
    var image: Bitmap? = null,
    var timestamp: Long = 0,
    var averageSpeedInKMH: Float = 0F,
    var distanceInMeters: Int = 0,
    var timeInMs: Long = 0,
    var caloriesBurned: Int = 0,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

}