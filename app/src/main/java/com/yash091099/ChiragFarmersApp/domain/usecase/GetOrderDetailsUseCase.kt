package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderDetailsResponse
import com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderDetailsUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(id: String): Result<OrderDetailsResponse> {
        return repository.getOrderDetails(id)
    }
}
