package com.cs4520.assignment5.core.background

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkerScheduler(context: Context) {
    private val workManager = WorkManager.getInstance(context)

    fun scheduleProductRefreshWorker(repeatedInterval: Long) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<ProductRefreshWorker>(
            repeatInterval = repeatedInterval,
            repeatIntervalTimeUnit = TimeUnit.SECONDS
        ).setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork(
            "Product List Request",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )
    }
}