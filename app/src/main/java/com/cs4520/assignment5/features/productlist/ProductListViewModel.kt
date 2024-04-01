package com.cs4520.assignment5.features.productlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.cs4520.assignment5.common.ApiResult
import com.cs4520.assignment5.core.background.ProductRefreshWorker
import com.cs4520.assignment5.core.model.Product
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ProductListViewModel(
    private val context : Application,
    private val repository: ProductListRepo) : ViewModel() {

    private val _productList = MutableLiveData<ApiResult<List<Product>>>()
    val productList: LiveData<ApiResult<List<Product>>> = _productList

    // Pagination state
    private val currentPage = MutableLiveData(0)

    init {
        fetchNextPage()
    }

    fun fetchNextPage() {
        viewModelScope.launch {
            currentPage.value?.let { page ->
                val result = repository.getProducts(page)
                if (result is ApiResult.Success) {
                    currentPage.value = page + 1
                }
                _productList.value = result
            }
        }
    }

    fun loadData() {
        _productList.value
    }

    fun scheduleProductRefreshWorker(repeatedInterval: Long) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<ProductRefreshWorker>(
            repeatInterval = repeatedInterval,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).setConstraints(constraints).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "Product Refresh",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )
    }

}
