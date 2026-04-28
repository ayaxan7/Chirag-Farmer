package com.yash091099.ChiragFarmersApp.ui.presentation.cart.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.FarmerAddressDto
import com.yash091099.ChiragFarmersApp.domain.usecase.GetFarmerAddressesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressListViewModel @Inject constructor(
    private val getFarmerAddressesUseCase: GetFarmerAddressesUseCase
) : ViewModel() {

    private val _addressState = MutableStateFlow<AddressListUiState>(AddressListUiState.Loading)
    val addressState: StateFlow<AddressListUiState> = _addressState.asStateFlow()

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
                        exception.message ?: "Failed to load addresses"
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

