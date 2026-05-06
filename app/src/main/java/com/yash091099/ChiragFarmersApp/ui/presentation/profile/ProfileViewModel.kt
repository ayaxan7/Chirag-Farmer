package com.yash091099.ChiragFarmersApp.ui.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.model.auth.FarmerProfileData
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            authRepository.getFarmerProfile().fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        _uiState.value = ProfileUiState.Success(response.data)
                    } else {
                        _uiState.value = ProfileUiState.Error(
                            response.message ?: "Failed to load profile"
                        )
                    }
                },
                onFailure = { exception ->
                    if (isUnauthorized(exception)) {
                        authRepository.logout()
                        _uiState.value = ProfileUiState.Unauthorized
                    } else {
                        _uiState.value = ProfileUiState.Error(
                            when (exception) {
                                is HttpException -> {
                                    if (exception.code() == 404) {
                                        "User not found"
                                    } else {
                                        exception.message ?: "Failed to load profile"
                                    }
                                }
                                else -> exception.message ?: "Failed to load profile"
                            }
                        )
                    }
                }
            )
        }
    }

    suspend fun logout() {
        authRepository.logout()
    }

    private fun isUnauthorized(exception: Throwable): Boolean {
        return when (exception) {
            is HttpException -> exception.code() == 401 || exception.code() == 403
            else -> exception.message?.contains("No authentication token found", ignoreCase = true) == true ||
                exception.message?.contains("unauthorized", ignoreCase = true) == true
        }
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val profile: FarmerProfileData) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
    object Unauthorized : ProfileUiState()
}

