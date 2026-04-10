package com.yash091099.ChiragFarmersApp.ui.presentation.auth.common.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<OTPUiState>(OTPUiState.Idle)
    val uiState: StateFlow<OTPUiState> = _uiState.asStateFlow()

    fun verifyOTP(phoneNumber: String, otp: String, requestId: String, isSignUp: Boolean) {
        viewModelScope.launch {
            _uiState.value = OTPUiState.Loading

            val result = if (isSignUp) {
                authRepository.register(phoneNumber, otp, requestId)
            } else {
                authRepository.verifyLoginOTP(phoneNumber, otp, requestId)
            }

            result.fold(
                onSuccess = { response ->
                    if (response.success && response.data?.verified == true) {
                        _uiState.value = OTPUiState.Success(
                            message = response.message,
                            token = response.data.token ?: ""
                        )
                    } else {
                        _uiState.value = OTPUiState.Error(response.message)
                    }
                },
                onFailure = { exception ->
                    _uiState.value = OTPUiState.Error(
                        exception.message ?: "Network error occurred"
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = OTPUiState.Idle
    }
}

sealed class OTPUiState {
    object Idle : OTPUiState()
    object Loading : OTPUiState()
    data class Success(val message: String, val token: String) : OTPUiState()
    data class Error(val message: String) : OTPUiState()
}

