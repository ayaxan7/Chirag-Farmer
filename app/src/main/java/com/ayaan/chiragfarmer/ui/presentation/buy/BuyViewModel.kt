package com.ayaan.chiragfarmer.ui.presentation.buy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.chiragfarmer.data.remote.dto.MixedProductItem
import com.ayaan.chiragfarmer.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuyViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BuyUiState>(BuyUiState.Loading)
    val uiState: StateFlow<BuyUiState> = _uiState.asStateFlow()

    init {
        loadMixedProducts()
    }

    private fun loadMixedProducts() {
        viewModelScope.launch {
            _uiState.value = BuyUiState.Loading

            productRepository.getMixedProducts().fold(
                onSuccess = { mixedProductsData ->
                    _uiState.value = BuyUiState.Success(
                        vendorProducts = mixedProductsData.vendorProducts,
                        directFromFarmersProducts = mixedProductsData.directFromFarmersProducts,
                        seedProducts = mixedProductsData.seedProducts,
                        randomProducts = mixedProductsData.randomProducts
                    )
                },
                onFailure = { exception ->
                    _uiState.value = BuyUiState.Error(
                        exception.message ?: "Failed to load products"
                    )
                }
            )
        }
    }

    fun retry() {
        loadMixedProducts()
    }
}

sealed class BuyUiState {
    object Loading : BuyUiState()
    data class Success(
        val vendorProducts: List<MixedProductItem>,
        val directFromFarmersProducts: List<MixedProductItem>,
        val seedProducts: List<MixedProductItem>,
        val randomProducts: List<MixedProductItem>
    ) : BuyUiState()

    data class Error(val message: String) : BuyUiState()
}

