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
import com.ayaan.chiragfarmer.ui.presentation.auth.common.screens.OTPVerificationScreen
import com.ayaan.chiragfarmer.ui.presentation.auth.register.RegisterScreen
import com.ayaan.chiragfarmer.ui.presentation.auth.register.RegisterSuccessScreen
import com.ayaan.chiragfarmer.ui.presentation.home.HomeScreen
import com.ayaan.chiragfarmer.ui.presentation.home.HomeViewModel
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.Route

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
            AuthScreen(
                navController = navController, modifier = modifier
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
            OTPVerificationScreen(
                navController = navController,
                phoneNumber = phoneNumber,
                requestId = requestId,
                isSignUp = isSignUp,
                modifier = modifier
            )
        }
        composable(Route.Register.path) {
            RegisterScreen(navController = navController)
        }
        composable(Route.RegisterSuccess.path) {
            RegisterSuccessScreen(navController = navController)
        }
        composable(Route.Home.path){
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(navController = navController, viewModel = viewModel)
        }
    }
}
