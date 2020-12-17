package com.udacity.asteroidradar.work

import android.app.Application
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.main.MainRepository
import retrofit2.HttpException

class RefreshDatabaseWork(val application: Application, val params: WorkerParameters) :
    CoroutineWorker(application, params) {

    companion object {
        const val WORK_NAME = "RefreshDatabaseWork"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(application.applicationContext)
        val repository = MainRepository(database)
        try {
            repository.refreshDatabase()
            repository.deleteFromDatabase()
            return Result.success()
        } catch (httpException: HttpException) {
            return Result.retry()
        }
    }
}