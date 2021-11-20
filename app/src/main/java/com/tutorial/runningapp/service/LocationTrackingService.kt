package com.tutorial.runningapp.service

import android.content.Intent
import androidx.lifecycle.LifecycleService
import com.tutorial.runningapp.utils.Constants
import timber.log.Timber

class LocationTrackingService : LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                Constants.ACTION_START_OR_RESUME_SERVICE -> {
                    Timber.d("Start or Resume service")
                }

                Constants.ACTION_PAUSE_SERVICE -> {
                    Timber.d("Pause service")
                }

                Constants.ACTION_STOP_SERVICE -> {
                    Timber.d("Stop service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}