package com.ayaan.chiragfarmer.data.remote

import com.ayaan.chiragfarmer.data.remote.dto.AddProductRequest
import com.ayaan.chiragfarmer.data.remote.dto.AddProductResponse
import com.ayaan.chiragfarmer.data.remote.dto.ProductResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductApiService {
    @GET("api/farmers/products")
    suspend fun getFarmerProducts(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("type") type: String,
        @Query("search") search: String? = null
    ): ProductResponseDto

    @POST("api/farmers/add-product")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Body request: AddProductRequest
    ): AddProductResponse
}
