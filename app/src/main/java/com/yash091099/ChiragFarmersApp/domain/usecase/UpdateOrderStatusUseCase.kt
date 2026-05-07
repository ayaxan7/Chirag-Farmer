package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateOrderStatusRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateOrderStatusResponse
import com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
import javax.inject.Inject

class UpdateOrderStatusUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(id: String, status: String): Result<UpdateOrderStatusResponse> {
        return repository.updateOrderStatus(id, UpdateOrderStatusRequest(status))
    }
}
