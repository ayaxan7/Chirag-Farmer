package com.ayaan.chiragfarmer.ui.presentation.sell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.ayaan.chiragfarmer.domain.model.Product
import com.ayaan.chiragfarmer.domain.usecase.GetFarmerProductsUseCase
import com.ayaan.chiragfarmer.domain.usecase.ToggleSoldOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ToggleSoldOutState {
    data object Idle : ToggleSoldOutState()
    data object Loading : ToggleSoldOutState()
    data class Success(val message: String, val isAvailable: Boolean) : ToggleSoldOutState()
    data class Error(val message: String) : ToggleSoldOutState()
}

@HiltViewModel
class SellViewModel @Inject constructor(
    private val getFarmerProductsUseCase: GetFarmerProductsUseCase,
    private val toggleSoldOutUseCase: ToggleSoldOutUseCase
) : ViewModel() {

    private val _activeSearchQuery = MutableStateFlow("")
    private val _soldOutSearchQuery = MutableStateFlow("")

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _toggleState = MutableStateFlow<ToggleSoldOutState>(ToggleSoldOutState.Idle)
    val toggleState: StateFlow<ToggleSoldOutState> = _toggleState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeProducts: Flow<PagingData<Product>> = _activeSearchQuery
        .flatMapLatest { query ->
            getFarmerProductsUseCase(type = "active", search = query.ifEmpty { null })
        }
//        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val soldOutProducts: Flow<PagingData<Product>> = _soldOutSearchQuery
        .flatMapLatest { query ->
            getFarmerProductsUseCase(type = "sold_out", search = query.ifEmpty { null })
        }
//        .cachedIn(viewModelScope)

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun fetchActive(query: String) {
        _activeSearchQuery.value = query
    }

    fun fetchSoldOut(query: String) {
        _soldOutSearchQuery.value = query
    }

    fun toggleSoldOut(productId: String) {
        viewModelScope.launch {
            _toggleState.value = ToggleSoldOutState.Loading

            toggleSoldOutUseCase(productId).onSuccess { isAvailable ->
                val message = if (isAvailable) {
                    "Product marked as Available"
                } else {
                    "Product marked as Sold Out"
                }
                _toggleState.value = ToggleSoldOutState.Success(message, isAvailable)

                // Refresh both lists after toggling
                _activeSearchQuery.value = _activeSearchQuery.value
                _soldOutSearchQuery.value = _soldOutSearchQuery.value
            }.onFailure { error ->
                _toggleState.value = ToggleSoldOutState.Error(
                    error.message ?: "Failed to update product status"
                )
            }
        }
    }

    fun resetToggleState() {
        _toggleState.value = ToggleSoldOutState.Idle
    }
}
