package com.ayaan.chiragfarmer.domain.usecase

import androidx.paging.PagingData
import com.ayaan.chiragfarmer.domain.model.Product
import com.ayaan.chiragfarmer.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFarmerProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(type: String, search: String? = null): Flow<PagingData<Product>> {
        return repository.getFarmerProducts(type, search)
    }
}
