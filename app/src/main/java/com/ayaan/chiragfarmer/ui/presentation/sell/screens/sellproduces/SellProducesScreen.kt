package com.ayaan.chiragfarmer.ui.presentation.sell.screens.sellproduces

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.common.components.ChiragBasicTextField
import com.ayaan.chiragfarmer.ui.presentation.common.components.ChiragButton
import com.ayaan.chiragfarmer.ui.presentation.common.components.MultiLineTextField
import com.ayaan.chiragfarmer.ui.presentation.common.components.ProductImageUpload
import com.ayaan.chiragfarmer.ui.presentation.home.components.bookservicecard.components.LocationInputField
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.ChiragTopBar
import com.ayaan.chiragfarmer.ui.theme.BGWhite

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SellProducesScreen(
    navController: NavHostController,
    productId: String? = null,
    viewModel: SellProducesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var productCategory by remember { mutableStateOf("") }
    var productTitle by remember { mutableStateOf("") }
    var availableStock by remember { mutableStateOf("") }
    val location by viewModel.locationQuery.collectAsStateWithLifecycle()
    val locationSuggestions by viewModel.locationSuggestions.collectAsStateWithLifecycle()
    var pricing by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }

    val imageUri by viewModel.imageUri.collectAsStateWithLifecycle()
    val existingImageUrl by viewModel.existingImageUrl.collectAsStateWithLifecycle()
    val addProductState by viewModel.addProductState.collectAsStateWithLifecycle()
    val fetchProductState by viewModel.fetchProductState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val isEditMode = productId != null
    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImageSelected(uri)
    }

    // Fetch product details if editing
    LaunchedEffect(productId) {
        if (productId != null) {
            viewModel.fetchProductDetails(productId)
        }
    }

    // Handle fetch product state
    LaunchedEffect(fetchProductState) {
        when (val state = fetchProductState) {
            is FetchProductState.Success -> {
                val product = state.product
                productCategory = product.category ?: ""
                productTitle = product.title
                availableStock = product.availableStockWeight?.toString() ?: ""
                pricing = product.price.toString()
                productDescription = product.description ?: ""
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
                navController.popBackStack()
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
                title = if (isEditMode) "Edit Product" else "Sell Produces"
            )
        },
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
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
                    text = "Produce Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Product Category
                FieldLabel(text = "Produce Category")
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = productCategory,
                    onValueChange = { productCategory = it },
                    placeholder = "Wheat, Rice, Maize, Barley, Millets Etc......."
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Product Title
                FieldLabel(text = "Produce Tittle")
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = productTitle,
                    onValueChange = { productTitle = it },
                    placeholder = "Enter Produce Tittle"
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Images
                FieldLabel(text = "Images")
                Spacer(modifier = Modifier.height(10.dp))

                // Show existing image or selected new image
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Selected product image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable{
                                imagePickerLauncher.launch("image/*")
                            }
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                } else if (existingImageUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(existingImageUrl),
                        contentDescription = "Existing product image",
                        modifier = Modifier
                            .size(100.dp)
                            .clickable{
                                imagePickerLauncher.launch("image/*")
                            }
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }else{
                    ProductImageUpload(
                        onClick = {
                            imagePickerLauncher.launch("image/*")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Available Stock Weight
                FieldLabel(text = "Available Stock Weight (Kg) *")
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = availableStock,
                    onValueChange = { availableStock = it },
                    placeholder = "KG , Grams , Litres",
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Location
                FieldLabel(text = "Location")
                Spacer(modifier = Modifier.height(6.dp))
                LocationInputField(
                    value = location,
                    onValueChange = { viewModel.onLocationQueryChange(it) },
                    placeholder = "Prtapgarh, Uttar pradesh",
                    suggestions = locationSuggestions,
                    onSuggestionClick = { viewModel.onLocationSelected(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Pricing
                FieldLabel(text = "Pricing")
                Spacer(modifier = Modifier.height(6.dp))
                ChiragBasicTextField(
                    value = pricing,
                    onValueChange = { pricing = it },
                    placeholder = "Enter Price",
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Product Description
                FieldLabel(text = "Produce Description")
                Spacer(modifier = Modifier.height(6.dp))
                MultiLineTextField(
                    value = productDescription,
                    onValueChange = { productDescription = it },
                    placeholder = "Add Produce Description",
                    minHeight = 120
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Submit Button
                ChiragButton(
                    text = if (fetchProductState is FetchProductState.Loading)
                        "Loading..."
                    else if (addProductState is AddProductState.UploadingImage)
                        "Uploading Image..."
                    else if (addProductState is AddProductState.Loading)
                        "Submitting..."
                    else if (isEditMode)
                        "Update Product"
                    else
                        "Submit",
                    onClick = {
                        viewModel.submitProduct(
                            context = context,
                            category = productCategory,
                            title = productTitle,
                            availableStock = availableStock,
                            price = pricing,
                            description = productDescription,
                            isUpdate = isEditMode
                        )
                    },
                    enabled = if (isEditMode) {
                        productTitle.isNotEmpty() &&
                        pricing.isNotEmpty() &&
                        addProductState !is AddProductState.Loading &&
                        addProductState !is AddProductState.UploadingImage &&
                        fetchProductState !is FetchProductState.Loading
                    } else {
                        productTitle.isNotEmpty() &&
                        availableStock.isNotEmpty() &&
                        location.isNotEmpty() &&
                        pricing.isNotEmpty() &&
                        imageUri != null &&
                        addProductState !is AddProductState.Loading &&
                        addProductState !is AddProductState.UploadingImage &&
                        fetchProductState !is FetchProductState.Loading
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Loading indicator
            if (addProductState is AddProductState.Loading ||
                addProductState is AddProductState.UploadingImage ||
                fetchProductState is FetchProductState.Loading) {
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