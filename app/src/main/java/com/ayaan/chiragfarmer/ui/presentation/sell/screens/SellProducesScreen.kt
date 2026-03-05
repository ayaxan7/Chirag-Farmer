package com.ayaan.chiragfarmer.ui.presentation.sell.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
    viewModel: SellProducesViewModel = hiltViewModel()
) {
    var productCategory by remember { mutableStateOf("") }
    var productTitle by remember { mutableStateOf("") }
    var availableStock by remember { mutableStateOf("") }
    val location by viewModel.locationQuery.collectAsStateWithLifecycle()
    val locationSuggestions by viewModel.locationSuggestions.collectAsStateWithLifecycle()
    var pricing by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController,
                icon = R.drawable.ic_arrow,
                title = "Sell Produces"
            )
        }, containerColor = BGWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
            ProductImageUpload(
                onClick = {
                    // Handle image selection
                })

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
                text = "Submit",
                onClick = {
                    // Handle form submission
                },
                enabled = productCategory.isNotEmpty() && productTitle.isNotEmpty() && availableStock.isNotEmpty() && location.isNotEmpty() && pricing.isNotEmpty()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text, fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight.Normal
    )
}