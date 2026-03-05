package com.ayaan.chiragfarmer.domain.repository

import androidx.paging.PagingData
import com.ayaan.chiragfarmer.data.remote.dto.AddProductRequest
import com.ayaan.chiragfarmer.data.remote.dto.ProductDetailsData
import com.ayaan.chiragfarmer.data.remote.dto.UpdateProductRequest
import com.ayaan.chiragfarmer.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getFarmerProducts(type: String, search: String?): Flow<PagingData<Product>>
    suspend fun getProductDetails(productId: String): Result<ProductDetailsData>
    suspend fun addProduct(request: AddProductRequest): Result<String>
    suspend fun updateProduct(request: UpdateProductRequest): Result<Unit>
    suspend fun toggleSoldOut(productId: String): Result<Boolean>
    suspend fun deleteProduct(productId: String): Result<Unit>
}
