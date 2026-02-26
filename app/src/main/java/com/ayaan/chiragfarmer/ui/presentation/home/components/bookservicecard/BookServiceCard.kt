package com.ayaan.chiragfarmer.ui.presentation.home.components.bookservicecard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.ayaan.chiragfarmer.ui.presentation.common.components.ChiragBasicTextField
import com.ayaan.chiragfarmer.ui.presentation.common.components.ChiragButton
import com.ayaan.chiragfarmer.ui.presentation.home.components.bookservicecard.components.LocationInputField
import com.ayaan.chiragfarmer.ui.presentation.home.components.bookservicecard.components.SearchInputField
import com.ayaan.chiragfarmer.ui.presentation.home.components.bookservicecard.components.ServiceDropdown
import com.ayaan.chiragfarmer.ui.theme.BGWhite
import com.ayaan.chiragfarmer.ui.theme.LightGray

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ayaan.chiragfarmer.ui.presentation.home.HomeViewModel
import com.ayaan.chiragfarmer.ui.presentation.home.BookingStatus

@Composable
fun BookServiceCard(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    isEnabled: Boolean
) {
    val locationQuery by viewModel.locationQuery.collectAsStateWithLifecycle()
    val locationSuggestions by viewModel.locationSuggestions.collectAsStateWithLifecycle()
    val selectedService by viewModel.selectedService.collectAsStateWithLifecycle()
    val farmArea by viewModel.farmArea.collectAsStateWithLifecycle()
    val cropName by viewModel.cropName.collectAsStateWithLifecycle()
    val bookingStatus by viewModel.bookingStatus.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = BGWhite,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = LightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)

    ) {
        // Title
        Text(
            text = "Book your service",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Select Service Label
        Text(
            text = "Select Service",
            fontSize = 13.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(2.dp))

        // Select Service Dropdown
        ServiceDropdown(
            selectedService = selectedService,
            placeholder = "Select Service",
            onServiceSelected = { viewModel.onServiceSelected(it) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Farm Location Label
        Text(
            text = "Farm Location",
            fontSize = 13.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(2.dp))

        // Farm Location Input
        LocationInputField(
            value = locationQuery,
            onValueChange = { viewModel.onLocationQueryChange(it) },
            placeholder = "Pratapgarh, Uttar pradesh",
            suggestions = locationSuggestions,
            onSuggestionClick = { viewModel.onLocationSelected(it) }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Farm Area Label
        Text(
            text = "Farm Area (in Acres)",
            fontSize = 13.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(2.dp))

        // Farm Area Input
        ChiragBasicTextField(
            value = farmArea,
            onValueChange = { viewModel.onFarmAreaChange(it) },
            placeholder = "Enter Your Farm area in acres",
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Search Crop Label
        Text(
            text = "Search crop",
            fontSize = 13.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(2.dp))

        // Search Crop Input
        SearchInputField(
            value = cropName,
            onValueChange = { viewModel.onCropNameChange(it) },
            placeholder = "Enter"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Book Now Button
        ChiragButton(
            text = "Book Now",
            onClick = { viewModel.createBooking() },
            enabled = selectedService.isNotEmpty() &&
                    locationQuery.isNotEmpty() &&
                    farmArea.isNotEmpty() &&
                    cropName.isNotEmpty() &&
                    isEnabled && 
                    bookingStatus !is BookingStatus.Loading
        )
    }
}