package com.yash091099.ChiragFarmersApp.ui.presentation.auth.common.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun sendOTP(phoneNumber: String, isSignUp: Boolean) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val result = if (isSignUp) {
                authRepository.sendRegistrationOTP(phoneNumber)
            } else {
                authRepository.sendLoginOTP(phoneNumber)
            }

            result.fold(
                onSuccess = { response ->
                    Timber.d("sendOTP response: success=%b, data=%s, message=%s", response.success, response.data, response.message)
                    if (response.success && response.data != null) {
                        _uiState.value = LoginUiState.Success(
                            requestId = response.data.requestId,
                            isSignUp = isSignUp,
                            message = response.message
                        )
                    } else {
                        _uiState.value = LoginUiState.Error(response.message)
                    }
                },
                onFailure = { exception ->
                    Timber.e(exception, "sendOTP failed")
                    _uiState.value = LoginUiState.Error(
                        exception.message ?: "Network error occurred"
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(
        val requestId: String,
        val isSignUp: Boolean,
        val message: String
    ) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

