package com.yash091099.ChiragFarmersApp.domain.repository

import androidx.paging.PagingData
import com.yash091099.ChiragFarmersApp.domain.model.OrdersData
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderTrackingDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateOrderStatusRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateOrderStatusResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.UserPlacedOrdersResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderDetailsResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.CancelOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.CancelOrderResponse
import com.yash091099.ChiragFarmersApp.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun getActiveOrders(page: Int, limit: Int): Result<OrdersData>
    fun getActiveOrdersPaged(status: String? = null): Flow<PagingData<Order>>
    suspend fun placeOrder(request: PlaceOrderRequest): Result<PlaceOrderResponse>
    suspend fun getOrderTracking(id: String): Result<OrderTrackingDto>
    suspend fun updateOrderStatus(id: String, request: UpdateOrderStatusRequest): Result<UpdateOrderStatusResponse>
    suspend fun getUserPlacedOrders(type: String, page: Int, limit: Int): Result<UserPlacedOrdersResponse>
    suspend fun getOrderDetails(id: String): Result<OrderDetailsResponse>
    suspend fun cancelOrder(request: CancelOrderRequest): Result<CancelOrderResponse>
    suspend fun sellerCancelOrder(request: CancelOrderRequest): Result<CancelOrderResponse>
}

