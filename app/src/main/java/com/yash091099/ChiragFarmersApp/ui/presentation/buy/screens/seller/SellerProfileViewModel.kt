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
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.yash091099.ChiragFarmersApp.R
import javax.inject.Inject

@HiltViewModel
class SellerProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<SellerProfileUiState>(SellerProfileUiState.Loading)
    val uiState: StateFlow<SellerProfileUiState> = _uiState.asStateFlow()

    private val sellerId: String = checkNotNull(savedStateHandle["sellerId"])
    private var currentFilter = SellerFilterState()

    init {
        loadSellerDetails()
    }

    fun loadSellerDetails() {
        viewModelScope.launch {
            _uiState.value = SellerProfileUiState.Loading
            val minPrice = currentFilter.minBudget.takeIf { it.isNotEmpty() }
            val maxPrice = currentFilter.maxBudget.takeIf { it.isNotEmpty() }
            val sort = when {
                currentFilter.sortByLatest -> "latest"
                currentFilter.sortByEarliest -> "earliest"
                else -> null
            }
            productRepository.getSellerDetails(sellerId, minPrice = minPrice, maxPrice = maxPrice, sort = sort).fold(
                onSuccess = { sellerDetails ->
                    _uiState.value = SellerProfileUiState.Success(sellerDetails)
                },
                onFailure = { exception ->
                    _uiState.value = SellerProfileUiState.Error(
                        exception.message ?: context.getString(R.string.error_failed_load_seller)
                    )
                }
            )
        }
    }

    fun applyFilter(filter: SellerFilterState) {
        currentFilter = filter
        loadSellerDetails()
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
