package com.yash091099.ChiragFarmersApp.data.repository

import com.yash091099.ChiragFarmersApp.data.local.AuthDataStore
import com.yash091099.ChiragFarmersApp.data.remote.CartApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddToCartRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartDataWrapper
import com.yash091099.ChiragFarmersApp.data.remote.dto.RemoveFromCartRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateQuantityData
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateQuantityRequest
import com.yash091099.ChiragFarmersApp.domain.repository.CartRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartApiService: CartApiService,
    private val authDataStore: AuthDataStore
) : CartRepository {

    override suspend fun addToCart(productId: String): Result<Int> {
        return try {
            val token = authDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = cartApiService.addToCart(
                "Bearer $token",
                AddToCartRequest(productId)
            )

            if (response.success && response.data != null) {
                Result.success(response.data.cartItemsCount)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCart(): Result<CartDataWrapper> {
        return try {
            val token = authDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = cartApiService.getCart("Bearer $token")

            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateQuantity(productId: String, action: String): Result<UpdateQuantityData> {
        return try {
            val token = authDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = cartApiService.updateQuantity(
                "Bearer $token",
                UpdateQuantityRequest(productId, action)
            )

            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFromCart(productId: String): Result<Unit> {
        return try {
            val token = authDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = cartApiService.removeFromCart(
                "Bearer $token",
                RemoveFromCartRequest(productId)
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

