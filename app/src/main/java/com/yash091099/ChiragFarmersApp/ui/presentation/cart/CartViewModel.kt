package com.yash091099.ChiragFarmersApp.ui.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartItemDto
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _cartState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val cartState: StateFlow<CartUiState> = _cartState.asStateFlow()

    init {
        loadCart()
    }

    private fun loadCart() {
        viewModelScope.launch {
            _cartState.value = CartUiState.Loading

            productRepository.getCart().fold(
                onSuccess = { cartItems ->
                    _cartState.value = if (cartItems.isEmpty()) {
                        CartUiState.Empty
                    } else {
                        CartUiState.Success(cartItems)
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
            productRepository.updateQuantity(productId, "increment").fold(
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
            productRepository.updateQuantity(productId, "decrement").fold(
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
    data class Success(val cartItems: List<CartItemDto>) : CartUiState()
    object Empty : CartUiState()
    data class Error(val message: String) : CartUiState()
}

