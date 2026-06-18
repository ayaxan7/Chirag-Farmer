package com.yash091099.ChiragFarmersApp.ui.presentation.sell.screens.sellproduces

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragBasicTextField
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.MultiLineTextField
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.bookservicecard.components.LocationInputField
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import java.util.Locale

private fun sanitizeCategoryForSingleLine(value: String?): String {
    return value?.replace("\\n", " ")?.replace("\n", " ")?.trim()?.replace(Regex("\\s+"), " ")
        .orEmpty()
}

@Composable
fun SellProducesScreen(
    navController: NavHostController,
    productId: String? = null,
    selectedCategory: String? = null,
    viewModel: SellProducesViewModel = hiltViewModel()
) {
    var productCategory by remember { mutableStateOf("") }
    var isCategoryEditable by remember { mutableStateOf(true) }
    var productTitle by remember { mutableStateOf("") }
    var availableStock by remember { mutableStateOf("") }
    val location by viewModel.locationQuery.collectAsStateWithLifecycle()
    val locationSuggestions by viewModel.locationSuggestions.collectAsStateWithLifecycle()
    var pricing by remember { mutableStateOf("") }
    var discountPercent by remember { mutableStateOf("") }
    var deliveryFeePerKm by remember { mutableStateOf("") }
    var isDeliveryFeeIncludedInTotalPrice by remember { mutableStateOf(false) }
    var productDescription by remember { mutableStateOf("") }
    var productFeatures by remember { mutableStateOf("") }

    val selectedImageUris by viewModel.selectedImageUris.collectAsStateWithLifecycle()
    val existingImageUrls by viewModel.existingImageUrls.collectAsStateWithLifecycle()
    val addProductState by viewModel.addProductState.collectAsStateWithLifecycle()
    val fetchProductState by viewModel.fetchProductState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val isEditMode = productId != null

    fun isOtherCategory(value: String?): Boolean {
        return value?.trim()?.equals("other", ignoreCase = true) == true
    }
    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        viewModel.onImageSelected(uri)
    }

    // Fetch product details if editing
    LaunchedEffect(productId) {
        if (productId != null) {
            viewModel.fetchProductDetails(productId)
        }
    }

    // Initialize category when screen is opened from category selection flow.
    LaunchedEffect(productId, selectedCategory) {
        if (productId != null) return@LaunchedEffect

        isDeliveryFeeIncludedInTotalPrice = false
        val decodedSelectedCategory = sanitizeCategoryForSingleLine(
            selectedCategory?.let(Uri::decode)
        )
        if (decodedSelectedCategory.isEmpty()) {
            productCategory = ""
            isCategoryEditable = true
        } else if (isOtherCategory(decodedSelectedCategory)) {
            productCategory = ""
            isCategoryEditable = true
        } else {
            productCategory = decodedSelectedCategory
            isCategoryEditable = false
        }
    }

    // Handle fetch product state
    LaunchedEffect(fetchProductState) {
        when (val state = fetchProductState) {
            is FetchProductState.Success -> {
                val product = state.product
                if (isOtherCategory(product.category)) {
                    productCategory = ""
                    isCategoryEditable = true
                } else {
                    productCategory = sanitizeCategoryForSingleLine(product.category)
                    isCategoryEditable = false
                }
                productTitle = product.title
                availableStock = product.availableStockWeight?.toString() ?: ""
                pricing = product.price.toString()
                discountPercent = product.discount?.toString() ?: ""
                deliveryFeePerKm = product.deliveryFee?.toString() ?: ""
                isDeliveryFeeIncludedInTotalPrice = product.deliveryFee == null
                productDescription = product.description ?: ""
                val featuresList = state.product.keyFeatures ?: emptyList()
                productFeatures = featuresList.joinToString(", ")
                viewModel.resetFetchState()
            }

            is FetchProductState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetFetchState()
            }

            else -> Unit
        }
    }

    // Handle product submission states
    LaunchedEffect(addProductState) {
        when (val state = addProductState) {
            is AddProductState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
                if (!navController.popBackStack(Route.Sell.path, false)) {
                    navController.navigate(Route.Sell.path) {
                        launchSingleTop = true
                    }
                }
            }

            is AddProductState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }

            else -> Unit
        }
    }

    Scaffold(
        topBar = {
        ChiragTopBar(
            navController = navController,
            icon = R.drawable.ic_arrow,
            title = if (isEditMode) stringResource(R.string.sell_produce_edit) else stringResource(R.string.sell_categories_title)
        )
    },
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Section Title
                Text(
                    text = stringResource(R.string.sell_produce_details),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Product Category
                FieldLabel(text = stringResource(R.string.sell_produce_category_label))
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = productCategory,
                    onValueChange = { productCategory = it },
                    placeholder = stringResource(R.string.sell_produce_category_placeholder),
                    readOnly = !isCategoryEditable
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Product Title
                FieldLabel(text = stringResource(R.string.sell_produce_title_label))
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = productTitle,
                    onValueChange = { productTitle = it },
                    placeholder = stringResource(R.string.sell_produce_title_placeholder)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Images Gallery
                FieldLabel(text = stringResource(R.string.sell_images_label))
                Spacer(modifier = Modifier.height(10.dp))

                // Display images horizontally with remove buttons
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(end = 8.dp)
                    ) {
                        // Existing images
                        items(existingImageUrls.size) { index ->
                            ImageThumbnailWithRemove(
                                imageUrl = existingImageUrls[index],
                                onRemove = { viewModel.removeExistingImage(index) }
                            )
                        }

                        // Selected new images
                        items(selectedImageUris.size) { index ->
                            ImageThumbnailWithRemove(
                                imageUri = selectedImageUris[index],
                                onRemove = { viewModel.removeSelectedImage(index) }
                            )
                        }

                        // Add image button (enabled only if less than 3 images)
                        if ((existingImageUrls.size + selectedImageUris.size) < 3) {
                            item {
                                AddImageButton(
                                    onClick = { imagePickerLauncher.launch(PickVisualMediaRequest()) }
                                )
                            }
                        }
                    }
                }

