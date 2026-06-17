package com.yash091099.ChiragFarmersApp.ui.presentation.buy

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.data.remote.dto.MixedProductItem
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuyViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
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
                        exception.message ?: context.getString(R.string.error_failed_load_products)
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

