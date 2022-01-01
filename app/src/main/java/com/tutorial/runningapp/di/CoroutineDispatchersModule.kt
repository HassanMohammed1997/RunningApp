package com.tutorial.runningapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@DisableInstallInCheck
@Module
object CoroutineDispatchersModule {
    @Provides
    @BackgroundDispatcherQualifier
    fun provideBackgroundDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IODispatcherQualifier
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

}