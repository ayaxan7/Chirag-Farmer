package com.ayaan.chiragfarmer.domain.usecase

import com.ayaan.chiragfarmer.data.remote.dto.AddProductRequest
import com.ayaan.chiragfarmer.domain.repository.ProductRepository
import javax.inject.Inject

class AddProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(request: AddProductRequest): Result<String> {
        return productRepository.addProduct(request)
    }
}
