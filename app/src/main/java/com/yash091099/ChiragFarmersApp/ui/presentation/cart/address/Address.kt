package com.yash091099.ChiragFarmersApp.ui.presentation.cart.address

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.data.remote.dto.FarmerAddressDto
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.TextDarkGray
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray
import com.yash091099.ChiragFarmersApp.utils.dashedBorder
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddressScreen(
    navController: NavHostController,
    viewModel: AddressListViewModel,
    onContinueClick: () -> Unit = {},
    onAddAddressClick: () -> Unit = {}
) {
    val addressState by viewModel.addressState.collectAsState()
    val isOperationInProgress by viewModel.isOperationInProgress.collectAsState()
    var selectedAddressId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                AddressListNavigationEvent.NavigateBackToCart -> {
                    navController.popBackStack()
                }
            }
        }
    }

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
                        title = stringResource(R.string.address_title), icon = R.drawable.ic_arrow, navController = navController
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
                            text = stringResource(R.string.address_error_loading),
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
                            Text(stringResource(R.string.address_retry), color = BGWhite)
                        }
                    }
                }
            }
        }

        is AddressListUiState.Empty -> {
            Scaffold(
                topBar = {
                    ChiragTopBar(
                        title = stringResource(R.string.address_title), icon = R.drawable.ic_arrow, navController = navController
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
                            text = stringResource(R.string.address_continue),
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
                            text = stringResource(R.string.address_empty_title),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BGBlack
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.address_empty_subtitle),
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
                    title = stringResource(R.string.address_title), icon = R.drawable.ic_arrow, navController = navController
                )
            }, bottomBar = {
                Column(
                    modifier = Modifier
                        .background(BGWhite)
                        .padding(16.dp)
                ) {
                    if (addresses.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.address_selected_info),
                            fontSize = 12.sp,
                            color = TextGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    ChiragButton(
                        text = if (isOperationInProgress) stringResource(R.string.address_updating) else stringResource(R.string.address_continue),
                        onClick = {
                            selectedAddressId?.let { id ->
                                viewModel.setDefaultAddress(id)
                            } ?: onContinueClick()
                        },
                        enabled = !isOperationInProgress,
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
            contentDescription = stringResource(R.string.address_location_description),
            modifier = Modifier
                .padding(top = 6.dp)
                .size(24.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1.0f)) {
            Text(
                text = address.name ?: stringResource(R.string.fallback_address),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = address.addressString ?: "",
                fontSize = 13.sp,
                color = TextDarkGray,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = stringResource(R.string.address_pin_label), fontSize = 13.sp, color = TextGray
                )
                Text(
                    text = address.pincode ?: "", fontSize = 13.sp, color = Color.Black
                )
            }
//            // Show receiver details if available
//            if (!address.receiverName.isNullOrBlank()) {
//                Spacer(modifier = Modifier.height(4.dp))
//                Row {
//                    Text(
//                        text = "Receiver: ", fontSize = 12.sp, color = TextGray
//                    )
//                    Text(
//                        text = address.receiverName, fontSize = 12.sp, color = TextDarkGray
//                    )
//                }
//            }
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
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xfff0f0f0), RoundedCornerShape(28.dp))
            .dashedBorder(
                width = 1.dp, color = BGBlack.copy(alpha = 0.5f), cornerRadius = 28.dp
            )
            .clickable { onClick() }, contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.address_add_button),
                modifier = Modifier.size(20.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.address_add_button),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

