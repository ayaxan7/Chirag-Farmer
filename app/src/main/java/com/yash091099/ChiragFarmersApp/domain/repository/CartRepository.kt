package com.yash091099.ChiragFarmersApp.domain.repository

import com.yash091099.ChiragFarmersApp.data.remote.dto.CartDataWrapper
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateQuantityData

interface CartRepository {
    suspend fun addToCart(productId: String): Result<Int>
    suspend fun getCart(): Result<CartDataWrapper>
    suspend fun updateQuantity(productId: String, action: String): Result<UpdateQuantityData>
    suspend fun removeFromCart(productId: String): Result<Unit>
    suspend fun buyNow(productId: String, quantity: Int = 1): Result<CartDataWrapper>
}

