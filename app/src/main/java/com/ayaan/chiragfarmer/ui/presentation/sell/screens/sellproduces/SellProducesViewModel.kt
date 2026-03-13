package com.ayaan.chiragfarmer.ui.presentation.sell.screens.sellproduces

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.chiragfarmer.data.remote.dto.AddProductRequest
import com.ayaan.chiragfarmer.data.remote.dto.LocationRequestDto
import com.ayaan.chiragfarmer.data.remote.dto.ProductDetailsData
import com.ayaan.chiragfarmer.data.remote.dto.UpdateProductRequest
import com.ayaan.chiragfarmer.domain.model.Location
import com.ayaan.chiragfarmer.domain.usecase.AddProductUseCase
import com.ayaan.chiragfarmer.domain.usecase.GetLocationSuggestionsUseCase
import com.ayaan.chiragfarmer.domain.usecase.GetProductDetailsUseCase
import com.ayaan.chiragfarmer.domain.usecase.UpdateProductUseCase
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

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    private val _existingImageUrl = MutableStateFlow<String?>(null)
    val existingImageUrl: StateFlow<String?> = _existingImageUrl.asStateFlow()

    private val _productId = MutableStateFlow<String?>(null)

    private var searchJob: Job? = null

    fun fetchProductDetails(productId: String) {
        viewModelScope.launch {
            _fetchProductState.value = FetchProductState.Loading
            _productId.value = productId

            getProductDetailsUseCase(productId).onSuccess { product ->
                _fetchProductState.value = FetchProductState.Success(product)
                _existingImageUrl.value = product.image
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
        _imageUri.value = uri
        // Clear existing image URL when new image is selected
        if (uri != null) {
            _existingImageUrl.value = null
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

                // Validate required fields
                val selectedLoc = _selectedLocation.value

                if (isUpdate) {
                    // UPDATE MODE - Use update endpoint for partial updates
                    val productIdValue = _productId.value
                    if (productIdValue == null) {
                        _addProductState.value = AddProductState.Error("Product ID not found")
                        return@launch
                    }

                    // Determine image URL (only upload if new image selected)
                    val imageUrl = when {
                        _imageUri.value != null -> {
                            // Upload new image to Cloudinary
                            _addProductState.value = AddProductState.UploadingProduct
                            CloudinaryUploader.uploadImage(context, _imageUri.value!!)
                        }
                        else -> null // Keep existing image (don't send in request)
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

                    // Create update request with only changed fields
                    val updateRequest = UpdateProductRequest(
                        productId = productIdValue,
                        title = title.takeIf { it.isNotEmpty() },
                        image = imageUrl, // null if no new image
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
                    // ADD MODE - Use add endpoint (requires all fields)
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
                    _addProductState.value = AddProductState.UploadingProduct
                    val imageUrl = CloudinaryUploader.uploadImage(context, imageUriValue)

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

                    // Create request
                    val request = AddProductRequest(
                        title = title,
                        image = imageUrl,
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
        _imageUri.value = null
        _existingImageUrl.value = null
        _locationQuery.value = ""
        _selectedLocation.value = null
        _productId.value = null
    }
}
