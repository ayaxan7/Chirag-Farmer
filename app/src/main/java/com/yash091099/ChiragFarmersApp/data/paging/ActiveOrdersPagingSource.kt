package com.yash091099.ChiragFarmersApp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yash091099.ChiragFarmersApp.data.remote.OrderApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.toDomain
import com.yash091099.ChiragFarmersApp.utils.getErrorMessage
import com.yash091099.ChiragFarmersApp.domain.model.Order

class ActiveOrdersPagingSource(
    private val apiService: OrderApiService,
    private val token: String
) : PagingSource<Int, Order>() {

    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        val page = params.key ?: 1
        return try {
            if (token.isBlank()) {
                return LoadResult.Error(Exception("Authentication token not found"))
            }

            val response = apiService.getActiveOrders(
                token = "Bearer $token",
                page = page,
                limit = params.loadSize
            )

            // Validate API response success flag
            if (!response.success) {
                return LoadResult.Error(Exception(response.message))
            }

            val orders = response.data.orders.map { it.toDomain() }
            val totalPages = response.data.pagination.pages

            LoadResult.Page(
                data = orders,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= totalPages || orders.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(Exception(getErrorMessage(e)))
        }
    }
}
