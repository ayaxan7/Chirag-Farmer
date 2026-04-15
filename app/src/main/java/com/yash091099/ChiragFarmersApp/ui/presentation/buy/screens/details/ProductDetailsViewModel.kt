package com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductDetailedData
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    private val _cartState = MutableStateFlow<CartActionState>(CartActionState.Idle)
    val cartState: StateFlow<CartActionState> = _cartState.asStateFlow()

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    init {
        loadProductDetails()
    }

    private fun loadProductDetails() {
        viewModelScope.launch {
            _uiState.value = ProductDetailsUiState.Loading

            productRepository.getProductDetailsDetailed(productId).fold(
                onSuccess = { productDetails ->
                    _uiState.value = ProductDetailsUiState.Success(productDetails)
                },
                onFailure = { exception ->
                    _uiState.value = ProductDetailsUiState.Error(
                        exception.message ?: "Failed to load product details"
                    )
                }
            )
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            _cartState.value = CartActionState.Loading
            productRepository.addToCart(productId).fold(
                onSuccess = { cartItemsCount ->
                    // Refresh product details to get updated isInCart status
                    loadProductDetails()
                    _cartState.value = CartActionState.Success(cartItemsCount)
                },
                onFailure = { exception ->
                    _cartState.value = CartActionState.Error(
                        exception.message ?: "Failed to add product to cart"
                    )
                }
            )
        }
    }

    fun retry() {
        loadProductDetails()
    }

    fun resetCartState() {
        _cartState.value = CartActionState.Idle
    }
}

sealed class ProductDetailsUiState {
    object Loading : ProductDetailsUiState()
    data class Success(val productDetails: ProductDetailedData) : ProductDetailsUiState()
    data class Error(val message: String) : ProductDetailsUiState()
}

sealed class CartActionState {
    object Idle : CartActionState()
    object Loading : CartActionState()
    data class Success(val cartItemsCount: Int) : CartActionState()
    data class Error(val message: String) : CartActionState()
}


