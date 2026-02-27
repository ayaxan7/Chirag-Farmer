package com.ayaan.chiragfarmer.ui.presentation.auth.register

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.chiragfarmer.data.model.auth.AddBusinessInfoRequest
import com.ayaan.chiragfarmer.data.repository.AuthRepository
import com.ayaan.chiragfarmer.utils.CloudinaryUploader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun addBusinessInfo(
        context: Context,
        name: String,
        email: String,
        companyName: String? = null,
        gstNumber: String? = null,
        businessAddress: String? = null,
        pincode: String? = null,
        vendorName: String? = null,
        profileImageUri: Uri? = null,
        aadhaarFrontImageUri: Uri? = null,
        aadhaarBackImageUri: Uri? = null,
        droneImageUri: Uri? = null
    ) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading

            try {
                // Upload each non-null URI to Cloudinary and get back secure_url
                val profileImageUrl = profileImageUri?.let {
                    CloudinaryUploader.uploadImage(context, it)
                }
                val aadhaarFrontUrl = aadhaarFrontImageUri?.let {
                    CloudinaryUploader.uploadImage(context, it)
                }
                val aadhaarBackUrl = aadhaarBackImageUri?.let {
                    CloudinaryUploader.uploadImage(context, it)
                }
                val droneImageUrl = droneImageUri?.let {
                    CloudinaryUploader.uploadImage(context, it)
                }

                val request = AddBusinessInfoRequest(
                    name = name,
                    emailId = email,
                    companyName = companyName,
                    gstNumber = gstNumber,
                    businessAddress = businessAddress,
                    pinCode = pincode,
                    vendorName = vendorName,
                    profileImage = profileImageUrl,
                    aadhaarFrontImage = aadhaarFrontUrl,
                    aadhaarBackImage = aadhaarBackUrl,
                    droneImage = droneImageUrl
                )

                val result = authRepository.addBusinessInfo(request)
                result.fold(
                    onSuccess = { response ->
                        if (response.success) {
                            _uiState.value = RegisterUiState.Success(response.message)
                        } else {
                            _uiState.value = RegisterUiState.Error(response.message)
                        }
                    },
                    onFailure = { exception ->
                        _uiState.value = RegisterUiState.Error(
                            exception.message ?: "An error occurred while updating business information"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error(
                    e.message ?: "Image upload failed. Please try again."
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Idle
    }
}

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val message: String) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}
