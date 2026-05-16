package com.yash091099.ChiragFarmersApp.ui.presentation.sell.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderTrackingData
import com.yash091099.ChiragFarmersApp.domain.usecase.GetActiveOrdersUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.GetOrderTrackingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yash091099.ChiragFarmersApp.domain.model.Order
import com.yash091099.ChiragFarmersApp.domain.usecase.UpdateOrderStatusUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class OrderTrackingState {
    data object Idle : OrderTrackingState()
    data object Loading : OrderTrackingState()
    data class Success(val data: OrderTrackingData) : OrderTrackingState()
    data class Error(val message: String) : OrderTrackingState()
}

@HiltViewModel
class ActiveOrdersViewModel @Inject constructor(
    getActiveOrdersUseCase: GetActiveOrdersUseCase,
    private val getOrderTrackingUseCase: GetOrderTrackingUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase
) : ViewModel() {

    private val activeOrdersFlow: Flow<PagingData<Order>> = getActiveOrdersUseCase().cachedIn(viewModelScope)

    private val _orderTrackingState = MutableStateFlow<OrderTrackingState>(OrderTrackingState.Idle)
    val orderTrackingState: StateFlow<OrderTrackingState> = _orderTrackingState.asStateFlow()

    val activeOrders: Flow<PagingData<Order>>
        get() = activeOrdersFlow

    fun selectOrder(orderId: String?) {
        if (orderId != null) {
            fetchOrderTracking(orderId)
        } else {
            _orderTrackingState.value = OrderTrackingState.Idle
        }
    }

    private fun fetchOrderTracking(id: String) {
        viewModelScope.launch {
            _orderTrackingState.value = OrderTrackingState.Loading
            getOrderTrackingUseCase(id).fold(
                onSuccess = { response ->
                    _orderTrackingState.value = OrderTrackingState.Success(response.data)
                },
                onFailure = { exception ->
                    _orderTrackingState.value = OrderTrackingState.Error(
                        exception.message ?: "Failed to load tracking details"
                    )
                }
            )
        }
    }

    fun updateOrderStatus(id: String, productId: String, status: String) {
        viewModelScope.launch {
            updateOrderStatusUseCase(id, productId, status).onSuccess { response ->
                if (response.success) {
                    fetchOrderTracking(id)
                }
            }.onFailure {
                // Error handling could be added here
            }
        }
    }
}

