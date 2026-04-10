package com.yash091099.ChiragFarmersApp.ui.presentation.sell.screens.sellproduces

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.LocationRequestDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductDetailsData
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateProductRequest
import com.yash091099.ChiragFarmersApp.domain.model.Location
import com.yash091099.ChiragFarmersApp.domain.usecase.AddProductUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.GetLocationSuggestionsUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.GetProductDetailsUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.UpdateProductUseCase
import com.yash091099.ChiragFarmersApp.utils.CloudinaryUploader
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
    data object UploadingProduct : AddProductState()
    data class Success(val message: String) : AddProductState()
    data class Error(val message: String) : AddProductState()
}

sealed class FetchProductState {
    data object Idle : FetchProductState()
    data object Loading : FetchProductState()
    data class Success(val product: ProductDetailsData) : FetchProductState()
    data class Error(val message: String) : FetchProductState()
}

@HiltViewModel
class SellProducesViewModel @Inject constructor(
    private val getLocationSuggestionsUseCase: GetLocationSuggestionsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) : ViewModel() {

    private val _locationQuery = MutableStateFlow("")
    val locationQuery: StateFlow<String> = _locationQuery.asStateFlow()

    private val _locationSuggestions = MutableStateFlow<List<Location>>(emptyList())
    val locationSuggestions: StateFlow<List<Location>> = _locationSuggestions.asStateFlow()

    private val _selectedLocation = MutableStateFlow<Location?>(null)
    val selectedLocation: StateFlow<Location?> = _selectedLocation.asStateFlow()

    private val _addProductState = MutableStateFlow<AddProductState>(AddProductState.Idle)
    val addProductState: StateFlow<AddProductState> = _addProductState.asStateFlow()

    private val _fetchProductState = MutableStateFlow<FetchProductState>(FetchProductState.Idle)
    val fetchProductState: StateFlow<FetchProductState> = _fetchProductState.asStateFlow()

    private val _selectedImageUris = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImageUris: StateFlow<List<Uri>> = _selectedImageUris.asStateFlow()

    private val _existingImageUrls = MutableStateFlow<List<String>>(emptyList())
    val existingImageUrls: StateFlow<List<String>> = _existingImageUrls.asStateFlow()

    private val _productId = MutableStateFlow<String?>(null)

    private var searchJob: Job? = null

    companion object {
        private const val MAX_IMAGES = 3
    }

    fun fetchProductDetails(productId: String) {
        viewModelScope.launch {
            _fetchProductState.value = FetchProductState.Loading
            _productId.value = productId

            getProductDetailsUseCase(productId).onSuccess { product ->
                _fetchProductState.value = FetchProductState.Success(product)
                // Load existing images (up to 3)
                _existingImageUrls.value = product.images.take(MAX_IMAGES)
                _locationQuery.value = product.location.name
                _selectedLocation.value = Location(
                    displayName = product.location.name,
                    latitude = product.location.coordinates[1],
                    longitude = product.location.coordinates[0]
                )
            }.onFailure { error ->
                _fetchProductState.value = FetchProductState.Error(
                    error.message ?: "Failed to fetch product details"
                )
            }
        }
    }

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
        uri?.let {
            val currentImages = _selectedImageUris.value.toMutableList()
            if (currentImages.size < MAX_IMAGES) {
                currentImages.add(uri)
                _selectedImageUris.value = currentImages
            }
        }
    }

    fun removeSelectedImage(index: Int) {
        val currentImages = _selectedImageUris.value.toMutableList()
        if (index in currentImages.indices) {
            currentImages.removeAt(index)
            _selectedImageUris.value = currentImages
        }
    }

    fun removeExistingImage(index: Int) {
        val currentImages = _existingImageUrls.value.toMutableList()
        if (index in currentImages.indices) {
            currentImages.removeAt(index)
            _existingImageUrls.value = currentImages
        }
    }

    fun submitProduct(
        context: Context,
        category: String,
        title: String,
        availableStock: String,
        price: String,
        discount: String,
        deliveryFee: String,
        description: String,
        isUpdate: Boolean = false
    ) {
        viewModelScope.launch {
            try {
                _addProductState.value = AddProductState.Loading
                val selectedLoc = _selectedLocation.value

                if (isUpdate) {
                    // UPDATE MODE
                    val productIdValue = _productId.value
                    if (productIdValue == null) {
                        _addProductState.value = AddProductState.Error("Product ID not found")
                        return@launch
                    }

                    // Collect all images: existing + newly selected
                    val allImageUrls = mutableListOf<String>()
                    
                    // Add existing images that weren't removed
                    allImageUrls.addAll(_existingImageUrls.value)

                    // Upload new selected images one by one
                    if (_selectedImageUris.value.isNotEmpty()) {
                        _addProductState.value = AddProductState.UploadingProduct
                        for (uri in _selectedImageUris.value) {
                            try {
                                val uploadedUrl = CloudinaryUploader.uploadImage(context, uri)
                                allImageUrls.add(uploadedUrl)
                            } catch (e: Exception) {
                                _addProductState.value = AddProductState.Error("Failed to upload image: ${e.message}")
                                return@launch
                            }
                        }
                    }

                    // Parse numeric values
                    val stockWeight = availableStock.toDoubleOrNull()
                    val priceValue = price.toDoubleOrNull()
                    val discountValue = discount.takeIf { it.isNotBlank() }?.toDoubleOrNull()
                    val deliveryFeeValue = deliveryFee.takeIf { it.isNotBlank() }?.toDoubleOrNull()

                    if (priceValue == null) {
                        _addProductState.value = AddProductState.Error("Invalid price value")
                        return@launch
                    }

                    if (discount.isNotBlank() && discountValue == null) {
                        _addProductState.value = AddProductState.Error("Invalid discount value")
                        return@launch
                    }

                    if (deliveryFee.isNotBlank() && deliveryFeeValue == null) {
                        _addProductState.value = AddProductState.Error("Invalid delivery fee value")
                        return@launch
                    }

                    // Create update request with all images
                    val updateRequest = UpdateProductRequest(
                        productId = productIdValue,
                        title = title.takeIf { it.isNotEmpty() },
                        images = if (allImageUrls.isNotEmpty()) allImageUrls else null,
                        category = category.takeIf { it.isNotEmpty() },
                        description = description.takeIf { it.isNotEmpty() },
                        availableStockWeight = stockWeight,
                        price = priceValue,
                        discount = discountValue,
                        deliveryFee = deliveryFeeValue,
                        location = selectedLoc?.let {
                            LocationRequestDto(
                                name = it.displayName,
                                coordinates = listOf(it.longitude, it.latitude)
                            )
                        }
                    )

                    // Submit update
                    updateProductUseCase(updateRequest).onSuccess {
                        _addProductState.value = AddProductState.Success("Product updated successfully")
                        resetForm()
                    }.onFailure { error ->
                        _addProductState.value = AddProductState.Error(
                            error.message ?: "Failed to update product"
                        )
                    }

                } else {
                    // ADD MODE
                    if (_selectedImageUris.value.isEmpty()) {
                        _addProductState.value = AddProductState.Error("Please select at least one product image")
                        return@launch
                    }

                    if (selectedLoc == null) {
                        _addProductState.value = AddProductState.Error("Please select a location")
                        return@launch
                    }

                    // Upload all selected images one by one
                    _addProductState.value = AddProductState.UploadingProduct
                    val uploadedImageUrls = mutableListOf<String>()
                    
                    for (uri in _selectedImageUris.value) {
                        try {
                            val uploadedUrl = CloudinaryUploader.uploadImage(context, uri)
                            uploadedImageUrls.add(uploadedUrl)
                        } catch (e: Exception) {
                            _addProductState.value = AddProductState.Error("Failed to upload image: ${e.message}")
                            return@launch
                        }
                    }

                    // Parse numeric values
                    val stockWeight = availableStock.toDoubleOrNull()
                    val priceValue = price.toDoubleOrNull()
                    val discountValue = discount.takeIf { it.isNotBlank() }?.toDoubleOrNull()
                    val deliveryFeeValue = deliveryFee.takeIf { it.isNotBlank() }?.toDoubleOrNull()

                    if (priceValue == null) {
                        _addProductState.value = AddProductState.Error("Invalid price value")
                        return@launch
                    }

                    if (discount.isNotBlank() && discountValue == null) {
                        _addProductState.value = AddProductState.Error("Invalid discount value")
                        return@launch
                    }

                    if (deliveryFee.isNotBlank() && deliveryFeeValue == null) {
                        _addProductState.value = AddProductState.Error("Invalid delivery fee value")
                        return@launch
                    }

                    // Create request with all uploaded images
                    val request = AddProductRequest(
                        title = title,
                        images = uploadedImageUrls, // All uploaded images
                        category = category.takeIf { it.isNotEmpty() },
                        description = description.takeIf { it.isNotEmpty() },
                        availableStockWeight = stockWeight,
                        price = priceValue,
                        discount = discountValue,
                        deliveryFee = deliveryFeeValue,
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

    fun resetFetchState() {
        _fetchProductState.value = FetchProductState.Idle
    }

    private fun resetForm() {
        _selectedImageUris.value = emptyList()
        _existingImageUrls.value = emptyList()
        _locationQuery.value = ""
        _selectedLocation.value = null
        _productId.value = null
    }
}
