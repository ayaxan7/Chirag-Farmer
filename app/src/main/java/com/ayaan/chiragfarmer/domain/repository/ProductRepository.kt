package com.ayaan.chiragfarmer.domain.repository

import androidx.paging.PagingData
import com.ayaan.chiragfarmer.data.remote.dto.AddProductRequest
import com.ayaan.chiragfarmer.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getFarmerProducts(type: String, search: String?): Flow<PagingData<Product>>
    suspend fun addProduct(request: AddProductRequest): Result<String>
}
