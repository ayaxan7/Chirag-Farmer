package com.ayaan.chiragfarmer.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ayaan.chiragfarmer.data.local.AuthDataStore
import com.ayaan.chiragfarmer.data.paging.ProductPagingSource
import com.ayaan.chiragfarmer.data.remote.ProductApiService
import com.ayaan.chiragfarmer.domain.model.Product
import com.ayaan.chiragfarmer.domain.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
}
