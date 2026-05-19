package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.RateProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.RateProductResponse
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import javax.inject.Inject

class RateProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(request: RateProductRequest): Result<RateProductResponse> {
        return productRepository.rateProduct(request)
    }
}

