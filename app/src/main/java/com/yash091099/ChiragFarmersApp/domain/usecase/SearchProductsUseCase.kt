package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.SearchProductItem
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(query: String): Result<List<SearchProductItem>> {
        return productRepository.searchProducts(query)
    }
}
