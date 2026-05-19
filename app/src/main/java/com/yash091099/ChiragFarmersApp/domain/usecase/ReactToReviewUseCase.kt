package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.ReviewReactionRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.ReviewReactionResponse
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import javax.inject.Inject

class ReactToReviewUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(request: ReviewReactionRequest): Result<ReviewReactionResponse> {
        return productRepository.reactToReview(request)
    }
}

