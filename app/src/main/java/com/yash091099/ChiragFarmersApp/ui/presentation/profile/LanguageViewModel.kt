package com.yash091099.ChiragFarmersApp.ui.presentation.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateLanguageRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateLanguageResponse
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.utils.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _updateLanguageState = MutableStateFlow<UpdateLanguageState>(UpdateLanguageState.Idle)
    val updateLanguageState: StateFlow<UpdateLanguageState> = _updateLanguageState.asStateFlow()

    fun updateLanguage(languageCode: String) {
        viewModelScope.launch {
            _updateLanguageState.value = UpdateLanguageState.Loading
            val request = UpdateLanguageRequest(language = languageCode)
            val result = authRepository.updatePreferredLanguage(request)
            result.fold(
                onSuccess = { response ->
                    if (response.success) {
                        _updateLanguageState.value = UpdateLanguageState.Success(response.message)
                    } else {
                        _updateLanguageState.value = UpdateLanguageState.Error(response.message)
                    }
                },
                onFailure = { e ->
                    _updateLanguageState.value = UpdateLanguageState.Error(e.message ?: context.getString(R.string.error_failed_update_language))
                }
            )
        }
    }

    fun resetState() {
        _updateLanguageState.value = UpdateLanguageState.Idle
    }
}

sealed class UpdateLanguageState {
    object Idle : UpdateLanguageState()
    object Loading : UpdateLanguageState()
    data class Success(val message: String) : UpdateLanguageState()
    data class Error(val message: String) : UpdateLanguageState()
}
