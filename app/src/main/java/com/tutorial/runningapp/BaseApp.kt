package com.tutorial.runningapp

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApp  : Application(){
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        val currentUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            Log.println(Log.ERROR, thread.name, Log.getStackTraceString(exception))
            currentUncaughtExceptionHandler?.uncaughtException(thread, exception)
        }
    }
}