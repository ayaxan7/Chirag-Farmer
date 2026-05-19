@file:Suppress("unused")

package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductReviewsData
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductReviewsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: String): Result<ProductReviewsData> {
        return productRepository.getProductReviews(productId)
    }
}

