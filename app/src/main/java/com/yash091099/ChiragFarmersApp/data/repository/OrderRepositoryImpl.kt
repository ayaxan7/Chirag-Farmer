package com.yash091099.ChiragFarmersApp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.paging.ActiveOrdersPagingSource
import com.yash091099.ChiragFarmersApp.data.remote.OrderApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.toDomain
import com.yash091099.ChiragFarmersApp.domain.model.Order
import com.yash091099.ChiragFarmersApp.domain.model.OrdersData
import com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderTrackingDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateOrderStatusRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateOrderStatusResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: OrderApiService,
    private val chiragDataStore: ChiragDataStore
) : OrderRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getActiveOrdersPaged(): Flow<PagingData<Order>> {
        return chiragDataStore.getAuthToken().flatMapLatest { token ->
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    initialLoadSize = 10,
                    prefetchDistance = 1,
                    enablePlaceholders = false
                ), pagingSourceFactory = {
                    ActiveOrdersPagingSource(apiService = api, token = token.orEmpty())
                }
            ).flow
        }
    }

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

    override suspend fun updateOrderStatus(
        id: String,
        request: UpdateOrderStatusRequest
    ): Result<UpdateOrderStatusResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            val response = api.updateOrderStatus("Bearer $token", id, request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


