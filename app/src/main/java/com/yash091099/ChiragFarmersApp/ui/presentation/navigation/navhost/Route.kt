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
    object AssistResult:Route("assist_result?imageUri={imageUri}") {
        fun createRoute(imageUri: String): String = "assist_result?imageUri=${Uri.encode(imageUri)}"
    }
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
     object Payment: Route("payment?subtotal={subtotal}&totalDiscount={totalDiscount}&totalDeliveryFee={totalDeliveryFee}&totalAmount={totalAmount}") {
         fun createRoute(subtotal: Double, totalDiscount: Double, totalDeliveryFee: Double, totalAmount: Double) = 
             "payment?subtotal=$subtotal&totalDiscount=$totalDiscount&totalDeliveryFee=$totalDeliveryFee&totalAmount=$totalAmount"
     }
    object PaymentSuccess: Route("payment_success")
    object MyOrders:Route("orders")
     object OrderDetails : Route("order_details/{orderId}?productId={productId}") {
         fun createRoute(orderId: String, productId: String? = null): String {
             return if (productId != null) {
                 "order_details/${Uri.encode(orderId)}?productId=${Uri.encode(productId)}"
             } else {
                 "order_details/${Uri.encode(orderId)}"
             }
         }
     }
    object SellerProfile : Route("seller_profile/{sellerId}?sellerName={sellerName}&sellerImage={sellerImage}") {
        fun createRoute(sellerId: String, sellerName: String? = null, sellerImage: String? = null): String {
            val base = "seller_profile/${Uri.encode(sellerId)}"
            val params = buildList {
                sellerName?.let { add("sellerName=${Uri.encode(it)}") }
                sellerImage?.let { add("sellerImage=${Uri.encode(it)}") }
            }
            return if (params.isEmpty()) base else "$base?${params.joinToString("&")}"
        }
    }
     object DropReview : Route("drop_review/{orderId}?productId={productId}&imageUrl={imageUrl}&productName={productName}&sellerName={sellerName}&pricePaid={pricePaid}") {
         fun createRoute(
             orderId: String,
             productId: String,
             imageUrl: String? = null,
             productName: String? = null,
             sellerName: String? = null,
             pricePaid: String? = null
         ): String {
             val base = "drop_review/${Uri.encode(orderId)}"
             val params = buildList {
                 add("productId=${Uri.encode(productId)}")
                 imageUrl?.let { add("imageUrl=${Uri.encode(it)}") }
                 productName?.let { add("productName=${Uri.encode(it)}") }
                 sellerName?.let { add("sellerName=${Uri.encode(it)}") }
                 pricePaid?.let { add("pricePaid=${Uri.encode(it)}") }
             }
             return if (params.isEmpty()) base else "$base?${params.joinToString("&")}"
         }
     }
     object SellerOrderDetails : Route("seller_order_details/{orderId}") {
         fun createRoute(orderId: String): String = "seller_order_details/${Uri.encode(orderId)}"
     }
}