package com.yash091099.ChiragFarmersApp.data.repository

import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.OrderApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.toDomain
import com.yash091099.ChiragFarmersApp.domain.model.OrdersData
import com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderTrackingDto
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: OrderApiService,
    private val chiragDataStore: ChiragDataStore
) : OrderRepository {
    override suspend fun getActiveOrders(page: Int, limit: Int): Result<OrdersData> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.getActiveOrders("Bearer $token", page, limit)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun placeOrder(request: PlaceOrderRequest): Result<PlaceOrderResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.placeOrder("Bearer $token", request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOrderTracking(id: String): Result<OrderTrackingDto> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.getOrderTracking("Bearer $token", id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


