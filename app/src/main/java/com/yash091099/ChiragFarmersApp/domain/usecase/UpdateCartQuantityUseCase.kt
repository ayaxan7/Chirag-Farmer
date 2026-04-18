package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateQuantityData
import com.yash091099.ChiragFarmersApp.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: String, action: String): Result<UpdateQuantityData> {
        return cartRepository.updateQuantity(productId, action)
    }
}

