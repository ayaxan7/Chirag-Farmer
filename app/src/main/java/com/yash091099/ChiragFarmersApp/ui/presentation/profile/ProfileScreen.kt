package com.yash091099.ChiragFarmersApp.ui.presentation.profile

import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.BuildConfig
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.presentation.profile.components.MenuItem
import com.yash091099.ChiragFarmersApp.ui.presentation.profile.components.QuickActionCard
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BackgroundGray
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavHostController, viewModel: ProfileViewModel = hiltViewModel()
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
    BackHandler {
        navController.navigate(Route.Home.path) {
            popUpTo(0) { inclusive = true }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }
    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = stringResource(R.string.profile_title),
                icon = R.drawable.ic_arrow,
                onBackClick = {
                    navController.navigate(Route.Home.path) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }, containerColor = BGWhite
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
                            name = profile.username ?: stringResource(R.string.profile_farmer_fallback),
                            phone = profile.phoneNumber ?: stringResource(R.string.profile_phone_unavailable),
                            email = profile.email ?: stringResource(R.string.profile_email_unavailable),
                            profileImageUrl = profile.profileImage,
                            onEditClick = { navController.navigate(Route.EditProfile.path) })
                    }

                    item {
                        QuickActionsRow(
                            navController = navController
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(thickness = 10.dp, color = BackgroundGray)
                    }

                    item {
                        MenuListSection(
                            navController = navController
                        )
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 24.dp)
                        ) {
                            ChiragButton(
                                text = stringResource(R.string.profile_logout),
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
                            text = stringResource(R.string.profile_version, BuildConfig.VERSION_NAME),
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
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.Black)
    }
}

@Composable
private fun ProfileErrorState(
    message: String, onRetry: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message, color = Color.Red, fontSize = 16.sp, textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.profile_retry))
        }
    }
}

@Composable
fun ProfileHeader(
    name: String, phone: String, profileImageUrl: String?, onEditClick: () -> Unit, email: String
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
                contentDescription = stringResource(R.string.profile_image_description),
                placeholder = painterResource(id = R.drawable.profile_placeholder),
                error = painterResource(id = R.drawable.profile_placeholder),
                fallback = painterResource(id = R.drawable.profile_placeholder),
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(1.dp, BorderColour, CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = BGBlack
            )
            Text(
                text = stringResource(R.string.profile_phone_format, phone), fontSize = 14.sp, color = TextGray
            )
            Text(
                text = email,
                fontSize = 14.sp,
                color = TextGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(onClick = onEditClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit_profile),
                contentDescription = stringResource(R.string.profile_edit_description),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun QuickActionsRow(
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        QuickActionCard(
            title = stringResource(R.string.profile_orders_title),
            icon = R.drawable.ic_ordersplaced,
            modifier = Modifier.width(110.dp),
            onClick = {
                navController.navigate(Route.MyOrders.path)
            })
        Spacer(
            modifier = Modifier.size(16.dp)
        )
        QuickActionCard(
            title = stringResource(R.string.profile_help_title),
            icon = R.drawable.ic_help_n_support,
            modifier = Modifier.width(110.dp)
        )
    }
}

@Composable
fun MenuListSection(
    navController: NavHostController
) {
    Column {
        MenuItem(icon = R.drawable.ic_wallet, title = stringResource(R.string.profile_wallet), onClick = { navController.navigate(Route.Wallet.path) })
        MenuItem(
            icon = R.drawable.location,
            title = stringResource(R.string.profile_manage_addresses),
            onClick = { navController.navigate(Route.AddressList.path) })
        MenuItem(icon = R.drawable.ic_terms_n_conditions, title = stringResource(R.string.profile_terms))
        MenuItem(icon = R.drawable.ic_privacy_policy, title = stringResource(R.string.profile_privacy))
        MenuItem(
            icon = R.drawable.ic_language,
            title = stringResource(R.string.profile_language),
            onClick = { navController.navigate(Route.Language.path) })
        MenuItem(icon = R.drawable.ic_rating, title = stringResource(R.string.profile_rate_app))
    }
}