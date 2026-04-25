package com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.destinations.AppDestinations
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.AppNavigation
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite

@Composable
fun ChiragFarmerApp(navController: NavHostController, modifier: Modifier = Modifier) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarState = rememberNavigationSuiteScaffoldState()

    // Define routes that should show the bottom navigation bar
    val routesWithBottomBar = listOf(
        Route.Home.path, Route.Assist.path, Route.Bookings.path, Route.Buy.path, Route.Sell.path
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
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
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
}