package com.yash091099.ChiragFarmersApp.ui.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.UserPlacedOrder
import com.yash091099.ChiragFarmersApp.domain.usecase.GetUserPlacedOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MyOrdersUiState {
    object Idle : MyOrdersUiState()
    object Loading : MyOrdersUiState()
    data class Success(val orders: List<UserPlacedOrder>) : MyOrdersUiState()
    data class Error(val message: String) : MyOrdersUiState()
}

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val getUserPlacedOrdersUseCase: GetUserPlacedOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyOrdersUiState>(MyOrdersUiState.Idle)
    val uiState: StateFlow<MyOrdersUiState> = _uiState.asStateFlow()

    private val _currentType = MutableStateFlow("active")
    val currentType: StateFlow<String> = _currentType.asStateFlow()

    init {
        fetchOrders("active")
    }

    fun onTabSelected(index: Int) {
        val type = when (index) {
            0 -> "active"
            1 -> "complete"
            2 -> "cancelled"
            else -> "active"
        }
        if (_currentType.value != type) {
            _currentType.value = type
            fetchOrders(type)
        }
    }

    fun fetchOrders(type: String) {
        viewModelScope.launch {
            _uiState.value = MyOrdersUiState.Loading
            getUserPlacedOrdersUseCase(type, page = 1, limit = 50).fold(
                onSuccess = { response ->
                    if (response.success) {
                        _uiState.value = MyOrdersUiState.Success(response.data.orders)
                    } else {
                        _uiState.value = MyOrdersUiState.Error(response.message)
                    }
                },
                onFailure = { error ->
                    _uiState.value = MyOrdersUiState.Error(error.message ?: "An unknown error occurred")
                }
            )
        }
    }
}
