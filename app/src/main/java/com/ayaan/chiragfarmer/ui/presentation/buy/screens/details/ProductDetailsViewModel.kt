package com.ayaan.chiragfarmer.ui.presentation.buy.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.chiragfarmer.data.remote.dto.ProductDetailedData
import com.ayaan.chiragfarmer.domain.repository.ProductRepository
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

    fun retry() {
        loadProductDetails()
    }
}

sealed class ProductDetailsUiState {
    object Loading : ProductDetailsUiState()
    data class Success(val productDetails: ProductDetailedData) : ProductDetailsUiState()
    data class Error(val message: String) : ProductDetailsUiState()
}

