package com.yash091099.ChiragFarmersApp.ui.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartItemDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartSummary
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
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase
) : ViewModel() {

    private val _cartState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val cartState: StateFlow<CartUiState> = _cartState.asStateFlow()

    init {
        loadCart()
    }
    
    fun deleteItem(productId: String) {
        viewModelScope.launch {
            removeFromCartUseCase(productId).fold(
                onSuccess = { _ ->
                    loadCart()  // Refresh cart after successful deletion
                },
                onFailure = { exception ->
                    println("Failed to delete item: ${exception.message}")
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
                        CartUiState.Success(cartData.items, cartData.summary)
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

    fun retry() {
        loadCart()
    }

    fun incrementQuantity(productId: String) {
        viewModelScope.launch {
            updateCartQuantityUseCase(productId, "increment").fold(
                onSuccess = { _ ->
                    loadCart()  // Refresh cart after successful update
                },
                onFailure = { exception ->
                    // Handle error silently, cart will show current state
                    println("Failed to increment: ${exception.message}")
                }
            )
        }
    }

    fun decrementQuantity(productId: String) {
        viewModelScope.launch {
            updateCartQuantityUseCase(productId, "decrement").fold(
                onSuccess = { _ ->
                    loadCart()  // Refresh cart after successful update
                },
                onFailure = { exception ->
                    // Handle error silently, cart will show current state
                    println("Failed to decrement: ${exception.message}")
                }
            )
        }
    }

    fun refreshCart() {
        loadCart()
    }
}

sealed class CartUiState {
    object Loading : CartUiState()
    data class Success(val cartItems: List<CartItemDto>, val summary: CartSummary) : CartUiState()
    object Empty : CartUiState()
    data class Error(val message: String) : CartUiState()
}

