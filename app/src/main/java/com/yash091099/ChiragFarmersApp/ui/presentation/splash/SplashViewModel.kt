package com.yash091099.ChiragFarmersApp.ui.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.location.LocationManager
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthCheckStatus {
    object Loading : AuthCheckStatus()
    object CheckingAuth : AuthCheckStatus()
    object CheckingLocationPermission : AuthCheckStatus()
    object ShowLocationPermissionDialog : AuthCheckStatus()
    object ShowLocationPermissionMandatoryDialog : AuthCheckStatus()
    object CheckingLocationService : AuthCheckStatus()
    object ShowLocationServiceDisabledDialog : AuthCheckStatus()
    object NavigateToHome : AuthCheckStatus()
    object NavigateToAuth : AuthCheckStatus()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _authCheckStatus = MutableStateFlow<AuthCheckStatus>(AuthCheckStatus.Loading)
    val authCheckStatus: StateFlow<AuthCheckStatus> = _authCheckStatus.asStateFlow()

    private var permissionDenialCount = 0
    private val MAX_PERMISSION_DENIALS = 1 // After 1 denial, show mandatory dialog

    init {
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            try {
                // First, check if auth token exists
                authRepository.getAuthToken().collect { token ->
                    if (token != null && token.isNotEmpty()) {
                        Log.d("SplashViewModel", "Token found, checking location permissions")
                        // Token exists, now check location permissions
                        _authCheckStatus.value = AuthCheckStatus.CheckingLocationPermission
                        checkLocationPermission()
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

    private fun checkLocationPermission() {
        viewModelScope.launch {
            try {
                val permissionGranted = locationManager.isLocationPermissionGranted()

                if (permissionGranted) {
                    Log.d("SplashViewModel", "Location permission granted, checking location service")
                    checkLocationService()
                } else {
                    Log.d("SplashViewModel", "Location permission not granted, showing dialog")
                    if (permissionDenialCount >= MAX_PERMISSION_DENIALS) {
                        _authCheckStatus.value = AuthCheckStatus.ShowLocationPermissionMandatoryDialog
                    } else {
                        _authCheckStatus.value = AuthCheckStatus.ShowLocationPermissionDialog
                    }
                }
            } catch (e: Exception) {
                Log.e("SplashViewModel", "Error checking location permission: ${e.message}")
                _authCheckStatus.value = AuthCheckStatus.NavigateToAuth
            }
        }
    }

    private fun checkLocationService() {
        viewModelScope.launch {
            try {
                val locationServiceEnabled = locationManager.isLocationServiceEnabled()

                if (locationServiceEnabled) {
                    Log.d("SplashViewModel", "Location service enabled, navigating to Home")
                    _authCheckStatus.value = AuthCheckStatus.NavigateToHome
                } else {
                    Log.d("SplashViewModel", "Location service disabled, showing dialog")
                    _authCheckStatus.value = AuthCheckStatus.ShowLocationServiceDisabledDialog
                }
            } catch (e: Exception) {
                Log.e("SplashViewModel", "Error checking location service: ${e.message}")
                _authCheckStatus.value = AuthCheckStatus.NavigateToAuth
            }
        }
    }

    /**
     * Called when user responds to location permission dialog
     */
    fun onLocationPermissionGrantedByUser() {
        Log.d("SplashViewModel", "User interaction: Location permission granted by user")
        checkLocationService()
    }

    /**
     * Called when user denies location permission
     */
    fun onLocationPermissionDeniedByUser() {
        Log.d("SplashViewModel", "User interaction: Location permission denied by user")
        permissionDenialCount++
        checkLocationPermission()
    }

    /**
     * Called when user goes to location settings
     */
    fun onUserReturnedFromSettings() {
        Log.d("SplashViewModel", "User interaction: Returned from settings, rechecking location")
        checkLocationPermission()
    }

    /**
     * Called when user cancels the location service dialog
     */
    fun onLocationServiceDialogCancelled() {
        Log.d("SplashViewModel", "User interaction: Location service dialog cancelled")
        _authCheckStatus.value = AuthCheckStatus.NavigateToAuth
    }

    /**
     * Called when user cancels the mandatory permission dialog
     */
    fun onMandatoryPermissionDialogCancelled() {
        Log.d("SplashViewModel", "User interaction: Mandatory permission dialog cancelled")
        _authCheckStatus.value = AuthCheckStatus.NavigateToAuth
    }
}


