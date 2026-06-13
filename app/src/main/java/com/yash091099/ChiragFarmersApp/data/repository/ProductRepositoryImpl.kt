package com.yash091099.ChiragFarmersApp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.paging.AllProductsPagingSource
import com.yash091099.ChiragFarmersApp.data.paging.ProductPagingSource
import com.yash091099.ChiragFarmersApp.data.paging.SmartFarmingPagingSource
import com.yash091099.ChiragFarmersApp.data.remote.ProductApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.DeleteProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.MixedProductsData
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductReviewsData
import com.yash091099.ChiragFarmersApp.data.remote.dto.RateProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.RateProductResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.ReviewReactionRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.ReviewReactionResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductDetailedData
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductDetailsData
import com.yash091099.ChiragFarmersApp.data.remote.dto.SearchProductItem
import com.yash091099.ChiragFarmersApp.data.remote.dto.SellerDetailsData
import com.yash091099.ChiragFarmersApp.data.remote.dto.ToggleSoldOutRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateProductRequest
import com.yash091099.ChiragFarmersApp.utils.getErrorMessage
import com.yash091099.ChiragFarmersApp.domain.model.Product
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ProductApiService, private val chiragDataStore: ChiragDataStore
) : ProductRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFarmerProducts(type: String, search: String?): Flow<PagingData<Product>> {
        return chiragDataStore.getAuthToken().flatMapLatest { token ->
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    initialLoadSize = 10,
                    prefetchDistance = 1,
                    enablePlaceholders = false
                ), pagingSourceFactory = {
                    ProductPagingSource(apiService, token ?: "", type, search)
                }).flow
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllProducts(
        category: String, subcategory: String?
    ): Flow<PagingData<Product>> {
        return chiragDataStore.getAuthToken().flatMapLatest { token ->
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    initialLoadSize = 10,
                    prefetchDistance = 1,
                    enablePlaceholders = false
                ), pagingSourceFactory = {
                    AllProductsPagingSource(
                        apiService = apiService,
                        token = token ?: "",
                        category = category,
                        subcategory = subcategory
                    )
                }).flow
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getSmartFarmingProducts(
        category: String, subcategory: String?
    ): Flow<PagingData<Product>> {
        return chiragDataStore.getAuthToken().flatMapLatest { token ->
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    initialLoadSize = 10,
                    prefetchDistance = 1,
                    enablePlaceholders = false
                ), pagingSourceFactory = {
                    SmartFarmingPagingSource(
                        apiService = apiService,
                        token = token ?: "",
                        category = category,
                        subcategory = subcategory
                    )
                }).flow
        }
    }

    override suspend fun getMixedProducts(): Result<MixedProductsData> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.getMixedProducts("Bearer $token")
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun getMixedProductsForHomeScreen(): Result<MixedProductsData> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.getMixedProducts("Bearer $token")
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun getProductDetails(productId: String): Result<ProductDetailsData> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
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
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun getProductDetailsDetailed(productId: String): Result<ProductDetailedData> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.getProductDetailsDetailed("Bearer $token", productId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun getProductReviews(productId: String): Result<ProductReviewsData> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.getProductReviews("Bearer $token", productId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun reactToReview(request: ReviewReactionRequest): Result<ReviewReactionResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.reactToReview("Bearer $token", request)
            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun addProduct(request: AddProductRequest): Result<String> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
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
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun updateProduct(request: UpdateProductRequest): Result<Unit> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
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
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun toggleSoldOut(productId: String): Result<Boolean> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.toggleSoldOut(
                "Bearer $token", ToggleSoldOutRequest(productId)
            )

            if (response.success && response.data != null) {
                Result.success(response.data.isAvailable)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.deleteProduct(
                "Bearer $token", DeleteProductRequest(productId)
            )

            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }
    override suspend fun searchProducts(query: String): Result<List<SearchProductItem>> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.searchProducts("Bearer $token", query)
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun getSellerDetails(
        sellerId: String,
        page: Int,
        limit: Int
    ): Result<SellerDetailsData> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.getSellerDetails("Bearer $token", sellerId, page, limit)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

    override suspend fun rateProduct(request: RateProductRequest): Result<RateProductResponse> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.rateProduct("Bearer $token", request)
            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception(getErrorMessage(e)))
        }
    }

}
