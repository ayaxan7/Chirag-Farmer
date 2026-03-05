package com.ayaan.chiragfarmer.domain.usecase

import com.ayaan.chiragfarmer.domain.repository.ProductRepository
import javax.inject.Inject

class ToggleSoldOutUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: String): Result<Boolean> {
        return productRepository.toggleSoldOut(productId)
    }
}

