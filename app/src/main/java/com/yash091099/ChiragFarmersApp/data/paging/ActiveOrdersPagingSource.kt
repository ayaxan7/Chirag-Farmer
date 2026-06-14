package com.yash091099.ChiragFarmersApp.data.paging

import android.os.Build
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yash091099.ChiragFarmersApp.data.remote.OrderApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.toDomain
import com.yash091099.ChiragFarmersApp.utils.getErrorMessage
import com.yash091099.ChiragFarmersApp.domain.model.Order
import timber.log.Timber

class ActiveOrdersPagingSource(
    private val apiService: OrderApiService,
    private val token: String
) : PagingSource<Int, Order>() {

    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        val key = state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
        Timber.tag("ActiveOrders").d("getRefreshKey anchorPosition=%s refreshKey=%s", state.anchorPosition, key)
        return key
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        val page = params.key ?: 1
        Timber.tag("ActiveOrders").d("load page=%s loadSize=%s tokenPresent=%s device=%s SDK=%s", page, params.loadSize, token.isNotBlank(), Build.MODEL, Build.VERSION.SDK_INT)

        return try {
            if (token.isBlank()) {
                Timber.tag("ActiveOrders").e("token is blank")
                return LoadResult.Error(Exception("Authentication token not found"))
            }

            Timber.tag("ActiveOrders").d("API call active-orders page=%s limit=%s", page, params.loadSize)
            val response = apiService.getActiveOrders(
                token = "Bearer $token",
                page = page,
                limit = params.loadSize
            )

            Timber.tag("ActiveOrders").d("API response success=%s message=%s | full=%s", response.success, response.message, response)

            if (!response.success) {
                Timber.tag("ActiveOrders").e("API success=false message=%s", response.message)
                return LoadResult.Error(Exception(response.message))
            }

            val orders = response.data.orders.map { it.toDomain() }
            val totalPages = response.data.pagination.pages
            Timber.tag("ActiveOrders").d("loaded %s orders totalPages=%s page=%s totalOrders=%s", orders.size, totalPages, page, response.data.pagination.total)

            LoadResult.Page(
                data = orders,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= totalPages || orders.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Timber.tag("ActiveOrders").e(e, "load failed page=%s", page)
            LoadResult.Error(Exception(getErrorMessage(e)))
        }
    }
}
