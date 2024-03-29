package com.cs4520.assignment5.features.productlist

import android.content.Context
import com.cs4520.assignment5.core.database.ProductDao
import com.cs4520.assignment5.core.network.Retrofit
import com.cs4520.assignment5.core.model.Product
import com.cs4520.assignment5.core.model.ProductEntity
import com.cs4520.assignment5.core.network.NetworkStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import com.cs4520.assignment5.common.ApiResult

class ProductListRepo(
    private val productDAO: ProductDao,
    context: Context
) {
    private val networkStatus = NetworkStatus(context)
    suspend fun getProducts(page: Int? = null): ApiResult<List<Product>> =
        withContext(Dispatchers.IO) {
            if (networkStatus.isNetworkAvailable()) {
                fetchProductsFromApi(page)
            } else {
                fetchProductsFromDatabase()
            }
        }
    suspend fun getNewProducts() : ApiResult<List<Product>>  =
        withContext(Dispatchers.IO) {
            if (networkStatus.isNetworkAvailable()) {
                fetchNewProductsFromApi()
            } else {
                ApiResult.Error(Exception("No network available"))
            }
        }

    private suspend fun fetchProductsFromApi(page: Int?): ApiResult<List<Product>> = try {
        val response = Retrofit.api.getProducts(page)
        processApiResponse(response)
    } catch (e: Exception) {
        ApiResult.Error(e)
    }

    private suspend fun fetchNewProductsFromApi(): ApiResult<List<Product>> = try {
        val response = Retrofit.api.getNewProducts()
        processApiResponse(response)
    } catch (e: Exception) {
        ApiResult.Error(e)
    }

    private suspend fun processApiResponse(response: Response<List<Product>>): ApiResult<List<Product>> {
        if (!response.isSuccessful) {
            return ApiResult.Error(Exception("API Error: ${response.message()}"))
        }
        val body = response.body() ?: return ApiResult.Empty(Exception("No data"))
        return if (body.isEmpty()) {
            ApiResult.Empty(Exception("No data"))
        } else {
            saveProductsToDatabase(body)
            ApiResult.Success(body)
        }
    }

    private suspend fun saveProductsToDatabase(products: List<Product>) =
        productDAO.insertAllProducts(products.mapToEntities())

    private suspend fun fetchProductsFromDatabase(): ApiResult<List<Product>> =
        withContext(Dispatchers.IO) {
            val products = productDAO.getAllProducts().mapToDomain()
            if (products.isNotEmpty()) ApiResult.Success(products)
            else ApiResult.Empty(Exception("No data in database"))
        }

    private fun List<Product>.mapToEntities(): List<ProductEntity> = map { product ->
        ProductEntity(
            name = product.name,
            price = product.price,
            expiryDate = product.expiryDate,
            type = product.type
        )
    }

    private fun List<ProductEntity>.mapToDomain(): List<Product> = map { entity ->
        Product(
            name = entity.name,
            price = entity.price,
            expiryDate = entity.expiryDate,
            type = entity.type
        )
    }


}

