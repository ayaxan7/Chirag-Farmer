package com.ayaan.chiragfarmer.domain.usecase

import com.ayaan.chiragfarmer.data.remote.dto.ProductDetailsData
import com.ayaan.chiragfarmer.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: String): Result<ProductDetailsData> {
        return productRepository.getProductDetails(productId)
    }
}

