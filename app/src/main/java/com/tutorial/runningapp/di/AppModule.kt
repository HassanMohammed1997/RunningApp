package com.tutorial.runningapp.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.tutorial.runningapp.data.db.AppDatabase
import com.tutorial.runningapp.stopwatch.StopWatchStateHolder
import com.tutorial.runningapp.stopwatch.StopwatchListOrchestrator
import com.tutorial.runningapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideRunningDao(database: AppDatabase) = database.runDao()

    @Provides
    @Singleton
    fun provideStopwatchListOrchestrator(
        stopWatchStateHolder: StopWatchStateHolder,
        @BackgroundDispatcherQualifier dispatcher: CoroutineDispatcher
    ): StopwatchListOrchestrator {
        return StopwatchListOrchestrator(
            stopWatchStateHolder,
            scope = CoroutineScope(SupervisorJob() + dispatcher)
        )
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext app: Context): SharedPreferences =
        app.getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideName(pref: SharedPreferences): String = pref.getString(Constants.KEY_NAME, "") ?: ""

    @Provides
    @Singleton
    fun provideWeight(pref: SharedPreferences): Float = pref.getFloat(Constants.KEY_WEIGHT, 80f)

    @Provides
    @Singleton
    fun provideFirstTimeToggle(pref: SharedPreferences): Boolean =
        pref.getBoolean(Constants.KEY_FIRST_TIME_TOGGLE, true)
}