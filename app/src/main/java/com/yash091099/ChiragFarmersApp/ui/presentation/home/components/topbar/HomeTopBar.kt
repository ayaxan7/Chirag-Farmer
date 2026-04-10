package com.yash091099.ChiragFarmersApp.ui.presentation.home.components.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.home.utils.TopBarCurvedShape
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .background(Color.Transparent),

    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(TopBarCurvedShape())
                .background(BGBlack)
        )

        // Content overlay
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.logo_with_title),
                        contentDescription = "Chirag Logo White",
                        colorFilter = ColorFilter.tint(BGWhite),
                        modifier = Modifier
                            .width(140.dp)
                            .height(38.dp)
                    )
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ), actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(0.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_notifications),
                            contentDescription = "Notifications",
                            colorFilter = ColorFilter.tint(BGWhite),
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                                .clickable{
                                    navController.navigate(
                                        Route.Notifications.path
                                    )
                                }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Image(
                            painter = painterResource(R.drawable.ic_cart),
                            contentDescription = "Cart",
                            colorFilter = ColorFilter.tint(BGWhite),
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Image(
                            painter = painterResource(R.drawable.profile_icon),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .width(32.dp)
                                .height(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                })

            // Search bar button
            SearchBarButton(
                onClick = {
                    // Navigate to search screen
                    navController.navigate(Route.Search.path)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
        }
    }
}