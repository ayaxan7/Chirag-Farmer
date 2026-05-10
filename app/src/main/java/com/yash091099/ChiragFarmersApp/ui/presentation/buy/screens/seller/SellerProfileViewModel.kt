package com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.seller

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.SellerDetailsData
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerProfileViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<SellerProfileUiState>(SellerProfileUiState.Loading)
    val uiState: StateFlow<SellerProfileUiState> = _uiState.asStateFlow()

    private val sellerId: String = checkNotNull(savedStateHandle["sellerId"])

    init {
        loadSellerDetails()
    }

    fun loadSellerDetails() {
        viewModelScope.launch {
            _uiState.value = SellerProfileUiState.Loading
            productRepository.getSellerDetails(sellerId).fold(
                onSuccess = { sellerDetails ->
                    _uiState.value = SellerProfileUiState.Success(sellerDetails)
                },
                onFailure = { exception ->
                    _uiState.value = SellerProfileUiState.Error(
                        exception.message ?: "Failed to load seller details"
                    )
                }
            )
        }
    }

    fun retry() {
        loadSellerDetails()
    }
}

sealed class SellerProfileUiState {
    object Loading : SellerProfileUiState()
    data class Success(val sellerDetails: SellerDetailsData) : SellerProfileUiState()
    data class Error(val message: String) : SellerProfileUiState()
}
