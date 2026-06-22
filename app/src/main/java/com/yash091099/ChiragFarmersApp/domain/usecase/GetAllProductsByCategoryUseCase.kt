package com.yash091099.ChiragFarmersApp.domain.usecase

import androidx.paging.PagingData
import com.yash091099.ChiragFarmersApp.domain.model.Product
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProductsByCategoryUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(category: String, subcategory: String? = null, minPrice: String? = null, maxPrice: String? = null, sort: String? = null, rating: String? = null, location: String? = null): Flow<PagingData<Product>> {
        return repository.getAllProducts(category, subcategory, minPrice, maxPrice, sort, rating, location)
    }
}

