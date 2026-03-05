package com.ayaan.chiragfarmer.ui.presentation.sell.screens

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.chiragfarmer.data.remote.dto.AddProductRequest
import com.ayaan.chiragfarmer.data.remote.dto.LocationRequestDto
import com.ayaan.chiragfarmer.domain.model.Location
import com.ayaan.chiragfarmer.domain.usecase.AddProductUseCase
import com.ayaan.chiragfarmer.domain.usecase.GetLocationSuggestionsUseCase
import com.ayaan.chiragfarmer.utils.CloudinaryUploader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AddProductState {
    data object Idle : AddProductState()
    data object Loading : AddProductState()
    data object UploadingImage : AddProductState()
    data class Success(val message: String) : AddProductState()
    data class Error(val message: String) : AddProductState()
}
@HiltViewModel
class SellProducesViewModel @Inject constructor(
    private val getLocationSuggestionsUseCase: GetLocationSuggestionsUseCase,
    private val addProductUseCase: AddProductUseCase
) : ViewModel() {

    private val _locationQuery = MutableStateFlow("")
    val locationQuery: StateFlow<String> = _locationQuery.asStateFlow()

    private val _locationSuggestions = MutableStateFlow<List<Location>>(emptyList())
    val locationSuggestions: StateFlow<List<Location>> = _locationSuggestions.asStateFlow()

    private val _selectedLocation = MutableStateFlow<Location?>(null)
    val selectedLocation: StateFlow<Location?> = _selectedLocation.asStateFlow()

    private val _addProductState = MutableStateFlow<AddProductState>(AddProductState.Idle)
    val addProductState: StateFlow<AddProductState> = _addProductState.asStateFlow()

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    private var searchJob: Job? = null

    fun onLocationQueryChange(query: String) {
        _locationQuery.value = query
        
        searchJob?.cancel()
        if (query.isNotEmpty()) {
            searchJob = viewModelScope.launch {
                delay(300) // Debounce
                getLocationSuggestionsUseCase(query).onSuccess {
                    _locationSuggestions.value = it
                }.onFailure {
                    _locationSuggestions.value = emptyList()
                }
            }
        } else {
            _locationSuggestions.value = emptyList()
        }
    }

    fun onLocationSelected(location: Location) {
        _locationQuery.value = location.displayName
        _selectedLocation.value = location
        _locationSuggestions.value = emptyList()
    }

    fun onImageSelected(uri: Uri?) {
        _imageUri.value = uri
    }

    fun submitProduct(
        context: Context,
        category: String,
        title: String,
        availableStock: String,
        price: String,
        description: String
    ) {
        viewModelScope.launch {
            try {
                _addProductState.value = AddProductState.Loading

                // Validate required fields
                val selectedLoc = _selectedLocation.value
                val imageUriValue = _imageUri.value

                if (imageUriValue == null) {
                    _addProductState.value = AddProductState.Error("Please select a product image")
                    return@launch
                }

                if (selectedLoc == null) {
                    _addProductState.value = AddProductState.Error("Please select a location")
                    return@launch
                }

                // Upload image to Cloudinary
                _addProductState.value = AddProductState.UploadingImage
                val imageUrl = CloudinaryUploader.uploadImage(context, imageUriValue)

                // Parse numeric values
                val stockWeight = availableStock.toDoubleOrNull()
                val priceValue = price.toDoubleOrNull()

                if (priceValue == null) {
                    _addProductState.value = AddProductState.Error("Invalid price value")
                    return@launch
                }

                // Create request
                val request = AddProductRequest(
                    title = title,
                    image = imageUrl,
                    category = category.takeIf { it.isNotEmpty() },
                    description = description.takeIf { it.isNotEmpty() },
                    availableStockWeight = stockWeight,
                    price = priceValue,
                    location = LocationRequestDto(
                        name = selectedLoc.displayName,
                        coordinates = listOf(selectedLoc.longitude, selectedLoc.latitude)
                    )
                )

                // Submit product
                addProductUseCase(request).onSuccess {
                    _addProductState.value = AddProductState.Success("Product added successfully")
                    resetForm()
                }.onFailure { error ->
                    _addProductState.value = AddProductState.Error(
                        error.message ?: "Failed to add product"
                    )
                }

            } catch (e: Exception) {
                _addProductState.value = AddProductState.Error(
                    e.message ?: "An error occurred"
                )
            }
        }
    }

    fun resetState() {
        _addProductState.value = AddProductState.Idle
    }

    private fun resetForm() {
        _imageUri.value = null
        _locationQuery.value = ""
        _selectedLocation.value = null
    }
}
