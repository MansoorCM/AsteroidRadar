package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.work.RefreshDatabaseWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication : Application() {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        coroutineScope.launch {
            setUpRecurringWork()
        }
    }

    fun setUpRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<RefreshDatabaseWork>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDatabaseWork.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest
        )
    }
}