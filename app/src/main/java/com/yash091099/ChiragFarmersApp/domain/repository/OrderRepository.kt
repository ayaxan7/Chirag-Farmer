package com.yash091099.ChiragFarmersApp.domain.repository

import com.yash091099.ChiragFarmersApp.domain.model.OrdersData
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest

interface OrderRepository {
    suspend fun getActiveOrders(page: Int, limit: Int): Result<OrdersData>
    suspend fun placeOrder(request: PlaceOrderRequest): Result<PlaceOrderResponse>
}

