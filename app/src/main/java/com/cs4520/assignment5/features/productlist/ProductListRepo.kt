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
import com.cs4520.assignment5.common.Result

class ProductListRepo(
    private val productDAO: ProductDao,
    context: Context
) {
    private val networkStatus = NetworkStatus(context)
    suspend fun getProducts(page: Int? = null): Result<List<Product>> =
        withContext(Dispatchers.IO) {
            if (networkStatus.isNetworkAvailable()) {
                fetchProductsFromApi(page)
            } else {
                fetchProductsFromDatabase()
            }
        }

    private suspend fun fetchProductsFromApi(page: Int?): Result<List<Product>> = try {
        val response = Retrofit.api.getProducts(page)
        processApiResponse(response)
    } catch (e: Exception) {
        Result.Error(e)
    }

    private suspend fun processApiResponse(response: Response<List<Product>>): Result<List<Product>> {
        if (!response.isSuccessful) {
            return Result.Error(Exception("API Error: ${response.message()}"))
        }
        val body = response.body() ?: return Result.Empty(Exception("No data"))
        return if (body.isEmpty()) {
            Result.Empty(Exception("No data"))
        } else {
            saveProductsToDatabase(body)
            Result.Success(body)
        }
    }

    private suspend fun saveProductsToDatabase(products: List<Product>) =
        productDAO.insertAllProducts(products.mapToEntities())

    private suspend fun fetchProductsFromDatabase(): Result<List<Product>> =
        withContext(Dispatchers.IO) {
            val products = productDAO.getAllProducts().mapToDomain()
            if (products.isNotEmpty()) Result.Success(products)
            else Result.Empty(Exception("No data in database"))
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

