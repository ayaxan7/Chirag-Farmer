package com.ayaan.chiragfarmer.ui.presentation.navigation.navhost

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.ayaan.chiragfarmer.ui.presentation.auth.login.LoginScreen
import com.ayaan.chiragfarmer.ui.presentation.auth.login.OTPVerificationScreen
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.Route

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier, startDestination = Route.OTPVerification.path, navController = navController
    ) {
        composable(Route.Login.path) {
            LoginScreen(
                navController = navController,
                modifier = modifier
            )
        }
        composable(
            Route.OTPVerification.path,
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phone") ?: ""
            OTPVerificationScreen(
                navController = navController,
                phoneNumber = phoneNumber,
                modifier = modifier
            )
        }
    }
}
