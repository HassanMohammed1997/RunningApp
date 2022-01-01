package com.tutorial.runningapp.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.tutorial.runningapp.R
import com.tutorial.runningapp.ui.MainActivity
import com.tutorial.runningapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object TrackingServiceModule {

    @ServiceScoped
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient = FusedLocationProviderClient(context)

    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_TRACKING_FRAGMENT
        }, PendingIntent.FLAG_UPDATE_CURRENT
    )

    @ServiceScoped
    @Provides
    fun provideServiceNotification(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false) //can't be cancelled by user
        .setOngoing(true) //can't be swiped
        .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)


}