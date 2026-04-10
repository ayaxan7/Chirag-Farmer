package com.yash091099.ChiragFarmersApp.ui.presentation.sell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.yash091099.ChiragFarmersApp.domain.model.Product
import com.yash091099.ChiragFarmersApp.domain.usecase.DeleteProductUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.GetFarmerProductsUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.ToggleSoldOutUseCase
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

sealed class DeleteProductState {
    data object Idle : DeleteProductState()
    data object Loading : DeleteProductState()
    data class Success(val message: String) : DeleteProductState()
    data class Error(val message: String) : DeleteProductState()
}
@HiltViewModel
class SellViewModel @Inject constructor(
    private val getFarmerProductsUseCase: GetFarmerProductsUseCase,
    private val toggleSoldOutUseCase: ToggleSoldOutUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {

    private val _activeSearchQuery = MutableStateFlow("")
    private val _soldOutSearchQuery = MutableStateFlow("")

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _toggleState = MutableStateFlow<ToggleSoldOutState>(ToggleSoldOutState.Idle)
    val toggleState: StateFlow<ToggleSoldOutState> = _toggleState.asStateFlow()

    private val _deleteState = MutableStateFlow<DeleteProductState>(DeleteProductState.Idle)
    val deleteState: StateFlow<DeleteProductState> = _deleteState.asStateFlow()

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

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            _deleteState.value = DeleteProductState.Loading

            deleteProductUseCase(productId).onSuccess {
                _deleteState.value = DeleteProductState.Success("Product deleted successfully")

                // Refresh both lists after deletion
                _activeSearchQuery.value = _activeSearchQuery.value
                _soldOutSearchQuery.value = _soldOutSearchQuery.value
            }.onFailure { error ->
                _deleteState.value = DeleteProductState.Error(
                    error.message ?: "Failed to delete product"
                )
            }
        }
    }

    fun resetToggleState() {
        _toggleState.value = ToggleSoldOutState.Idle
    }

    fun resetDeleteState() {
        _deleteState.value = DeleteProductState.Idle
    }
}
