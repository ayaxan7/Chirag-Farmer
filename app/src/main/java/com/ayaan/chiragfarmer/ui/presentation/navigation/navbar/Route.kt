package com.ayaan.chiragfarmer.ui.presentation.navigation.navbar

sealed class Route(val path: String) {
    object Home : Route("home")
    object Favorites : Route("favorites")
    object Profile : Route("profile")
    object Login : Route("login")
    object OTPVerification : Route("otp/{phone}/{requestId}/{isSignUp}"){
        fun createRoute(phone: String, requestId: String, isSignUp: Boolean) = "otp/$phone/$requestId/$isSignUp"
    }
}