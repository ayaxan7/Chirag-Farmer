package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.CartDataWrapper
import com.yash091099.ChiragFarmersApp.domain.repository.CartRepository
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): Result<CartDataWrapper> {
        return cartRepository.getCart()
    }
}

