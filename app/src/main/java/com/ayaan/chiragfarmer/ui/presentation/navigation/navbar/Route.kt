package com.ayaan.chiragfarmer.ui.presentation.navigation.navbar

sealed class Route(val path: String) {
    object Home : Route("home")
    object Assist : Route("assist")
    object Bookings : Route("bookings")
    object Buy: Route("buy")
    object Sell: Route("sell")
    object Auth : Route("auth")
    object OTPVerification : Route("otp/{phone}/{requestId}/{isSignUp}"){
        fun createRoute(phone: String, requestId: String, isSignUp: Boolean) = "otp/$phone/$requestId/$isSignUp"
    }
    object Register: Route("register")
    object RegisterSuccess: Route("register_success")
    object Search:Route("search")
    object SellProduct:Route("sell_product")
}