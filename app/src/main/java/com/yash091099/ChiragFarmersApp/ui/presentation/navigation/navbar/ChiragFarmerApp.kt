package com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.destinations.AppDestinations
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.AppNavigation
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite

@Composable
fun ChiragFarmerApp(navController: NavHostController, deepLinkFlow: SharedFlow<Intent>? = null, modifier: Modifier = Modifier) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarState = rememberNavigationSuiteScaffoldState()

    // Define routes that should show the bottom navigation bar
    val routesWithBottomBar = listOf(
        Route.Home.path, Route.Assist.path, Route.Bookings.path, Route.Buy.path, Route.Sell.path,Route.SellerOrderDetails.path
    )

    // Splash, Auth, and other unauthenticated screens should NOT show bottom bar
    val showBottomBar = currentRoute in routesWithBottomBar

    LaunchedEffect(showBottomBar) {
        if (!showBottomBar) {
            bottomBarState.hide()
        } else {
            bottomBarState.show()
        }
    }
    val bottomItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
//            selectedIconColor = Color.Black,
//            selectedTextColor = Color.Black,
//            unselectedIconColor = Color.Gray,
//            unselectedTextColor = Color.Gray,
            indicatorColor = Color.Transparent
        )
    )
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            painter = if (currentRoute == destination.route) painterResource(id = destination.selectedIcon)
                            else painterResource(id = destination.unSelectedIcon),
                            contentDescription = stringResource(destination.labelRes)
                        )
                    },
                    label = { Text(stringResource(destination.labelRes)) },
                    selected = currentRoute == destination.route,
                    onClick = {
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = bottomItemColors
                )
            }
        },
        containerColor = BGWhite,
        state = bottomBarState,
        contentColor = BGWhite,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = BGWhite, navigationBarContentColor = BGWhite
        )
    ) {
        Surface(
            color = BGWhite
        ) {
            AppNavigation(navController = navController, modifier = modifier)
        }
    }

    // Collect deep link intents from Activity and navigate accordingly.
    LaunchedEffect(deepLinkFlow) {
        deepLinkFlow?.collectLatest { intent ->
            val uri = intent.data
            if (uri == null) return@collectLatest

            // Extract type and id robustly from incoming URI
            val segments = uri.pathSegments
            val type: String?
            val id: String?

            if (segments.size >= 2) {
                // Common patterns: /share/{type}/{id} -> segments[0]="share", [1]=type, [2]=id
                // Or /{type}/{id} -> segments[segments.size-2]=type, last=id
                if (segments.size >= 3 && segments[0].equals("share", ignoreCase = true)) {
                    type = segments.getOrNull(1)
                    id = segments.getOrNull(2)
                } else {
                    type = segments.getOrNull(segments.size - 2)
                    id = segments.getOrNull(segments.size - 1)
                }
            } else {
                type = null
                id = null
            }

            if (type == null || id == null) return@collectLatest

            // Normalize type
            val normalizedType = type.lowercase()

            // Ensure Home is present under the deep link so pressing back returns to Home
            try {
                if (navController.currentDestination?.route != Route.Home.path) {
                    navController.navigate(Route.Home.path) {
                        launchSingleTop = true
                    }
                }

                when (normalizedType) {
                    "product" -> {
                        navController.navigate(Route.ProductDetails.createRoute(id)) {
                            launchSingleTop = true
                        }
                    }
                    "farmer", "seller" -> {
                        navController.navigate(Route.SellerProfile.createRoute(id)) {
                            launchSingleTop = true
                        }
                    }
                    else -> {
                        // Unknown type: no-op
                    }
                }
            } catch (e: Exception) {
                // Swallow exceptions to avoid crashes when deep linking; optionally log
            }
        }
    }
}