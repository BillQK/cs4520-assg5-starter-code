package com.cs4520.assignment5.core.background

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cs4520.assignment5.common.ApiResult
import com.cs4520.assignment5.core.database.AppDatabase
import com.cs4520.assignment5.features.productlist.ProductListRepo

class ProductRefreshWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val repo = ProductListRepo(
            AppDatabase.getDatabase(applicationContext).productDao(),
            applicationContext
        )
        val products = repo.getNewProducts()
        Log.d("Worker", "Hello WORLD from my slave")
        return when (products) {
            is ApiResult.Empty -> {
                Result.failure()
            }

            is ApiResult.Error -> {
                Result.failure()
            }

            is ApiResult.Success -> {
                Result.success()
            }

        }

    }
}