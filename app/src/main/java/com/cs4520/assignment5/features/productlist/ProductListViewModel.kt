package com.cs4520.assignment5.features.productlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs4520.assignment5.common.ApiResult
import com.cs4520.assignment5.core.model.Product
import kotlinx.coroutines.launch

class ProductListViewModel(private val repository: ProductListRepo) : ViewModel() {

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

}
