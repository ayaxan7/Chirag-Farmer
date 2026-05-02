package com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost

import android.net.Uri

sealed class Route(val path: String) {
    object Splash : Route("splash")
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
    object BuyCategory : Route("buy_category/{categoryName}?bannerImageResId={bannerImageResId}") {
        fun createRoute(categoryName: String, bannerImageResId: Int? = null): String {
            val params = Uri.encode(categoryName)
            return if (bannerImageResId != null) {
                "buy_category/$params?bannerImageResId=$bannerImageResId"
            } else {
                "buy_category/$params"
            }
        }
    }
    object AddressMap: Route("address_map")
    object AddressList:Route("address")
    object Notifications:Route("notifications")
    object SellCategories:Route("sell_categories")
    object AssistImage:Route("assist_image")
    object SellProduct:Route("sell_product?productId={productId}&selectedCategory={selectedCategory}") {
        fun createRoute(productId: String? = null, selectedCategory: String? = null): String {
            val queryParams = buildList {
                productId?.let { add("productId=$it") }
                selectedCategory?.let { add("selectedCategory=$it") }
            }

            return if (queryParams.isEmpty()) {
                "sell_product"
            } else {
                "sell_product?${queryParams.joinToString("&")}"
            }
        }
    }
     object ProductDetails:Route("productdetails/{productId}") {
         fun createRoute(productId: String): String = "productdetails/$productId"
     }
     object Cart: Route("cart?isBuyNow={isBuyNow}&productId={productId}&quantity={quantity}") {
         fun createRoute(isBuyNow: Boolean = false, productId: String? = null, quantity: Int = 1): String {
             return if (isBuyNow && productId != null) {
                 "cart?isBuyNow=true&productId=$productId&quantity=$quantity"
             } else {
                 "cart"
             }
         }
     }
     object Profile: Route("profile")
     object Payment: Route("payment")
     object OrderStatus: Route("order_status/{orderId}") {
         fun createRoute(orderId: String) = "order_status/$orderId"
     }
}