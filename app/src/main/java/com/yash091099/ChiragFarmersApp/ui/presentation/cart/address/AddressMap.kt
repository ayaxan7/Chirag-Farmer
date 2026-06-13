package com.yash091099.ChiragFarmersApp.ui.presentation.cart.address

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.ChiragFarmerTheme
import com.yash091099.ChiragFarmersApp.ui.theme.TextDarkGray
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressMapScreen(
    navController: NavHostController,
    viewModel: AddressMapViewModel = hiltViewModel(),
    onAddMoreDetailsClick: () -> Unit = {}
) {
    val currentLocation by viewModel.currentLocation.collectAsState()
    val currentAddress by viewModel.currentAddress.collectAsState()
    val locationQuery by viewModel.locationQuery.collectAsState()
    val locationSuggestions by viewModel.locationSuggestions.collectAsState()
    val isLoadingLocation by viewModel.isLoadingLocation.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val hasDefaultLocation by viewModel.hasDefaultLocation.collectAsState()

    var showLocationSelectionSheet by remember { mutableStateOf(false) }
    var showAddressDetailsSheet by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                AddressMapNavigationEvent.NavigateBackToCart -> {
                    navController.popBackStack(Route.Cart.path, false)
                }
            }
        }
    }

    // Show error snackbar
    val unknownAddrError = stringResource(R.string.snackbar_address_unknown_error)
    if (!errorMessage.isNullOrEmpty()) {
        scope.launch {
            snackbarHostState.showSnackbar(errorMessage ?: unknownAddrError)
            viewModel.clearError()
        }
    }

    if (showLocationSelectionSheet) {
        LocationSelectionBottomSheet(
            onDismissRequest = { showLocationSelectionSheet = false },
            onLocationSelected = { address ->
                // ViewModel now handles marker relocation and address update in onLocationSelected
                showLocationSelectionSheet = false
            },
            locationQuery = locationQuery,
            locationSuggestions = locationSuggestions,
            onLocationQueryChange = { viewModel.onLocationQueryChange(it) },
            onSuggestionClick = { viewModel.onLocationSelected(it) }
        )
    }

    if (showAddressDetailsSheet) {
        AddressDetailsBottomSheet(
            onDismissRequest = { showAddressDetailsSheet = false },
            onConfirmClick = {
                viewModel.addDeliveryLocation()
            },
            onChangeLocationClick = {
                showAddressDetailsSheet = false
                showLocationSelectionSheet = true
            },
            currentAddress = currentAddress,
            viewModel = viewModel
        )
    }

    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController, title = stringResource(R.string.address_map_title), icon = R.drawable.ic_arrow
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BGWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Map Section
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // OpenStreetMap View
                OpenStreetMapView(
                    modifier = Modifier.fillMaxSize(),
                    currentLocation = currentLocation,
                    onMapLongClick = { geoPoint ->
                        viewModel.updateLocationFromCoordinates(
                            geoPoint.latitude,
                            geoPoint.longitude
                        )
                    }
                )

                // Search Bar Overlay
                SearchBarOverlay(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopCenter),
                    onClick = {showLocationSelectionSheet = true }
                )

                // Use Current Location Button
                UseCurrentLocationButton(
                    modifier = Modifier
                        .width(200.dp)
                        .padding(bottom = 24.dp)
                        .align(Alignment.BottomCenter),
                    isLoading = isLoadingLocation,
                    onClick = { viewModel.fetchCurrentLocation() }
                )
            }

            // Bottom Details Section - Only show if default location exists
            if (hasDefaultLocation) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BGWhite)
                        .padding(16.dp)
                ) {
                    AddressDetailCard(
                        type = "Location",
                        address = currentAddress,
                        onChangeClick = { showLocationSelectionSheet = true })

                    Spacer(modifier = Modifier.height(20.dp))

                    ChiragButton(
                        text = stringResource(R.string.address_map_details_title), onClick = {
                            showAddressDetailsSheet = true
                            onAddMoreDetailsClick()
                        }, modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                // Show only the "Add more address details" button when no default location
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BGWhite)
                        .padding(16.dp)
                ) {
                    ChiragButton(
                        text = stringResource(R.string.address_map_details_title), onClick = {
                            showAddressDetailsSheet = true
                            onAddMoreDetailsClick()
                        }, modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun SearchBarOverlay(modifier: Modifier = Modifier,onClick: () -> Unit={}) {
    Card(
        modifier = modifier
            .clickable{onClick()}
            .fillMaxWidth()
            .height(56.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = stringResource(R.string.address_map_search_description),
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.address_map_search_placeholder), color = Color.Gray, fontSize = 14.sp
            )
        }
    }
}

