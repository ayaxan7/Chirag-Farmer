package com.yash091099.ChiragFarmersApp.ui.presentation.sell.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.domain.model.Order
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderTrackingData
import com.yash091099.ChiragFarmersApp.domain.usecase.GetActiveOrdersUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.GetOrderTrackingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ActiveOrdersState {
    data object Loading : ActiveOrdersState()
    data class Success(val orders: List<Order>, val total: Int, val page: Int, val totalPages: Int) : ActiveOrdersState()
    data class Error(val message: String) : ActiveOrdersState()
}

sealed class OrderTrackingState {
    data object Idle : OrderTrackingState()
    data object Loading : OrderTrackingState()
    data class Success(val data: OrderTrackingData) : OrderTrackingState()
    data class Error(val message: String) : OrderTrackingState()
}

@HiltViewModel
class ActiveOrdersViewModel @Inject constructor(
    private val getActiveOrdersUseCase: GetActiveOrdersUseCase,
    private val getOrderTrackingUseCase: GetOrderTrackingUseCase
) : ViewModel() {

    private val _ordersState = MutableStateFlow<ActiveOrdersState>(ActiveOrdersState.Loading)
    val ordersState: StateFlow<ActiveOrdersState> = _ordersState.asStateFlow()

    private val _orderTrackingState = MutableStateFlow<OrderTrackingState>(OrderTrackingState.Idle)
    val orderTrackingState: StateFlow<OrderTrackingState> = _orderTrackingState.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _pageSize = MutableStateFlow(10)
    
    private val _selectedOrderId = MutableStateFlow<String?>(null)
    val selectedOrderId: StateFlow<String?> = _selectedOrderId.asStateFlow()

    fun selectOrder(orderId: String?) {
        _selectedOrderId.value = orderId
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

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _ordersState.value = ActiveOrdersState.Loading
            getActiveOrdersUseCase(_currentPage.value, _pageSize.value).fold(
                onSuccess = { ordersData ->
                    _ordersState.value = ActiveOrdersState.Success(
                        orders = ordersData.orders,
                        total = ordersData.total,
                        page = ordersData.page,
                        totalPages = ordersData.totalPages
                    )
                },
                onFailure = { exception ->
                    _ordersState.value = ActiveOrdersState.Error(
                        exception.message ?: "Failed to load orders"
                    )
                }
            )
        }
    }

    fun nextPage() {
        val currentState = _ordersState.value
        if (currentState is ActiveOrdersState.Success && _currentPage.value < currentState.totalPages) {
            _currentPage.value++
            loadOrders()
        }
    }

    fun previousPage() {
        if (_currentPage.value > 1) {
            _currentPage.value--
            loadOrders()
        }
    }

    fun retry() {
        loadOrders()
    }
}

