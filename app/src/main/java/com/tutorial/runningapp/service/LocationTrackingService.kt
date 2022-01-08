package com.tutorial.runningapp.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getService
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.tutorial.runningapp.R
import com.tutorial.runningapp.stopwatch.StopwatchListOrchestrator
import com.tutorial.runningapp.utils.Constants
import com.tutorial.runningapp.utils.LocationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

typealias Polyline = MutableList<LatLng>
typealias Polyines = MutableList<Polyline>

@AndroidEntryPoint
class LocationTrackingService : LifecycleService() {
    var isFirstRun = true
    var isTimerEnabled = false

    @Inject
    lateinit var stopwatchListOrchestrator: StopwatchListOrchestrator

    @Inject
    lateinit var baseServiceNotificationBuilder: NotificationCompat.Builder

    lateinit var currentServiceNotificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val tickerInSeconds = MutableLiveData<String>()
    companion object {
        val ticker = MutableLiveData<String>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polyines>()

    }

    override fun onCreate() {
        super.onCreate()
        postInitalValues()
        currentServiceNotificationBuilder = baseServiceNotificationBuilder

        isTracking.observe(this) {
            updateLocationTracking(it)
            updateNotificationTrackingState(it)
        }
    }

    private fun postInitalValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        ticker.postValue("")
        tickerInSeconds.postValue("")
    }

    private fun startTimer() {
        addEmptyPolyLine()
        isTracking.postValue(true)
        isTimerEnabled = true
        stopwatchListOrchestrator.start()
        lifecycleScope.launchWhenStarted {
            // for update a notification every 1 sec
            stopwatchListOrchestrator.ticker.onStart { delay(1000) }
                .collectLatest { tickerInSeconds.postValue(it) }
        }

        lifecycleScope.launchWhenStarted {
            stopwatchListOrchestrator.ticker.collect {
                ticker.postValue(it)
            }
        }


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                Constants.ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        startForegroundService()
                        Timber.d("Resuming Service")
                    }
                }

                Constants.ACTION_PAUSE_SERVICE -> {
                    pauseService()
                }

                Constants.ACTION_STOP_SERVICE -> {
                    pauseService()
                    stopwatchListOrchestrator.stop()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
        stopwatchListOrchestrator.pause()
    }

    private fun addEmptyPolyLine() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun addNewPathPoint(location: Location?) {
        location?.let {
            val point = LatLng(it.latitude, it.longitude)
            pathPoints.value?.apply {
                last().add(point)
                pathPoints.postValue(this)
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result.locations.let { locations ->
                    for (location in locations) {
                        addNewPathPoint(location)
                        Timber.d("Location: $location")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (LocationUtils.hasLocationPermission(this)) {
                val request = LocationRequest.create().apply {
                    interval = Constants.LOCATIONS_UPDATE_INTERVAL
                    fastestInterval = Constants.FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }

                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW

        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun startForegroundService() {
        startTimer()
        startForegroundNotification()
        tickerInSeconds.observe(this) {
            val notification = currentServiceNotificationBuilder.setContentText(it)
            notificationManager.notify(Constants.NOTIFICATION_ID, notification.build())
        }
    }

    private fun startForegroundNotification() {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }


        startForeground(Constants.NOTIFICATION_ID, baseServiceNotificationBuilder.build())
    }

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionStringRes = getNotificationActionText(isTracking)
        val pendingIntent = createTrackingPendingIntent(isTracking)
        updateCurrentNotification(notificationActionStringRes, pendingIntent)
    }

    private fun updateCurrentNotification(
        notificationActionStringRes: String,
        pendingIntent: PendingIntent?
    ) {
        currentServiceNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currentServiceNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }
        currentServiceNotificationBuilder =
            baseServiceNotificationBuilder.addAction(
                R.drawable.baseline_pause_black_24dp,
                notificationActionStringRes,
                pendingIntent
            )
        notificationManager.notify(
            Constants.NOTIFICATION_ID,
            currentServiceNotificationBuilder.build()
        )
    }

    private fun getNotificationActionText(isTracking: Boolean) =
        if (isTracking) getString(R.string.pause) else getString(R.string.resume)

    private fun createTrackingPendingIntent(isTracking: Boolean) = if (isTracking) {
        val pauseIntent = Intent(this, LocationTrackingService::class.java).apply {
            action = Constants.ACTION_PAUSE_SERVICE
        }
        getService(this, 1, pauseIntent, FLAG_UPDATE_CURRENT)
    } else {
        val resumeIntent = Intent(this, LocationTrackingService::class.java).apply {
            action = Constants.ACTION_START_OR_RESUME_SERVICE
        }
        getService(this, 2, resumeIntent, FLAG_UPDATE_CURRENT)
    }

    private val notificationManager get() = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
}
