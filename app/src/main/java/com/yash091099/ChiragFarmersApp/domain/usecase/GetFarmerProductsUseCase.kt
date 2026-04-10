package com.yash091099.ChiragFarmersApp.domain.usecase

import androidx.paging.PagingData
import com.yash091099.ChiragFarmersApp.domain.model.Product
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFarmerProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(type: String, search: String? = null): Flow<PagingData<Product>> {
        return repository.getFarmerProducts(type, search)
    }
}
