package com.yash091099.ChiragFarmersApp.data

import com.yash091099.ChiragFarmersApp.data.remote.dto.CartItemDto
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class CartData(
    val items: List<CartItemDto> = emptyList(),
    val shippingAddress: String = ""
)

@Singleton
class CartDataCache @Inject constructor() {
    private val _cartData = MutableStateFlow<CartData?>(null)

    fun setCartData(items: List<CartItemDto>, address: String) {
        _cartData.value = CartData(items = items, shippingAddress = address)
    }

    fun getCartData(): CartData? {
        return _cartData.value
    }

    fun clearCartData() {
        _cartData.value = null
    }
}

