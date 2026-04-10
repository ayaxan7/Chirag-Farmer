package com.yash091099.ChiragFarmersApp.domain.repository

import androidx.paging.PagingData
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.MixedProductsData
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductDetailsData
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductDetailedData
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateProductRequest
import com.yash091099.ChiragFarmersApp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getFarmerProducts(type: String, search: String?): Flow<PagingData<Product>>
    fun getAllProducts(category: String, subcategory: String?): Flow<PagingData<Product>>
    fun getSmartFarmingProducts(category: String, subcategory: String?): Flow<PagingData<Product>>
    suspend fun getMixedProducts(): Result<MixedProductsData>
    suspend fun getMixedProductsForHomeScreen(): Result<MixedProductsData>
    suspend fun getProductDetails(productId: String): Result<ProductDetailsData>
    suspend fun getProductDetailsDetailed(productId: String): Result<ProductDetailedData>
    suspend fun addProduct(request: AddProductRequest): Result<String>
    suspend fun updateProduct(request: UpdateProductRequest): Result<Unit>
    suspend fun toggleSoldOut(productId: String): Result<Boolean>
    suspend fun deleteProduct(productId: String): Result<Unit>
}
