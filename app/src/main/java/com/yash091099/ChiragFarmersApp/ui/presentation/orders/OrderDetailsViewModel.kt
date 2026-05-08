package com.yash091099.ChiragFarmersApp.ui.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderDetailsData
import com.yash091099.ChiragFarmersApp.domain.usecase.GetOrderDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class OrderDetailsUiState {
    object Loading : OrderDetailsUiState()
    data class Success(val data: OrderDetailsData) : OrderDetailsUiState()
    data class Error(val message: String) : OrderDetailsUiState()
}

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderDetailsUiState>(OrderDetailsUiState.Loading)
    val uiState: StateFlow<OrderDetailsUiState> = _uiState.asStateFlow()

    fun loadOrderDetails(orderId: String) {
        viewModelScope.launch {
            _uiState.value = OrderDetailsUiState.Loading
            getOrderDetailsUseCase(orderId).fold(
                onSuccess = { response ->
                    if (response.success) {
                        _uiState.value = OrderDetailsUiState.Success(response.data)
                    } else {
                        _uiState.value = OrderDetailsUiState.Error(response.message)
                    }
                },
                onFailure = { error ->
                    _uiState.value = OrderDetailsUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }
}
