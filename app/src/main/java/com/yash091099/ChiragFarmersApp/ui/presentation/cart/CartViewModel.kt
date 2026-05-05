package com.yash091099.ChiragFarmersApp.ui.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartAddressDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartItemDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartSummary
import com.yash091099.ChiragFarmersApp.data.CartDataCache
import com.yash091099.ChiragFarmersApp.domain.usecase.GetBuyNowUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.GetCartUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.RemoveFromCartUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.UpdateCartQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val getBuyNowUseCase: GetBuyNowUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val cartDataCache: CartDataCache
) : ViewModel() {

    private val _cartState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val cartState: StateFlow<CartUiState> = _cartState.asStateFlow()

    // Separate loading state for operations (quantity update, delete) - does not cause full screen refresh
    private val _isOperationInProgress = MutableStateFlow(false)
    val isOperationInProgress: StateFlow<Boolean> = _isOperationInProgress.asStateFlow()

//    init {
//        loadCart()
//    }
    fun initLoadCart(){
        loadCart()
    }
    fun initBuyNow(productId: String, quantity: Int = 1) {
        loadBuyNow(productId, quantity)
    }

    fun deleteItem(productId: String) {
        viewModelScope.launch {
            _isOperationInProgress.value = true
            removeFromCartUseCase(productId).fold(
                onSuccess = { _ ->
                    loadCartSilently()  // Update without full UI refresh
                },
                onFailure = { exception ->
                    _isOperationInProgress.value = false
                    println("Failed to delete item: ${exception.message}")
                }
            )
        }
    }

    private fun loadBuyNow(productId: String, quantity: Int) {
        viewModelScope.launch {
            _cartState.value = CartUiState.Loading

            getBuyNowUseCase(productId, quantity).fold(
                onSuccess = { cartData ->
                    _cartState.value = if (cartData.items.isEmpty()) {
                        CartUiState.Empty
                    } else {
                        CartUiState.Success(cartData.items, cartData.summary, cartData.currentDefaultAddress)
                    }
                },
                onFailure = { exception ->
                    _cartState.value = CartUiState.Error(
                        exception.message ?: "Failed to load buy now checkout"
                    )
                }
            )
        }
    }

    private fun loadCart() {
        viewModelScope.launch {
            _cartState.value = CartUiState.Loading

            getCartUseCase().fold(
                onSuccess = { cartData ->
                    _cartState.value = if (cartData.items.isEmpty()) {
                        CartUiState.Empty
                    } else {
                        CartUiState.Success(cartData.items, cartData.summary, cartData.currentDefaultAddress)
                    }
                },
                onFailure = { exception ->
                    _cartState.value = CartUiState.Error(
                        exception.message ?: "Failed to load cart"
                    )
                }
            )
        }
    }

    // Load cart silently without showing full screen loading indicator
    // Only updates the Success state if already loaded
    private fun loadCartSilently() {
        viewModelScope.launch {
            getCartUseCase().fold(
                onSuccess = { cartData ->
                    val newState = if (cartData.items.isEmpty()) {
                        CartUiState.Empty
                    } else {
                        CartUiState.Success(cartData.items, cartData.summary, cartData.currentDefaultAddress)
                    }
                    _cartState.value = newState
                    _isOperationInProgress.value = false
                },
                onFailure = { exception ->
                    _isOperationInProgress.value = false
                    println("Failed to load cart: ${exception.message}")
                }
            )
        }
    }

    fun retry() {
        loadCart()
    }

    fun incrementQuantity(productId: String) {
        viewModelScope.launch {
            _isOperationInProgress.value = true
            updateCartQuantityUseCase(productId, "increment").fold(
                onSuccess = { _ ->
                    loadCartSilently()  // Update without full UI refresh
                },
                onFailure = { exception ->
                    _isOperationInProgress.value = false
                    println("Failed to increment: ${exception.message}")
                }
            )
        }
    }

    fun decrementQuantity(productId: String) {
        viewModelScope.launch {
            _isOperationInProgress.value = true
            updateCartQuantityUseCase(productId, "decrement").fold(
                onSuccess = { _ ->
                    loadCartSilently()  // Update without full UI refresh
                },
                onFailure = { exception ->
                    _isOperationInProgress.value = false
                    println("Failed to decrement: ${exception.message}")
                }
            )
        }
    }

    fun refreshCart() {
        loadCart()
    }

    fun cacheCartDataForPayment(cartItems: List<CartItemDto>, address: String) {
        cartDataCache.setCartData(cartItems, address)
    }

    fun clearCachedCartData() {
        cartDataCache.clearCartData()
    }
}

sealed class CartUiState {
    object Loading : CartUiState()
    data class Success(
        val cartItems: List<CartItemDto>,
        val summary: CartSummary,
        val address: CartAddressDto? = null
    ) : CartUiState()
    object Empty : CartUiState()
    data class Error(val message: String) : CartUiState()
}

