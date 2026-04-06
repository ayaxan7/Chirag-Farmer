package com.ayaan.chiragfarmer.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ayaan.chiragfarmer.data.local.AuthDataStore
import com.ayaan.chiragfarmer.data.paging.AllProductsPagingSource
import com.ayaan.chiragfarmer.data.paging.ProductPagingSource
import com.ayaan.chiragfarmer.data.paging.SmartFarmingPagingSource
import com.ayaan.chiragfarmer.data.remote.ProductApiService
import com.ayaan.chiragfarmer.data.remote.dto.AddProductRequest
import com.ayaan.chiragfarmer.data.remote.dto.DeleteProductRequest
import com.ayaan.chiragfarmer.data.remote.dto.MixedProductsData
import com.ayaan.chiragfarmer.data.remote.dto.ProductDetailsData
import com.ayaan.chiragfarmer.data.remote.dto.ToggleSoldOutRequest
import com.ayaan.chiragfarmer.data.remote.dto.UpdateProductRequest
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
                    initialLoadSize = 10,
                    prefetchDistance = 1,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    ProductPagingSource(apiService, token ?: "", type, search)
                }
            ).flow
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllProducts(
        category: String,
        subcategory: String?
    ): Flow<PagingData<Product>> {
        return authDataStore.getAuthToken().flatMapLatest { token ->
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    initialLoadSize = 10,
                    prefetchDistance = 1,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    AllProductsPagingSource(
                        apiService = apiService,
                        token = token ?: "",
                        category = category,
                        subcategory = subcategory
                    )
                }
            ).flow
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getSmartFarmingProducts(
        category: String,
        subcategory: String?
    ): Flow<PagingData<Product>> {
        return authDataStore.getAuthToken().flatMapLatest { token ->
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    initialLoadSize = 10,
                    prefetchDistance = 1,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    SmartFarmingPagingSource(
                        apiService = apiService,
                        token = token ?: "",
                        category = category,
                        subcategory = subcategory
                    )
                }
            ).flow
        }
    }

     override suspend fun getMixedProducts(): Result<MixedProductsData> {
         return try {
             val token = authDataStore.getAuthToken().first()
             if (token.isNullOrEmpty()) {
                 return Result.failure(Exception("Authentication token not found"))
             }

             val response = apiService.getMixedProducts("Bearer $token")
             if (response.success && response.data != null) {
                 Result.success(response.data)
             } else {
                 Result.failure(Exception(response.message))
             }
         } catch (e: Exception) {
             Result.failure(e)
         }
     }

     override suspend fun getMixedProductsForHomeScreen(): Result<MixedProductsData> {
         return try {
             val token = authDataStore.getAuthToken().first()
             if (token.isNullOrEmpty()) {
                 return Result.failure(Exception("Authentication token not found"))
             }

             val response = apiService.getMixedProducts("Bearer $token", screen = "homeScreen")
             if (response.success && response.data != null) {
                 Result.success(response.data)
             } else {
                 Result.failure(Exception(response.message))
             }
         } catch (e: Exception) {
             Result.failure(e)
         }
     }

    override suspend fun getProductDetails(productId: String): Result<ProductDetailsData> {
        return try {
            val token = authDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.getProductDetails("Bearer $token", productId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
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

    override suspend fun updateProduct(request: UpdateProductRequest): Result<Unit> {
        return try {
            val token = authDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.updateProduct("Bearer $token", request)
            if (response.success) {
                Result.success(Unit)
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

    override suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            val token = authDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.deleteProduct(
                "Bearer $token",
                DeleteProductRequest(productId)
            )

            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
