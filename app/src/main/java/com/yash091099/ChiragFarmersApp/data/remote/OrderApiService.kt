package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.ActiveOrdersResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderTrackingDto
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Path

interface OrderApiService {
    @GET("api/farmers/orders/active-orders")
    suspend fun getActiveOrders(
    @Header("Authorization") token: String,
    @Query("page") page: Int,
    @Query("limit") limit: Int
    ): ActiveOrdersResponse
    @POST("api/farmers/orders/")
    suspend fun placeOrder(
        @Header("Authorization") token: String,
        @Body request: PlaceOrderRequest
    ): PlaceOrderResponse

    @GET("api/farmers/orders/{id}")
    suspend fun getOrderTracking(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): OrderTrackingDto
}
