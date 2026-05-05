package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderResponse
import javax.inject.Inject

class PlaceOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(request: PlaceOrderRequest): Result<PlaceOrderResponse> {
        return orderRepository.placeOrder(request)
    }
}