//                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.sell_images_count, existingImageUrls.size + selectedImageUris.size),
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                var expanded by remember { mutableStateOf(false) }
                val defaultUnit = stringResource(R.string.unit_kg)
                var selectedUnit by remember(defaultUnit) { mutableStateOf(defaultUnit) }
                val units = listOf(
                    defaultUnit,
                    stringResource(R.string.unit_unit),
                    stringResource(R.string.unit_litre)
                )

                FieldLabel(text = stringResource(R.string.sell_stock_label))

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    // Text Field (takes remaining space)
                    ChiragBasicTextField(
                        value = availableStock,
                        onValueChange = { availableStock = it },
                        placeholder = stringResource(R.string.sell_stock_placeholder),
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )

                    // Dropdown (only required width)
                    Box {
                        Text(
                            text = selectedUnit,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { expanded = true }
                                .background(Color.LightGray)
                                .padding(horizontal = 12.dp, vertical = 12.dp))

                        DropdownMenu(
                            expanded = expanded, onDismissRequest = { expanded = false }) {
                            units.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(text = unit, fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        selectedUnit = unit
                                        expanded = false
                                    })
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Location
                FieldLabel(text = stringResource(R.string.sell_location_label))
                Spacer(modifier = Modifier.height(6.dp))
                LocationInputField(
                    value = location,
                    onValueChange = { viewModel.onLocationQueryChange(it) },
                    placeholder = stringResource(R.string.sell_location_placeholder),
                    suggestions = locationSuggestions,
                    onSuggestionClick = { viewModel.onLocationSelected(it) })

                Spacer(modifier = Modifier.height(16.dp))

                // Pricing
                FieldLabel(
                    text = stringResource(R.string.sell_pricing_label)
                )
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = pricing,
                    onValueChange = { pricing = it },
                    placeholder = stringResource(R.string.sell_price_placeholder),
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(16.dp))
                FieldLabel(text = stringResource(R.string.sell_discount_label))
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = discountPercent,
                    onValueChange = { discountPercent = it },
                    placeholder = stringResource(R.string.sell_discount_placeholder),
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Product Description
                FieldLabel(text = stringResource(R.string.sell_description_label))
                Spacer(modifier = Modifier.height(6.dp))
                MultiLineTextField(
                    value = productDescription,
                    onValueChange = { productDescription = it },
                    placeholder = stringResource(R.string.sell_description_placeholder),
                    minHeight = 120
                )
                Spacer(modifier = Modifier.height(16.dp))
                FieldLabel(text = stringResource(R.string.sell_features_label))
                Spacer(modifier = Modifier.height(6.dp))
                MultiLineTextField(
                    value = productFeatures,
                    onValueChange = { 
                        productFeatures = it
                        viewModel.onKeyFeaturesChange(it)
                    },
                    placeholder = stringResource(R.string.sell_features_placeholder),
                    minHeight = 80
                )
                Spacer(modifier = Modifier.height(16.dp))
                FieldLabel(text = stringResource(R.string.sell_delivery_fee_included_label))
