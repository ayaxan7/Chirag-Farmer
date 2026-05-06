package com.yash091099.ChiragFarmersApp.ui.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        if (uiState is ProfileUiState.Unauthorized) {
            navController.navigate(Route.Auth.path) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = "My Account",
                icon = R.drawable.ic_arrow,
            )
        },
        containerColor = BGWhite
    ) { paddingValues ->
        when (uiState) {
            ProfileUiState.Loading -> ProfileLoadingState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )

            is ProfileUiState.Error -> ProfileErrorState(
                message = (uiState as ProfileUiState.Error).message,
                onRetry = { viewModel.loadProfile() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )

            ProfileUiState.Unauthorized -> ProfileLoadingState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )

            is ProfileUiState.Success -> {
                val profile = (uiState as ProfileUiState.Success).profile
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item {
                        ProfileHeader(
                            name = profile.username ?: "Farmer",
                            phone = profile.phoneNumber ?: "Phone unavailable",
                            profileImageUrl = profile.profileImage,
                            onEditClick = { /* TODO: edit profile */ }
                        )
                    }

                    item {
                        QuickActionsRow()
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(thickness = 10.dp, color = BackgroundGray)
                    }

                    item {
                        MenuListSection()
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 24.dp)
                        ) {
                            ChiragButton(
                                text = "Logout",
                                onClick = {
                                    scope.launch {
                                        viewModel.logout()
                                        navController.navigate(Route.Auth.path) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    item {
                        Text(
                            text = "Version 2.90",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 32.dp),
                            textAlign = TextAlign.Center,
                            color = TextGray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.Black)
    }
}

@Composable
private fun ProfileErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = Color.Red,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun ProfileHeader(
    name: String,
    phone: String,
    profileImageUrl: String?,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            AsyncImage(
                model = profileImageUrl,
                contentDescription = "Profile Image",
                placeholder = painterResource(id = R.drawable.profile_placeholder),
                error = painterResource(id = R.drawable.profile_placeholder),
                fallback = painterResource(id = R.drawable.profile_placeholder),
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(1.dp, BorderColour, CircleShape),
                contentScale = ContentScale.Crop
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(24.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = "Edit Profile Image",
                    modifier = Modifier.padding(4.dp),
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        Column(modifier = Modifier.weight(1.0f)) {
            Text(
                text = name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "+91 $phone",
                fontSize = 14.sp,
                color = TextGray
            )
        }

        IconButton(onClick = onEditClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "Edit Profile",
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
    }
}

@Composable
fun QuickActionsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QuickActionCard(
            title = "Orders\nplaced",
            icon = R.drawable.ic_ordersplaced,
            modifier = Modifier.weight(1f)
        )
        QuickActionCard(
            title = "Help &\nSupport",
            icon = R.drawable.ic_help_n_support,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun QuickActionCard(title: String, icon: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BGWhite),
        border = BorderStroke(1.dp, BorderColour)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun MenuListSection() {
    Column {
        MenuItem(icon = R.drawable.profile_icon, title = "My profile")
        MenuItem(icon = R.drawable.ic_wallet, title = "Wallet")
        MenuItem(icon = R.drawable.ic_view_seller, title = "Bank Details")
        MenuItem(icon = R.drawable.location, title = "Manage Addresses")
        MenuItem(icon = R.drawable.qna, title = "Terms and conditions")
        MenuItem(icon = R.drawable.ic_cart, title = "Privacy Policy")
        MenuItem(icon = R.drawable.ic_share, title = "Language")
        MenuItem(icon = R.drawable.ic_rating_star, title = "Rate app")
    }
}

@Composable
fun MenuItem(icon: Int, title: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextGray
        )
    }
}
