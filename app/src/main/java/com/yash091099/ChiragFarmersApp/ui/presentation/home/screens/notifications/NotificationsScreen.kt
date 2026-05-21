package com.yash091099.ChiragFarmersApp.ui.presentation.home.screens.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.home.screens.notifications.components.NotificationFilterChip
import com.yash091099.ChiragFarmersApp.ui.presentation.home.screens.notifications.components.NotificationItem
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite

data class NotificationFilterOption(
    val label: String,
    val apiValue: String
)

@Composable
fun NotificationsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val filters = remember {
        listOf(
            NotificationFilterOption(label = "All", apiValue = ""),
            NotificationFilterOption(label = "Buy", apiValue = "buy"),
            NotificationFilterOption(label = "Sell", apiValue = "sell"),
            NotificationFilterOption(label = "Service", apiValue = "service")
        )
    }

    var selectedFilter by rememberSaveable { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(selectedFilter) {
        viewModel.loadNotifications(type = selectedFilter.takeIf { it.isNotBlank() })
    }

    Scaffold(
        modifier = modifier,
        containerColor = BGWhite,
        topBar = {
            ChiragTopBar(
                navController = navController,
                icon = R.drawable.ic_arrow,
                title = "Notifications"
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(id = R.drawable.notifications_screen_bg),
                contentDescription = "Background illustration",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filters.forEach { filter ->
                        NotificationFilterChip(
                            label = filter.label,
                            isSelected = selectedFilter == filter.apiValue,
                            onClick = { selectedFilter = filter.apiValue }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                when (val state = uiState) {
                    is NotificationsUiState.Loading -> {
                        Spacer(modifier = Modifier.height(160.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = BGBlack
                        )
                        Spacer(modifier = Modifier.height(160.dp))
                    }

                    is NotificationsUiState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.message,
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = { viewModel.retry() }) {
                                Text(text = "Retry")
                            }
                        }
                    }

                    is NotificationsUiState.Success -> {
                        val groupedNotifications = remember(state.notifications) {
                            groupNotificationsByRecency(state.notifications)
                        }

                        if (state.notifications.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No notifications found",
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                            }
                        } else {
                            NotificationSection(
                                title = "Today",
                                notifications = groupedNotifications["Today"].orEmpty(),
                                navController = navController
                            )

                            NotificationSection(
                                title = "Yesterday",
                                notifications = groupedNotifications["Yesterday"].orEmpty(),
                                navController = navController
                            )

                            NotificationSection(
                                title = "Earlier",
                                notifications = groupedNotifications["Earlier"].orEmpty(),
                                navController = navController
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun NotificationSection(
    title: String,
    notifications: List<NotificationUiModel>,
    navController: NavHostController
) {
    if (notifications.isEmpty()) return

    Text(
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black,
        modifier = Modifier.padding(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))

    notifications.forEach { notification ->
        NotificationItem(
            imageUrl = notification.imageUrl,
            avatarRes = R.drawable.sprayer,
            title = notification.title,
            message = notification.body,
            timeAgo = notification.receivedAt,
            actionButtonText = notification.actionButtonText,
            onActionClick = {
                when {
                    !notification.bookingId.isNullOrBlank() -> {
                        navController.navigate(Route.Bookings.path)
                    }

                    !notification.orderId.isNullOrBlank() -> {
                        // Route based on notification tag
                        if (notification.tag == "sell") {
                            navController.navigate(Route.SellerOrderDetails.createRoute(notification.orderId))
                        } else {
                            // Default to buyer's order (buy tag or any other tag)
                            navController.navigate(Route.OrderDetails.createRoute(notification.orderId))
                        }
                    }
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

private fun groupNotificationsByRecency(
    notifications: List<NotificationUiModel>
): Map<String, List<NotificationUiModel>> {
    val today = mutableListOf<NotificationUiModel>()
    val yesterday = mutableListOf<NotificationUiModel>()
    val earlier = mutableListOf<NotificationUiModel>()

    notifications.forEach { notification ->
        val receivedAt = notification.receivedAt.lowercase()
        when {
            receivedAt.contains("yesterday") -> yesterday.add(notification)
            receivedAt.contains("ago") || receivedAt.contains("today") -> today.add(notification)
            else -> earlier.add(notification)
        }
    }

    return linkedMapOf(
        "Today" to today,
        "Yesterday" to yesterday,
        "Earlier" to earlier
    )
}