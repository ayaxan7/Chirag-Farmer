package com.yash091099.ChiragFarmersApp.ui.presentation.cart.address

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.data.remote.dto.FarmerAddressDto
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.TextDarkGray
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray
import com.yash091099.ChiragFarmersApp.utils.dashedBorder

@Composable
fun AddressScreen(
    navController: NavHostController,
    viewModel: AddressListViewModel,
    onContinueClick: () -> Unit = {},
    onAddAddressClick: () -> Unit = {}
) {
    val addressState by viewModel.addressState.collectAsState()
    var selectedAddressId by remember { mutableStateOf<String?>(null) }

    when (val state = addressState) {
        is AddressListUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BGWhite),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BGBlack)
            }
        }

        is AddressListUiState.Error -> {
            Scaffold(
                topBar = {
                    ChiragTopBar(
                        title = "Address", icon = R.drawable.ic_arrow, navController = navController
                    )
                }, containerColor = BGWhite
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Error Loading Addresses",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BGBlack
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.message, fontSize = 14.sp, color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.retry() },
                            colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                        ) {
                            Text("Retry", color = BGWhite)
                        }
                    }
                }
            }
        }

        is AddressListUiState.Empty -> {
            Scaffold(
                topBar = {
                    ChiragTopBar(
                        title = "Address", icon = R.drawable.ic_arrow, navController = navController
                    )
                },
                containerColor = BGWhite,
                bottomBar = {
                    Column(
                        modifier = Modifier
                            .background(BGWhite)
                            .padding(16.dp)
                    ) {
                        ChiragButton(
                            text = "Continue",
                            onClick = onContinueClick,
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                },
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "No Addresses Found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BGBlack
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add your first address to get started",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AddAddressButton(onClick = onAddAddressClick)
                        Spacer(modifier = Modifier.height(16.dp))

                    }
                }
            }
        }

        is AddressListUiState.Success -> {
            val addresses = state.addresses
            if (selectedAddressId == null && addresses.isNotEmpty()) {
                selectedAddressId = addresses.first().id
            }

            Scaffold(
                topBar = {
                ChiragTopBar(
                    title = "Address", icon = R.drawable.ic_arrow, navController = navController
                )
            }, bottomBar = {
                Column(
                    modifier = Modifier
                        .background(BGWhite)
                        .padding(16.dp)
                ) {
                    // Only show note if we have addresses
                    if (addresses.isNotEmpty()) {
                        Text(
                            text = "Selected address will be used for delivery",
                            fontSize = 12.sp,
                            color = TextGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    ChiragButton(
                        text = "Continue",
                        onClick = onContinueClick,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }, containerColor = BGWhite
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    items(addresses) { address ->
                        FarmerAddressItem(
                            address = address,
                            isSelected = selectedAddressId == address.id,
                            onSelect = { selectedAddressId = address.id })
                        if (address != addresses.last()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 1.dp,
                                color = BorderColour.copy(alpha = 0.5f)
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        AddAddressButton(onClick = onAddAddressClick)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FarmerAddressItem(
    address: FarmerAddressDto, isSelected: Boolean, onSelect: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onSelect() }
        .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top) {
        Icon(
            painter = painterResource(R.drawable.location),
            contentDescription = "Location",
            modifier = Modifier
                .padding(top = 6.dp)
                .size(24.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1.0f)) {
            Text(
                text = address.city,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = address.address, fontSize = 13.sp, color = TextDarkGray, lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = "Pin : ", fontSize = 13.sp, color = TextGray
                )
                Text(
                    text = address.pincode, fontSize = 13.sp, color = Color.Black
                )
            }
            // Show landmark if available
            if (!address.landmark.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Landmark: ", fontSize = 12.sp, color = TextGray
                    )
                    Text(
                        text = address.landmark, fontSize = 12.sp, color = TextDarkGray
                    )
                }
            }
        }
        RadioButton(
            selected = isSelected, onClick = onSelect, colors = RadioButtonDefaults.colors(
                selectedColor = Color.Black, unselectedColor = BorderColour
            ), modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun AddAddressButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xfff0f0f0), RoundedCornerShape(28.dp))
            .dashedBorder(
                width = 1.dp, color = BGBlack.copy(alpha = 0.5f), cornerRadius = 28.dp
            )
            .clickable { onClick() }, contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Add Address",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