//                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isDeliveryFeeIncludedInTotalPrice = true }
                    ) {
                        RadioButton(
                            selected = isDeliveryFeeIncludedInTotalPrice,
                            onClick = { isDeliveryFeeIncludedInTotalPrice = true }
                        )
                        Text(text = stringResource(R.string.sell_delivery_included), fontSize = 14.sp, color = Color.Black)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isDeliveryFeeIncludedInTotalPrice = false }
                    ) {
                        RadioButton(
                            selected = !isDeliveryFeeIncludedInTotalPrice,
                            onClick = { isDeliveryFeeIncludedInTotalPrice = false }
                        )
                        Text(text = stringResource(R.string.sell_delivery_not_included), fontSize = 14.sp, color = Color.Black)
                    }
                }

                if (!isDeliveryFeeIncludedInTotalPrice) {
                    Spacer(modifier = Modifier.height(6.dp))
                    FieldLabel(text = stringResource(R.string.sell_delivery_fee_label))
                    Spacer(modifier = Modifier.height(6.dp))
                    ChiragBasicTextField(
                        value = deliveryFeePerKm,
                        onValueChange = { deliveryFeePerKm = it },
                        placeholder = stringResource(R.string.sell_delivery_fee_placeholder),
                        keyboardType = KeyboardType.Number
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))

                // Submit Button
                ChiragButton(
                    text = if (fetchProductState is FetchProductState.Loading) stringResource(R.string.common_loading)
                    else if (addProductState is AddProductState.UploadingProduct) stringResource(R.string.sell_uploading_product)
                    else if (addProductState is AddProductState.Loading) stringResource(R.string.sell_submitting)
                    else if (isEditMode) stringResource(R.string.sell_update_product)
                    else stringResource(R.string.sell_submit), onClick = {
                        val keyFeaturesList = productFeatures.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        viewModel.submitProduct(
                            category = productCategory,
                            title = productTitle,
                            availableStock = availableStock,
                            price = pricing,
                            discount = discountPercent,
                            deliveryFee = if (isDeliveryFeeIncludedInTotalPrice) "" else deliveryFeePerKm,
                            description = productDescription,
                            isUpdate = isEditMode,
                            unit = selectedUnit,
                            quantity = availableStock.toIntOrNull(),
                            keyFeatures = keyFeaturesList
                        )
                    }, enabled = if (isEditMode) {
                        productCategory.isNotEmpty() && productTitle.isNotEmpty() && pricing.isNotEmpty() && (isDeliveryFeeIncludedInTotalPrice || deliveryFeePerKm.isNotBlank()) && addProductState !is AddProductState.Loading && addProductState !is AddProductState.UploadingProduct && fetchProductState !is FetchProductState.Loading
                    } else {
                        productCategory.isNotEmpty() && productTitle.isNotEmpty() && availableStock.isNotEmpty() && location.isNotEmpty() && pricing.isNotEmpty() && (isDeliveryFeeIncludedInTotalPrice || deliveryFeePerKm.isNotBlank()) && (selectedImageUris.isNotEmpty() || existingImageUrls.isNotEmpty()) && addProductState !is AddProductState.Loading && addProductState !is AddProductState.UploadingProduct && fetchProductState !is FetchProductState.Loading
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Loading indicator
            if (addProductState is AddProductState.Loading || addProductState is AddProductState.UploadingProduct || fetchProductState is FetchProductState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text, fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Normal
    )
}

@Composable
private fun ImageThumbnailWithRemove(
    imageUrl: String? = null,
    imageUri: Uri? = null,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .background(Color.LightGray)
    ) {
        // Display image
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = stringResource(R.string.sell_product_image_description),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = stringResource(R.string.sell_selected_image_description),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Remove button at top-right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Red)
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.sell_remove_image_description),
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun AddImageButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.sell_add_image_description),
            tint = Color.Gray,
            modifier = Modifier.size(40.dp)
        )
    }
}
