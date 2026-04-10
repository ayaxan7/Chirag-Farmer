package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import javax.inject.Inject

class ToggleSoldOutUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: String): Result<Boolean> {
        return productRepository.toggleSoldOut(productId)
    }
}

