package com.yash091099.ChiragFarmersApp.ui.presentation.sell.screens.orderstatus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderTrackingData
import com.yash091099.ChiragFarmersApp.domain.usecase.GetOrderTrackingUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.UpdateOrderStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderStatusViewModel @Inject constructor(
    private val getOrderTrackingUseCase: GetOrderTrackingUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<OrderStatusState>(OrderStatusState.Loading)
    val state: StateFlow<OrderStatusState> = _state.asStateFlow()

    fun getOrderTracking(id: String) {
        viewModelScope.launch {
            _state.value = OrderStatusState.Loading
            getOrderTrackingUseCase(id).onSuccess { response ->
                _state.value = OrderStatusState.Success(response.data)
            }.onFailure { error ->
                _state.value = OrderStatusState.Error(error.message ?: "An unknown error occurred")
            }
        }
    }

    fun updateOrderStatus(id: String, productId: String, status: String) {
        viewModelScope.launch {
            updateOrderStatusUseCase(id, productId, status).onSuccess { response ->
                // Always re-fetch the full tracking details on success to ensure all fields are populated correctly
                if (response.success) {
                    getOrderTracking(id)
                }
            }.onFailure { error ->
                // Handle error
            }
        }
    }
}

sealed class OrderStatusState {
    object Loading : OrderStatusState()
    data class Success(val data: OrderTrackingData) : OrderStatusState()
    data class Error(val message: String) : OrderStatusState()
}
