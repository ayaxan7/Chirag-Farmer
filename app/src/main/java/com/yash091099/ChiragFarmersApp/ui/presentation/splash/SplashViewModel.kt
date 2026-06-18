package com.yash091099.ChiragFarmersApp.ui.presentation.splash

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import timber.log.Timber
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.location.LocationManager
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthCheckStatus {
    object Loading : AuthCheckStatus()
    object CheckingAuth : AuthCheckStatus()
    object RequestNotificationPermission : AuthCheckStatus()
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
    private val locationManager: LocationManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _authCheckStatus = MutableStateFlow<AuthCheckStatus>(AuthCheckStatus.Loading)
    val authCheckStatus: StateFlow<AuthCheckStatus> = _authCheckStatus.asStateFlow()

    private var permissionDenialCount = 0
    private val MAX_PERMISSION_DENIALS = 1 // After 1 denial, show mandatory dialog
    private var destinationAfterChecks: AuthCheckStatus = AuthCheckStatus.NavigateToAuth

    init {
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            try {
                // First, check if auth token exists
                val token = authRepository.getAuthToken().first()
                destinationAfterChecks = if (!token.isNullOrEmpty()) {
                    AuthCheckStatus.NavigateToHome
                } else {
                    AuthCheckStatus.NavigateToAuth
                }
                checkNotificationPermission()
            } catch (e: Exception) {
                Timber.e("Error checking authentication status: ${e.message}")
                _authCheckStatus.value = AuthCheckStatus.NavigateToAuth
            }
        }
    }

    private fun checkNotificationPermission() {
        val notificationPermissionGranted =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

        if (notificationPermissionGranted) {
            checkLocationPermission()
        } else {
            _authCheckStatus.value = AuthCheckStatus.RequestNotificationPermission
        }
    }

    /** Continue startup whether the user grants or declines notifications. */
    fun onNotificationPermissionResult(isGranted: Boolean) {
        Timber.d("Notification permission result: $isGranted")
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        viewModelScope.launch {
            try {
                val permissionGranted = locationManager.isLocationPermissionGranted()

                if (permissionGranted) {
                    Timber.d("Location permission granted, checking location service")
                    checkLocationService()
                } else {
                    Timber.d("Location permission not granted, showing dialog")
                    if (permissionDenialCount >= MAX_PERMISSION_DENIALS) {
                        _authCheckStatus.value = AuthCheckStatus.ShowLocationPermissionMandatoryDialog
                    } else {
                        _authCheckStatus.value = AuthCheckStatus.ShowLocationPermissionDialog
                    }
                }
            } catch (e: Exception) {
                Timber.e("Error checking location permission: ${e.message}")
                _authCheckStatus.value = AuthCheckStatus.NavigateToAuth
            }
        }
    }

    private fun checkLocationService() {
        viewModelScope.launch {
            try {
                val locationServiceEnabled = locationManager.isLocationServiceEnabled()

                if (locationServiceEnabled) {
                    Timber.d("Location service enabled, continuing startup")
                    _authCheckStatus.value = destinationAfterChecks
                } else {
                    Timber.d("Location service disabled, showing dialog")
                    _authCheckStatus.value = AuthCheckStatus.ShowLocationServiceDisabledDialog
                }
            } catch (e: Exception) {
                Timber.e("Error checking location service: ${e.message}")
                _authCheckStatus.value = AuthCheckStatus.NavigateToAuth
            }
        }
    }

    /**
     * Called when user responds to location permission dialog
     */
    fun onLocationPermissionGrantedByUser() {
        Timber.d("User interaction: Location permission granted by user")
        checkLocationService()
    }

    /**
     * Called when user denies location permission
     */
    fun onLocationPermissionDeniedByUser() {
        Timber.d("User interaction: Location permission denied by user")
        permissionDenialCount++
        checkLocationPermission()
    }

    /**
     * Called when user goes to location settings
     */
    fun onUserReturnedFromSettings() {
        Timber.d("User interaction: Returned from settings, rechecking location")
        checkLocationPermission()
    }

    /**
     * Called when user cancels the location service dialog
     */
    fun onLocationServiceDialogCancelled() {
        Timber.d("User interaction: Location service dialog cancelled")
        _authCheckStatus.value = AuthCheckStatus.NavigateToAuth
    }

    /**
     * Called when user cancels the mandatory permission dialog
     */
    fun onMandatoryPermissionDialogCancelled() {
        Timber.d("User interaction: Mandatory permission dialog cancelled")
        _authCheckStatus.value = AuthCheckStatus.NavigateToAuth
    }
}

