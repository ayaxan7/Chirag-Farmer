package com.ayaan.chiragfarmer.ui.presentation.home.screens.notifications

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.home.screens.notifications.components.NotificationFilterChip
import com.ayaan.chiragfarmer.ui.presentation.home.screens.notifications.components.NotificationItem
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.ChiragTopBar

data class NotificationData(
    val avatarRes: Int,
    val title: String,
    val message: String,
    val timeAgo: String,
    val actionButtonText: String,
    val category: String // "all", "buy", "sell", "service"
)

@Composable
fun NotificationsScreen(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }

    // Sample notifications data
    val allNotifications = remember {
        listOf(
            // Today
            NotificationData(
                avatarRes = R.drawable.sprayer,
                title = "Sprayer (20L) is out for delivery.",
                message = "Track your order for updates.",
                timeAgo = "1m ago",
                actionButtonText = "Shop Now",
                category = "buy"
            ), NotificationData(
                avatarRes = R.drawable.sprayer,
                title = "Flash Sale on Vegetable Seeds!",
                message = "Up to 40% off for the next 6 hours.",
                timeAgo = "1m ago",
                actionButtonText = "Shop Now",
                category = "buy"
            ), NotificationData(
                avatarRes = R.drawable.sprayer,
                title = "You received a new Order on your Wheat listing",
                message = "",
                timeAgo = "1m ago",
                actionButtonText = "View Order",
                category = "sell"
            ),
            // Yesterday
            NotificationData(
                avatarRes = R.drawable.sprayer,
                title = "Transaction Successful! ₹3,500 credited for your sale of Green Chillies.",
                message = "",
                timeAgo = "1m ago",
                actionButtonText = "View Details",
                category = "sell"
            ), NotificationData(
                avatarRes = R.drawable.sprayer,
                title = "Reminder: Drone spraying service booked for Saturday, 3 PM.",
                message = "",
                timeAgo = "1m ago",
                actionButtonText = "View Details",
                category = "service"
            ), NotificationData(
                avatarRes = R.drawable.sprayer,
                title = "Service Feedback Request: Rate your recent service for Water Pump.",
                message = "",
                timeAgo = "1m ago",
                actionButtonText = "Drop Rating",
                category = "service"
            )
        )
    }

    // Filter notifications based on selected category
    val filteredNotifications = if (selectedFilter.equals("All",ignoreCase = true)) {
        allNotifications
    } else {
        allNotifications.filter { it.category == selectedFilter.lowercase() }
    }

    // Group by today and yesterday
    val todayNotifications = filteredNotifications.take(3)
    val yesterdayNotifications = filteredNotifications.drop(3)

    Scaffold(
        modifier = modifier, containerColor = Color.Transparent, topBar = {
            ChiragTopBar(
                navController = navController, icon = R.drawable.ic_arrow, title = "Notifications"
            )
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background illustration at bottom
            Image(
                painter = painterResource(id = R.drawable.notifications_screen_bg),
                contentDescription = "Background illustration",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxSize(),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Filter chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NotificationFilterChip(
                        label = "All",
                        isSelected = selectedFilter == "All",
                        onClick = { selectedFilter = "All" })
                    NotificationFilterChip(
                        label = "Buy",
                        isSelected = selectedFilter == "Buy",
                        onClick = { selectedFilter = "Buy" })
                    NotificationFilterChip(
                        label = "sell",
                        isSelected = selectedFilter == "sell",
                        onClick = { selectedFilter = "sell" })
                    NotificationFilterChip(
                        label = "service",
                        isSelected = selectedFilter == "service",
                        onClick = { selectedFilter = "service" })
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Today section
                if (todayNotifications.isNotEmpty()) {
                    Text(
                        text = "Today",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    todayNotifications.forEach { notification ->
                        NotificationItem(
                            avatarRes = notification.avatarRes,
                            title = notification.title,
                            message = notification.message,
                            timeAgo = notification.timeAgo,
                            actionButtonText = notification.actionButtonText,
                            onActionClick = { /* Handle action */ },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                // Yesterday section
                if (yesterdayNotifications.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Yesterday",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    yesterdayNotifications.forEach { notification ->
                        NotificationItem(
                            avatarRes = notification.avatarRes,
                            title = notification.title,
                            message = notification.message,
                            timeAgo = notification.timeAgo,
                            actionButtonText = notification.actionButtonText,
                            onActionClick = { /* Handle action */ },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}