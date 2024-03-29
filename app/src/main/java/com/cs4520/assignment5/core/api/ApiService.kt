package com.cs4520.assignment5.core.api

import com.cs4520.assignment5.core.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(Api.ENDPOINT)
    suspend fun getProducts(@Query("page") pageNumber: Int?) : Response<List<Product>>

    @GET(Api.SCHEDULED_ENDPOINT)
    suspend fun getNewProducts() : Response<List<Product>>
}
