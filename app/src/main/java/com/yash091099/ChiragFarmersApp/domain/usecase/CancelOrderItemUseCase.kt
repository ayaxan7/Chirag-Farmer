package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.CancelOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.CancelOrderResponse
import com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
import javax.inject.Inject

class CancelOrderItemUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(
        orderId: String,
        productId: String,
        reason: String
    ): Result<CancelOrderResponse> {
        return repository.buyerCancelOrder(CancelOrderRequest(orderId, productId, reason))
    }
}

