package com.ayaan.chiragfarmer.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ayaan.chiragfarmer.data.remote.ProductApiService
import com.ayaan.chiragfarmer.data.remote.dto.toDomain
import com.ayaan.chiragfarmer.domain.model.Product

class SmartFarmingPagingSource(
    private val apiService: ProductApiService,
    private val token: String,
    private val category: String,
    private val subcategory: String?
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
                subcategory = subcategory
            )
            val products = response.data.products.map { it.toDomain() }
            val totalPages = (response.data.total + params.loadSize - 1) / params.loadSize

            LoadResult.Page(
                data = products,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= totalPages || products.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

