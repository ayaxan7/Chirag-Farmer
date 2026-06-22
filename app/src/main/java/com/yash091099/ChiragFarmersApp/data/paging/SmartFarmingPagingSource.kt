package com.yash091099.ChiragFarmersApp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yash091099.ChiragFarmersApp.data.remote.ProductApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.toDomain
import com.yash091099.ChiragFarmersApp.utils.getErrorMessage
import com.yash091099.ChiragFarmersApp.domain.model.Product

class SmartFarmingPagingSource(
    private val apiService: ProductApiService,
    private val token: String,
    private val category: String,
    private val subcategory: String?,
    private val minPrice: String? = null,
    private val maxPrice: String? = null,
    private val sort: String? = null,
    private val rating: String? = null,
    private val location: String? = null
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getSmartFarmingProducts(
                token = "Bearer $token",
                page = page,
                limit = params.loadSize,
                category = category,
                subcategory = subcategory,
                minPrice = minPrice,
                maxPrice = maxPrice,
                sort = sort,
                rating = rating,
                location = location
            )
            val products = response.data.products.map { it.toDomain() }
            val totalPages = (response.data.total + params.loadSize - 1) / params.loadSize

            LoadResult.Page(
                data = products,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= totalPages || products.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(Exception(getErrorMessage(e)))
        }
    }
}

