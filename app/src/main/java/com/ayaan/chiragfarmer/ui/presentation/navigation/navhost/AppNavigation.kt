package com.ayaan.chiragfarmer.ui.presentation.navigation.navhost

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ayaan.chiragfarmer.data.local.AuthDataStore
import com.ayaan.chiragfarmer.ui.presentation.auth.login.LoginScreen
import com.ayaan.chiragfarmer.ui.presentation.auth.login.OTPVerificationScreen
import com.ayaan.chiragfarmer.ui.presentation.home.HomeScreen
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
        Route.Login.path
    }

    NavHost(
        modifier = modifier,
        startDestination = startDestination,
        navController = navController
    ) {
        composable(Route.Login.path) {
            LoginScreen(
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
        composable(Route.Home.path){
            HomeScreen(navController = navController)
        }
    }
}
