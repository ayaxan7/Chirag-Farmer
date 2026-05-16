package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.ActiveOrdersResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderTrackingDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.UserPlacedOrdersResponse
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Path
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateOrderStatusRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateOrderStatusResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderDetailsResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.CancelOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.CancelOrderResponse
import retrofit2.http.PATCH

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

    @PATCH("api/farmers/orders/{id}/status")
    suspend fun updateOrderStatus(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: UpdateOrderStatusRequest
    ): UpdateOrderStatusResponse
    @GET("api/farmers/orders/my-placed-orders")
    suspend fun getUserPlacedOrders(
        @Header("Authorization") token: String,
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): UserPlacedOrdersResponse

    @GET("api/farmers/orders/details/{id}")
    suspend fun getOrderDetails(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): OrderDetailsResponse

    @POST("api/farmer/orders/cancel")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Body request: CancelOrderRequest
    ): CancelOrderResponse
}
