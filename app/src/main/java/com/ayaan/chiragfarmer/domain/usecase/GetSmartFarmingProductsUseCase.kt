package com.ayaan.chiragfarmer.domain.usecase

import androidx.paging.PagingData
import com.ayaan.chiragfarmer.domain.model.Product
import com.ayaan.chiragfarmer.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSmartFarmingProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(category: String, subcategory: String? = null): Flow<PagingData<Product>> {
        return repository.getSmartFarmingProducts(category, subcategory)
    }
}

