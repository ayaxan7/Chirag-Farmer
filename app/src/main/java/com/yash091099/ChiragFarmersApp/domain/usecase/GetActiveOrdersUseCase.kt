package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.domain.model.OrdersData
import com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
import javax.inject.Inject

class GetActiveOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(page: Int = 1, limit: Int = 10): Result<OrdersData> {
        return orderRepository.getActiveOrders(page, limit)
    }
}

