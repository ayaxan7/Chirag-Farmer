package com.ayaan.chiragfarmer.ui.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.chiragfarmer.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isProfileComplete = MutableStateFlow(false)
    val isProfileComplete: StateFlow<Boolean> = _isProfileComplete.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Load profile status from DataStore
        viewModelScope.launch {
            authRepository.getProfileStatus().collect { status ->
                _isProfileComplete.value = status
            }
        }
        // Fetch fresh profile status from API
        fetchProfileStatus()
    }

    fun fetchProfileStatus() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.checkProfileStatus()
            result.fold(
                onSuccess = { response ->
                    if (response.success && response.data != null) {
                        _isProfileComplete.value = response.data
                    }
                },
                onFailure = {
                    Log.e("HomeViewModel", "Error fetching profile status: ${it.message}")
                }
            )
            _isLoading.value = false
        }
    }

    suspend fun logout() {
        authRepository.logout()
    }
}

