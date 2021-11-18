package com.tutorial.runningapp.repositories

import com.tutorial.runningapp.data.db.RunDAO
import com.tutorial.runningapp.data.db.RunEntity
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val runDao: RunDAO
) {

    suspend fun insertRun(run: RunEntity) = runDao.insertRun(run)

    suspend fun deleteRun(run: RunEntity) = runDao.deleteRun(run)

    fun getAllRunsSortedByDate() = runDao.getAllRunsSortedByDate()

    fun getAllRunsSortedBySpeed() = runDao.getAllRunsSortedBySpeed()

    fun getAllRunsSortedByTimeInMs() = runDao.getAllRunsSortedByTimeInMs()

    fun getAllRunsSortedByCaloriesBurned() = runDao.getAllRunsSortedByCaloriesBurned()

    fun getAllRunsSortedByDistance() = runDao.getAllRunsSortedByDistance()

    fun getTotalTimeInMs() = runDao.getTotalTimeInMs()

    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()

    fun getTotalDistance() = runDao.getTotalDistance()

    fun getTotalAverageSpeed() = runDao.getTotalAverageSpeed()

}