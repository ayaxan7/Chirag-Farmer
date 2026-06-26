package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderTrackingDto
import com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderTrackingUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(id: String, productId: String? = null): Result<OrderTrackingDto> {
        return repository.getOrderTracking(id, productId)
    }
}
