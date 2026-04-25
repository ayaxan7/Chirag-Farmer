package com.yash091099.ChiragFarmersApp.ui.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthCheckStatus {
    object Loading : AuthCheckStatus()
    object NavigateToHome : AuthCheckStatus()
    object NavigateToAuth : AuthCheckStatus()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authCheckStatus = MutableStateFlow<AuthCheckStatus>(AuthCheckStatus.Loading)
    val authCheckStatus: StateFlow<AuthCheckStatus> = _authCheckStatus.asStateFlow()

    init {
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            try {
                // Check if auth token exists in DataStore
                authRepository.getAuthToken().collect { token ->
                    if (token != null && token.isNotEmpty()) {
                        Log.d("SplashViewModel", "Token found, navigating to Home")
                        _authCheckStatus.value = AuthCheckStatus.NavigateToHome
                    } else {
                        Log.d("SplashViewModel", "No token found, navigating to Auth")
                        _authCheckStatus.value = AuthCheckStatus.NavigateToAuth
                    }
                }
            } catch (e: Exception) {
                Log.e("SplashViewModel", "Error checking authentication status: ${e.message}")
                _authCheckStatus.value = AuthCheckStatus.NavigateToAuth
            }
        }
    }
}

