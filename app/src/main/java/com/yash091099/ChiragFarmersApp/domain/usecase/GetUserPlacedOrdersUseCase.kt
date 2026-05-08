package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.UserPlacedOrdersResponse
import com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
import javax.inject.Inject

class GetUserPlacedOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(type: String, page: Int, limit: Int): Result<UserPlacedOrdersResponse> {
        return repository.getUserPlacedOrders(type, page, limit)
    }
}
