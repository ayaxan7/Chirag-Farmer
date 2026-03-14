package com.ayaan.chiragfarmer.ui.presentation.navigation.navhost

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ayaan.chiragfarmer.data.local.AuthDataStore
import com.ayaan.chiragfarmer.ui.presentation.auth.common.screens.AuthScreen
import com.ayaan.chiragfarmer.ui.presentation.auth.common.screens.AuthViewModel
import com.ayaan.chiragfarmer.ui.presentation.auth.common.screens.OTPVerificationScreen
import com.ayaan.chiragfarmer.ui.presentation.auth.common.screens.OTPViewModel
import com.ayaan.chiragfarmer.ui.presentation.auth.register.RegisterScreen
import com.ayaan.chiragfarmer.ui.presentation.auth.register.RegisterSuccessScreen
import com.ayaan.chiragfarmer.ui.presentation.auth.register.RegisterViewModel
import com.ayaan.chiragfarmer.ui.presentation.home.HomeScreen
import com.ayaan.chiragfarmer.ui.presentation.home.HomeViewModel
import com.ayaan.chiragfarmer.ui.presentation.assist.AssistScreen
import com.ayaan.chiragfarmer.ui.presentation.bookings.BookingsScreen
import com.ayaan.chiragfarmer.ui.presentation.buy.BuyScreen
import com.ayaan.chiragfarmer.ui.presentation.home.screens.SearchScreen
import com.ayaan.chiragfarmer.ui.presentation.sell.SellScreen
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.Route
import com.ayaan.chiragfarmer.ui.presentation.sell.screens.sellcategories.SellCategoriesScreen
import com.ayaan.chiragfarmer.ui.presentation.sell.screens.sellproduces.SellProducesScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val dataStore = AuthDataStore(context = context)

    // Collect auth token from DataStore
    val authToken by dataStore.getAuthToken().collectAsState(initial = null)

    // Determine start destination based on auth token
    val startDestination = if (authToken != null && authToken!!.isNotEmpty()) {
        Route.Home.path
    } else {
        Route.Auth.path
    }

    NavHost(
        modifier = modifier,
        startDestination = startDestination,
        navController = navController
    ) {
        composable(Route.Auth.path) {
            val viewModel: AuthViewModel=hiltViewModel()
            AuthScreen(
                navController = navController, modifier = modifier, viewModel = viewModel
            )
        }
        composable(
            Route.OTPVerification.path,
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType },
                navArgument("requestId") { type = NavType.StringType },
                navArgument("isSignUp") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phone") ?: ""
            val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
            val isSignUp = backStackEntry.arguments?.getBoolean("isSignUp") ?: false
            val viewModel: OTPViewModel=hiltViewModel()
            OTPVerificationScreen(
                navController = navController,
                phoneNumber = phoneNumber,
                requestId = requestId,
                isSignUp = isSignUp,
                modifier = modifier,
                viewModel = viewModel
            )
        }
        composable(Route.Register.path) {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(navController = navController, viewModel = viewModel)
        }
        composable(Route.RegisterSuccess.path) {
            RegisterSuccessScreen(navController = navController)
        }
        composable(Route.Home.path){
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(Route.Search.path) {
            SearchScreen(navController = navController)
        }
        composable(Route.SellCategories.path) {
            SellCategoriesScreen(navController = navController)
        }
        composable(Route.Assist.path) {
            AssistScreen(navController = navController)
        }
        composable(Route.Bookings.path) {
            BookingsScreen(navController = navController)
        }
        composable(Route.Buy.path) {
            BuyScreen(navController = navController)
        }
        composable(Route.Sell.path) {
            SellScreen(navController = navController)
        }
        composable(
            Route.SellProduct.path,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            SellProducesScreen(
                navController = navController,
                productId = productId,
                viewModel = hiltViewModel()
            )
        }
    }
}
