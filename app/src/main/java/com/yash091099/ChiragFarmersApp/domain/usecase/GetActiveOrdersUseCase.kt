package com.yash091099.ChiragFarmersApp.domain.usecase

import androidx.paging.PagingData
import com.yash091099.ChiragFarmersApp.domain.model.Order
import com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActiveOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(): Flow<PagingData<Order>> {
        return orderRepository.getActiveOrdersPaged()
    }
}

