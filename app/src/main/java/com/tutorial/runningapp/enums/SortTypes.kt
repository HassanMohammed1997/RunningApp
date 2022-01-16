package com.tutorial.runningapp.enums

enum class SortTypes {
    DATE, RUNNING_TIME, AVG_SPEED, DISTANCE, CALORIES_BURNED;

    companion object{
        fun getSortType(position: Int) = values().first { it.ordinal == position }
    }
}