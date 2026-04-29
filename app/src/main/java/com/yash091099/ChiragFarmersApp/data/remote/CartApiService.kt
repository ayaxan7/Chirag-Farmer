package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.AddToCartRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddToCartResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.BuyNowResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.GetCartResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.RemoveFromCartRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.RemoveFromCartResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateQuantityRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateQuantityResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface CartApiService {
    @POST("api/cart/add")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body request: AddToCartRequest
    ): AddToCartResponse

    @GET("api/cart/")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): GetCartResponse

    @PATCH("api/cart/update-quantity")
    suspend fun updateQuantity(
        @Header("Authorization") token: String,
        @Body request: UpdateQuantityRequest
    ): UpdateQuantityResponse

    @POST("api/cart/remove")
    suspend fun removeFromCart(
        @Header("Authorization") token: String,
        @Body request: RemoveFromCartRequest
    ): RemoveFromCartResponse

    @GET("api/farmers/buy-now")
    suspend fun buyNow(
        @Header("Authorization") token: String,
        @Query("productId") productId: String,
        @Query("quantity") quantity: Int = 1
    ): BuyNowResponse
}

