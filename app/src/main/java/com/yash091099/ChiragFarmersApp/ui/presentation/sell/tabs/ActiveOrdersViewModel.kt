package com.yash091099.ChiragFarmersApp.ui.presentation.sell.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.domain.model.Order
import com.yash091099.ChiragFarmersApp.domain.usecase.GetActiveOrdersUseCase
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

@HiltViewModel
class ActiveOrdersViewModel @Inject constructor(
    private val getActiveOrdersUseCase: GetActiveOrdersUseCase
) : ViewModel() {

    private val _ordersState = MutableStateFlow<ActiveOrdersState>(ActiveOrdersState.Loading)
    val ordersState: StateFlow<ActiveOrdersState> = _ordersState.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _pageSize = MutableStateFlow(10)

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

