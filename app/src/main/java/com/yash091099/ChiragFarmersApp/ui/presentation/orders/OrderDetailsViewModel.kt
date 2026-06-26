package com.yash091099.ChiragFarmersApp.ui.presentation.orders

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderDetailsData
import com.yash091099.ChiragFarmersApp.domain.usecase.GetOrderDetailsUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.CancelOrderItemUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.UpdateOrderStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import com.yash091099.ChiragFarmersApp.R
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class OrderDetailsUiState {
    object Loading : OrderDetailsUiState()
    data class Success(val data: OrderDetailsData) : OrderDetailsUiState()
    data class Error(val message: String) : OrderDetailsUiState()
}

sealed class CancelOrderState {
    object Idle : CancelOrderState()
    object Loading : CancelOrderState()
    data class Success(val message: String) : CancelOrderState()
    data class Error(val message: String) : CancelOrderState()
}

sealed class UpdateStatusState {
    object Idle : UpdateStatusState()
    object Loading : UpdateStatusState()
    data class Success(val message: String) : UpdateStatusState()
    data class Error(val message: String) : UpdateStatusState()
}

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase,
    private val cancelOrderItemUseCase: CancelOrderItemUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderDetailsUiState>(OrderDetailsUiState.Loading)
    val uiState: StateFlow<OrderDetailsUiState> = _uiState.asStateFlow()

    private val _cancelOrderState = MutableStateFlow<CancelOrderState>(CancelOrderState.Idle)
    val cancelOrderState: StateFlow<CancelOrderState> = _cancelOrderState.asStateFlow()

    private val _updateStatusState = MutableStateFlow<UpdateStatusState>(UpdateStatusState.Idle)
    val updateStatusState: StateFlow<UpdateStatusState> = _updateStatusState.asStateFlow()

    fun loadOrderDetails(orderId: String, productId: String? = null) {
        viewModelScope.launch {
            _uiState.value = OrderDetailsUiState.Loading
            getOrderDetailsUseCase(orderId, productId).fold(
                onSuccess = { response ->
                    if (response.success) {
                        _uiState.value = OrderDetailsUiState.Success(response.data)
                    } else {
                        _uiState.value = OrderDetailsUiState.Error(response.message)
                    }
                },
                onFailure = { error ->
                    _uiState.value = OrderDetailsUiState.Error(error.message ?: context.getString(R.string.error_unknown))
                }
            )
        }
    }

    fun cancelOrderItem(orderId: String, productId: String, reason: String) {
        viewModelScope.launch {
            _cancelOrderState.value = CancelOrderState.Loading
            cancelOrderItemUseCase(orderId, productId, reason).fold(
                onSuccess = { response ->
                    if (response.success) {
                        _cancelOrderState.value = CancelOrderState.Success(response.message)
                    } else {
                        _cancelOrderState.value = CancelOrderState.Error(response.message)
                    }
                },
                onFailure = { error ->
                    _cancelOrderState.value = CancelOrderState.Error(error.message ?: context.getString(R.string.error_unknown))
                }
            )
        }
    }

    fun updateOrderItemStatus(orderId: String, productId: String, status: String) {
        viewModelScope.launch {
            _updateStatusState.value = UpdateStatusState.Loading
            updateOrderStatusUseCase(orderId, productId, status).fold(
                onSuccess = { response ->
                    if (response.success) {
                        _updateStatusState.value = UpdateStatusState.Success(response.message)
                    } else {
                        _updateStatusState.value = UpdateStatusState.Error(response.message)
                    }
                },
                onFailure = { error ->
                    _updateStatusState.value = UpdateStatusState.Error(error.message ?: context.getString(R.string.error_unknown))
                }
            )
        }
    }

    fun resetCancelState() {
        _cancelOrderState.value = CancelOrderState.Idle
    }

    fun resetUpdateStatusState() {
        _updateStatusState.value = UpdateStatusState.Idle
    }
}
