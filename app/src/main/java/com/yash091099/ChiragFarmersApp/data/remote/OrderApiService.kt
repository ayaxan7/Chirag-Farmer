package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.ActiveOrdersResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface OrderApiService {
    @GET("api/farmer/orders/active-orders")
    suspend fun getActiveOrders(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ActiveOrdersResponse
}