@Composable
fun UseCurrentLocationButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(14.dp))
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(14.dp))
            .clickable(enabled = !isLoading) { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Color.Black
                )
            } else {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isLoading) "Fetching location..." else "Use Current Location",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AddressDetailCard(
    type: String, address: String, onChangeClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(id = R.drawable.location),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = type, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black
                )
                Text(
                    text = stringResource(R.string.address_map_change),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier.clickable { onChangeClick() })
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = address, fontSize = 14.sp, color = TextDarkGray, lineHeight = 20.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelectionBottomSheet(
    onDismissRequest: () -> Unit,
    onLocationSelected: (String) -> Unit,
    locationQuery: String,
    locationSuggestions: List<com.yash091099.ChiragFarmersApp.domain.model.Location>,
    onLocationQueryChange: (String) -> Unit,
    onSuggestionClick: (com.yash091099.ChiragFarmersApp.domain.model.Location) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(),
        containerColor = Color(0xFFF7F8FA),
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.address_map_select_location),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                IconButton(onClick = onDismissRequest) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.address_map_close_description),
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            ) {
                Column {
                    com.yash091099.ChiragFarmersApp.ui.presentation.home.components.bookservicecard.components.LocationInputField(
                        value = locationQuery,
                        onValueChange = onLocationQueryChange,
                        placeholder = stringResource(R.string.address_map_search_placeholder),
                        suggestions = locationSuggestions,
                        onSuggestionClick = {
                            onSuggestionClick(it)
                            onLocationSelected(it.displayName)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(300.dp))
            }
    }
}

@Composable
fun RecentLocationItem(
    title: String, address: String, distance: String, onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = distance,
                    fontSize = 10.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = address, fontSize = 13.sp, color = Color.Gray, lineHeight = 18.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressDetailsBottomSheet(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    onChangeLocationClick: () -> Unit,
    currentAddress: String,
    viewModel: AddressMapViewModel = hiltViewModel()
) {
    val receiverName by viewModel.receiverName.collectAsState()
    val receiverContact by viewModel.receiverContact.collectAsState()
    val floor by viewModel.floor.collectAsState()
    val landmark by viewModel.landmark.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isLoading by viewModel.isLoadingLocation.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            // Scrollable Form Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.address_map_enter_details),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.address_map_close_description),
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Receiver's Name
                AddressInputField(
                    label = stringResource(R.string.address_map_receiver_name_label),
                    value = receiverName,
                    onValueChange = { viewModel.receiverName.value = it },
                    placeholder = stringResource(R.string.address_map_receiver_name_placeholder)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Receiver's Contact
                AddressInputField(
                    label = stringResource(R.string.address_map_receiver_contact_label),
                    value = receiverContact,
                    onValueChange = { viewModel.receiverContact.value = it },
                    placeholder = stringResource(R.string.address_map_receiver_contact_placeholder),
                    keyboardType= KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Address Category Chips
                val categories = listOf(
                    "Home" to Icons.Default.Home,
                    "Work" to Icons.Default.Work,
                    "Hotel" to Icons.Default.Apartment,
                    "Other" to Icons.Default.LocationOn
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(categories) { (name, icon) ->
                        AddressTypeChip(
                            name = name,
                            icon = icon,
                            isSelected = selectedCategory == name,
                            onClick = { viewModel.selectedCategory.value = name })
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Current Address Summary
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currentAddress,
                            modifier = Modifier.weight(1f),
                            fontSize = 13.sp,
                            color = Color.Gray,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = stringResource(R.string.address_map_change_label),
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { onChangeLocationClick() })
                    }
                }

                Text(
                    text = stringResource(R.string.address_map_update_hint),
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Additional Form Fields
                AddressInputField(
                    label = stringResource(R.string.address_map_floor_label),
                    value = floor,
                    placeholder = stringResource(R.string.address_map_floor_placeholder),
                    onValueChange = { viewModel.floor.value = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AddressInputField(
                    label = stringResource(R.string.address_map_landmark_label),
                    value = landmark,
                    placeholder = stringResource(R.string.address_map_landmark_placeholder),
                    onValueChange = { viewModel.landmark.value = it }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Sticky Footer for Confirm Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ChiragButton(
                    text = if (isLoading) "Saving..." else "Confirm address",
                    onClick = onConfirmClick,
                    enabled = !isLoading && receiverName.isNotBlank() && receiverContact.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun AddressInputField(
    label: String, value: String = "", placeholder: String = "", onValueChange: (String) -> Unit,keyboardType: KeyboardType= KeyboardType.Text
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = placeholder, color = Color.Gray, fontSize = 14.sp) },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color.Black,
                cursorColor = Color.Black
            ),
            singleLine = true
        )
    }
}

@Composable
fun AddressTypeChip(
    name: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .shadow(if (isSelected) 2.dp else 0.dp, RoundedCornerShape(8.dp))
            .background(if (isSelected) Color.Black else Color.White, RoundedCornerShape(8.dp))
            .border(
                1.dp, if (isSelected) Color.Black else Color(0xFFE0E0E0), RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color.White else Color.Black,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = name,
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddressMapScreenPreview() {
    ChiragFarmerTheme {
        AddressMapScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun LocationSelectionBottomSheetPreview() {
    ChiragFarmerTheme {
        LocationSelectionBottomSheet(
            onDismissRequest = {},
            onLocationSelected = {},
            locationQuery = "",
            locationSuggestions = emptyList(),
            onLocationQueryChange = {},
            onSuggestionClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddressDetailsBottomSheetPreview() {
    ChiragFarmerTheme {
        AddressDetailsBottomSheet(
            onDismissRequest = {},
            onConfirmClick = {},
            onChangeLocationClick = {},
            currentAddress = "Sample Address")
    }
}
