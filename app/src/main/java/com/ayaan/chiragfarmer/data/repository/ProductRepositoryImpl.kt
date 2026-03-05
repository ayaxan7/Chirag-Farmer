package com.ayaan.chiragfarmer.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ayaan.chiragfarmer.data.local.AuthDataStore
import com.ayaan.chiragfarmer.data.paging.ProductPagingSource
import com.ayaan.chiragfarmer.data.remote.ProductApiService
import com.ayaan.chiragfarmer.data.remote.dto.AddProductRequest
import com.ayaan.chiragfarmer.data.remote.dto.ToggleSoldOutRequest
import com.ayaan.chiragfarmer.domain.model.Product
import com.ayaan.chiragfarmer.domain.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ProductApiService,
    private val authDataStore: AuthDataStore
) : ProductRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFarmerProducts(type: String, search: String?): Flow<PagingData<Product>> {
        return authDataStore.getAuthToken().flatMapLatest { token ->
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    ProductPagingSource(apiService, token ?: "", type, search)
                }
            ).flow
        }
    }

    override suspend fun addProduct(request: AddProductRequest): Result<String> {
        return try {
            val token = authDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.addProduct("Bearer $token", request)
            if (response.success && response.data != null) {
                Result.success(response.data.id)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleSoldOut(productId: String): Result<Boolean> {
        return try {
            val token = authDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.toggleSoldOut(
                "Bearer $token",
                ToggleSoldOutRequest(productId)
            )

            if (response.success && response.data != null) {
                Result.success(response.data.isAvailable)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
