package com.tutorial.runningapp.utils.extensions

fun Int.toKm() = this / 1000f

fun Long.toHour() = (this / 1000f / 60 / 60)