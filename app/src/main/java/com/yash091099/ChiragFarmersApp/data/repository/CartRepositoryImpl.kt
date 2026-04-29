package com.yash091099.ChiragFarmersApp.data.repository

import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.CartApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddToCartRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartAddressDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartDataWrapper
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartItemDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartSummary
import com.yash091099.ChiragFarmersApp.data.remote.dto.RemoveFromCartRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateQuantityData
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateQuantityRequest
import com.yash091099.ChiragFarmersApp.domain.repository.CartRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartApiService: CartApiService,
    private val chiragDataStore: ChiragDataStore
) : CartRepository {

    override suspend fun addToCart(productId: String): Result<Int> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
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
            val token = chiragDataStore.getAuthToken().first()
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
            val token = chiragDataStore.getAuthToken().first()
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
            val token = chiragDataStore.getAuthToken().first()
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

    override suspend fun buyNow(productId: String, quantity: Int): Result<CartDataWrapper> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = cartApiService.buyNow(
                "Bearer $token",
                productId,
                quantity
            )

            if (response.success && response.data != null) {
                val buyNowData = response.data
                // Convert BuyNowData to CartDataWrapper format
                val cartItem = CartItemDto(
                    productId = buyNowData.productId,
                    productName = buyNowData.name,
                    productImage = buyNowData.imageUrl,
                    sellerName = buyNowData.sellerName,
                    finalPrice = buyNowData.finalPrice,
                    quantity = buyNowData.quantity
                )

                val cartSummary = CartSummary(
                    subtotal = buyNowData.subtotal,
                    totalDiscount = buyNowData.discount,
                    totalDeliveryFee = buyNowData.deliveryFee,
                    totalAmount = buyNowData.totalCost
                )

                val address = buyNowData.defaultLocation?.let {
                    CartAddressDto(
                        name = it.name ?: "",
                        addressString = it.completeAddress ?: "",
                        pincode = it.pincode ?: ""
                    )
                }

                val cartDataWrapper = CartDataWrapper(
                    items = listOf(cartItem),
                    summary = cartSummary,
                    currentDefaultAddress = address
                )

                Result.success(cartDataWrapper)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

