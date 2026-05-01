package com.yash091099.ChiragFarmersApp.domain.repository

import com.yash091099.ChiragFarmersApp.domain.model.OrdersData

interface OrderRepository {
    suspend fun getActiveOrders(page: Int, limit: Int): Result<OrdersData>
}

