package com.yash091099.ChiragFarmersApp.ui.presentation.cart.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.FarmerAddressDto
import com.yash091099.ChiragFarmersApp.domain.usecase.GetFarmerAddressesUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.SetDefaultAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.yash091099.ChiragFarmersApp.R
import javax.inject.Inject

@HiltViewModel
class AddressListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getFarmerAddressesUseCase: GetFarmerAddressesUseCase,
    private val setDefaultAddressUseCase: SetDefaultAddressUseCase
) : ViewModel() {

    private val _addressState = MutableStateFlow<AddressListUiState>(AddressListUiState.Loading)
    val addressState: StateFlow<AddressListUiState> = _addressState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AddressListNavigationEvent>()
    val navigationEvent: SharedFlow<AddressListNavigationEvent> = _navigationEvent.asSharedFlow()

    private val _isOperationInProgress = MutableStateFlow(false)
    val isOperationInProgress: StateFlow<Boolean> = _isOperationInProgress.asStateFlow()

    init {
        loadAddresses()
    }

    private fun loadAddresses() {
        viewModelScope.launch {
            _addressState.value = AddressListUiState.Loading

            getFarmerAddressesUseCase().fold(
                onSuccess = { addresses ->
                    _addressState.value = if (addresses.isEmpty()) {
                        AddressListUiState.Empty
                    } else {
                        AddressListUiState.Success(addresses)
                    }
                },
                onFailure = { exception ->
                    _addressState.value = AddressListUiState.Error(
                        exception.message ?: context.getString(R.string.error_failed_load_addresses)
                    )
                }
            )
        }
    }

    fun setDefaultAddress(addressId: String) {
        viewModelScope.launch {
            _isOperationInProgress.value = true
            setDefaultAddressUseCase(addressId).fold(
                onSuccess = {
                    _isOperationInProgress.value = false
                    _navigationEvent.emit(AddressListNavigationEvent.NavigateBackToCart)
                },
                onFailure = { exception ->
                    _isOperationInProgress.value = false
                    _addressState.value = AddressListUiState.Error(
                        exception.message ?: context.getString(R.string.error_failed_set_default_address)
                    )
                }
            )
        }
    }

    fun retry() {
        loadAddresses()
    }
}

sealed class AddressListUiState {
    object Loading : AddressListUiState()
    data class Success(val addresses: List<FarmerAddressDto>) : AddressListUiState()
    object Empty : AddressListUiState()
    data class Error(val message: String) : AddressListUiState()
}

sealed class AddressListNavigationEvent {
    object NavigateBackToCart : AddressListNavigationEvent()
}

