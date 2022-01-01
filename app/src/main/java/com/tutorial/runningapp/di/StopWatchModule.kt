package com.tutorial.runningapp.di

import com.tutorial.runningapp.stopwatch.ClockTimestampProvider
import com.tutorial.runningapp.stopwatch.TimestampProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [CoroutineDispatchersModule::class])
abstract class StopWatchModule {
    @Binds
    abstract fun bindTimestampProvider(timestampProvider: ClockTimestampProvider): TimestampProvider
}