package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.CartDataWrapper
import com.yash091099.ChiragFarmersApp.domain.repository.CartRepository
import javax.inject.Inject

class GetBuyNowUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(productId: String, quantity: Int = 1): Result<CartDataWrapper> {
        return cartRepository.buyNow(productId, quantity)
    }
}

