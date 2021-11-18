package com.tutorial.runningapp.di

import android.content.Context
import androidx.room.Room
import com.tutorial.runningapp.data.db.AppDatabase
import com.tutorial.runningapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
}