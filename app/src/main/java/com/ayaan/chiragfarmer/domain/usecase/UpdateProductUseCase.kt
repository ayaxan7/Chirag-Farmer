package com.ayaan.chiragfarmer.domain.usecase

import com.ayaan.chiragfarmer.data.remote.dto.UpdateProductRequest
import com.ayaan.chiragfarmer.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(request: UpdateProductRequest): Result<Unit> {
        return productRepository.updateProduct(request)
    }
}

