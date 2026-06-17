package com.yash091099.ChiragFarmersApp.ui.presentation.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.model.auth.FarmerProfileData
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.utils.CloudinaryUploader
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _updateProfileUiState = MutableStateFlow<UpdateProfileUiState>(UpdateProfileUiState.Idle)
    val updateProfileUiState: StateFlow<UpdateProfileUiState> = _updateProfileUiState.asStateFlow()

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
                            response.message ?: context.getString(R.string.error_failed_load_profile)
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
                                        context.getString(R.string.error_user_not_found)
                                    } else {
                                        exception.message ?: context.getString(R.string.error_failed_load_profile)
                                    }
                                }
                                else -> exception.message ?: context.getString(R.string.error_failed_load_profile)
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

    fun updateProfile(
        context: Context,
        originalProfile: FarmerProfileData,
        editedName: String,
        editedEmail: String,
        selectedImageUri: Uri?
    ) {
        viewModelScope.launch {
            _updateProfileUiState.value = UpdateProfileUiState.Loading

            try {
                val changedFields = mutableMapOf<String, String>()

                val normalizedName = editedName.trim()
                val normalizedEmail = editedEmail.trim()
                val originalName = originalProfile.username.orEmpty().trim()
                val originalEmail = originalProfile.email.orEmpty().trim()

                if (normalizedName != originalName) {
                    changedFields["name"] = normalizedName
                }

                if (normalizedEmail != originalEmail) {
                    changedFields["email"] = normalizedEmail
                }

                var uploadedImageUrl: String? = null
                if (selectedImageUri != null) {
                    uploadedImageUrl = CloudinaryUploader.uploadImage(context, selectedImageUri)
                    changedFields["profileImage"] = uploadedImageUrl
                }

                if (changedFields.isEmpty()) {
                    _updateProfileUiState.value = UpdateProfileUiState.Error(context.getString(R.string.error_no_changes_detected))
                    return@launch
                }

                authRepository.updateProfile(changedFields).fold(
                    onSuccess = { response ->
                        if (response.success) {
                            _uiState.value = ProfileUiState.Success(
                                originalProfile.copy(
                                    username = changedFields["name"] ?: originalProfile.username,
                                    email = changedFields["email"] ?: originalProfile.email,
                                    profileImage = uploadedImageUrl ?: originalProfile.profileImage
                                )
                            )
                            _updateProfileUiState.value = UpdateProfileUiState.Success(response.message)
                        } else {
                            _updateProfileUiState.value = UpdateProfileUiState.Error(response.message)
                        }
                    },
                    onFailure = { exception ->
                        if (isUnauthorized(exception)) {
                            authRepository.logout()
                            _uiState.value = ProfileUiState.Unauthorized
                        }
                        _updateProfileUiState.value = UpdateProfileUiState.Error(
                            exception.message ?: context.getString(R.string.error_failed_update_profile)
                        )
                    }
                )
            } catch (e: Exception) {
                _updateProfileUiState.value = UpdateProfileUiState.Error(
                    e.message ?: context.getString(R.string.error_failed_update_profile)
                )
            }
        }
    }

    fun resetUpdateProfileState() {
        _updateProfileUiState.value = UpdateProfileUiState.Idle
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

sealed class UpdateProfileUiState {
    object Idle : UpdateProfileUiState()
    object Loading : UpdateProfileUiState()
    data class Success(val message: String) : UpdateProfileUiState()
    data class Error(val message: String) : UpdateProfileUiState()
}

