package com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.auth.common.screens.AuthScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.auth.common.screens.AuthViewModel
import com.yash091099.ChiragFarmersApp.ui.presentation.auth.common.screens.OTPVerificationScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.auth.common.screens.OTPViewModel
import com.yash091099.ChiragFarmersApp.ui.presentation.auth.register.RegisterScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.auth.register.RegisterSuccessScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.auth.register.RegisterViewModel
import com.yash091099.ChiragFarmersApp.ui.presentation.home.HomeScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.home.HomeViewModel
import com.yash091099.ChiragFarmersApp.ui.presentation.assist.AssistScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.assist.screens.assistImage.AssistImage
import com.yash091099.ChiragFarmersApp.ui.presentation.bookings.BookingsScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.buy.BuyScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.categories.CategoriesScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.details.ProductDetailsScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.details.ProductDetailsViewModel
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.CartScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.CartViewModel
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.address.AddressMapScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.address.AddressScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.address.AddressListViewModel
import com.yash091099.ChiragFarmersApp.ui.presentation.home.screens.SearchScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.home.screens.notifications.NotificationsScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.SellScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.screens.sellcategories.SellCategoriesScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.screens.sellproduces.SellProducesScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment.PaymentScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment.PaymentSuccess
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment.PaymentViewModel
import com.yash091099.ChiragFarmersApp.ui.presentation.splash.SplashScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.profile.ProfileScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.profile.ProfileViewModel
import com.yash091099.ChiragFarmersApp.ui.presentation.orders.MyOrdersScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.orders.MyOrderDetailsScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.seller.SellerProfileScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // Splash screen as initial destination - authentication logic is handled there
    NavHost(
        modifier = modifier, startDestination = Route.Splash.path, navController = navController
    ) {
        composable(Route.Splash.path) {
            SplashScreen(navController = navController)
        }
        composable(Route.Auth.path) {
            val viewModel: AuthViewModel = hiltViewModel()
            AuthScreen(
                navController = navController, modifier = modifier, viewModel = viewModel
            )
        }
        composable(
            Route.OTPVerification.path,
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType },
                navArgument("requestId") { type = NavType.StringType },
                navArgument("isSignUp") { type = NavType.BoolType })) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phone") ?: ""
            val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
            val isSignUp = backStackEntry.arguments?.getBoolean("isSignUp") ?: false
            val viewModel: OTPViewModel = hiltViewModel()
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
        composable(Route.Home.path) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(Route.Cart.path, arguments = listOf(
            navArgument("isBuyNow") {
                type = NavType.BoolType
                defaultValue = false
            },
            navArgument("productId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument("quantity") {
                type = NavType.IntType
                defaultValue = 1
            }
        )) {
            val viewModel: CartViewModel = hiltViewModel()
            val isBuyNow = it.arguments?.getBoolean("isBuyNow") ?: false
            val productId = it.arguments?.getString("productId")
            val quantity = it.arguments?.getInt("quantity") ?: 1

            CartScreen(navController = navController, viewModel = viewModel, isBuyNow = isBuyNow, productId = productId, quantity = quantity)
        }
        composable(Route.Search.path) {
            SearchScreen(navController = navController)
        }
        composable(Route.SellCategories.path) {
            SellCategoriesScreen(navController = navController)
        }
        composable(Route.AddressList.path){
            val viewModel: AddressListViewModel = hiltViewModel()
            AddressScreen(
                navController = navController,
                viewModel = viewModel,
                onAddAddressClick = {
                navController.navigate(Route.AddressMap.path){
                    popUpTo(Route.AddressMap.path){
                        inclusive=true
                    }
                }
            })
        }
        composable(Route.Assist.path) {
            AssistScreen(navController = navController)
        }
        composable(Route.Notifications.path) {
            NotificationsScreen(navController = navController)
        }
        composable(Route.Bookings.path) {
            BookingsScreen(navController = navController)
        }
        composable(Route.Buy.path) {
            BuyScreen(navController = navController)
        }
        composable(
            route = Route.BuyCategory.path,
            arguments = listOf(
                navArgument("categoryName") { type = NavType.StringType },
                navArgument("bannerImageResId") {
                    type = NavType.IntType
                    defaultValue = R.drawable.buy_banner
                })) { backStackEntry ->
            val categoryName = Uri.decode(backStackEntry.arguments?.getString("categoryName") ?: "")
            val bannerImageResId =
                backStackEntry.arguments?.getInt("bannerImageResId") ?: R.drawable.buy_banner
            CategoriesScreen(
                navController = navController,
                categoryName = categoryName,
                bannerImageRes = bannerImageResId
            )
        }
        composable(Route.AssistImage.path) {
            AssistImage(
                navController = navController
            )
        }
        composable(Route.Sell.path) {
            SellScreen(navController = navController)
        }
        composable(Route.SellProduct.path, arguments = listOf(navArgument("productId") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }, navArgument("selectedCategory") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            val selectedCategory = backStackEntry.arguments?.getString("selectedCategory")
            SellProducesScreen(
                navController = navController,
                productId = productId,
                selectedCategory = selectedCategory,
                viewModel = hiltViewModel()
            )
        }
        composable(Route.AddressMap.path){
            AddressMapScreen(navController=navController)
        }
        composable(
            Route.ProductDetails.path, arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
        ) {
            val viewModel: ProductDetailsViewModel = hiltViewModel()
            ProductDetailsScreen(
                navController = navController, viewModel = viewModel
            )
        }
        composable(
            Route.Payment.path,
            arguments = listOf(
                navArgument("subtotal") { type = NavType.FloatType; defaultValue = 0.0f },
                navArgument("totalDiscount") { type = NavType.FloatType; defaultValue = 0.0f },
                navArgument("totalDeliveryFee") { type = NavType.FloatType; defaultValue = 0.0f },
                navArgument("totalAmount") { type = NavType.FloatType; defaultValue = 0.0f }
            )
        ) { backStackEntry ->
            val subtotal = backStackEntry.arguments?.getFloat("subtotal")?.toDouble() ?: 0.0
            val totalDiscount = backStackEntry.arguments?.getFloat("totalDiscount")?.toDouble() ?: 0.0
            val totalDeliveryFee = backStackEntry.arguments?.getFloat("totalDeliveryFee")?.toDouble() ?: 0.0
            val totalAmount = backStackEntry.arguments?.getFloat("totalAmount")?.toDouble() ?: 0.0
            val viewModel: PaymentViewModel = hiltViewModel()

            PaymentScreen(
                navController = navController,
                viewModel = viewModel,
                subtotal = subtotal,
                totalDiscount = totalDiscount,
                totalDeliveryFee = totalDeliveryFee,
                totalAmount = totalAmount
            )
        }
        composable(Route.PaymentSuccess.path){
            PaymentSuccess(navController = navController)
        }
        composable(Route.Profile.path) {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(navController = navController, viewModel = viewModel)
        }
        composable(Route.MyOrders.path) {
            MyOrdersScreen(navController = navController)
        }
        composable(
            Route.OrderDetails.path,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = Uri.decode(backStackEntry.arguments?.getString("orderId") ?: "")
            MyOrderDetailsScreen(navController = navController, orderId = orderId)
        }
        composable(
            Route.SellerProfile.path,
            arguments = listOf(
                navArgument("sellerId") { type = NavType.StringType },
                navArgument("sellerName") { type = NavType.StringType; nullable = true; defaultValue = null },
                navArgument("sellerImage") { type = NavType.StringType; nullable = true; defaultValue = null }
            )
        ) { backStackEntry ->
            val sellerId = backStackEntry.arguments?.getString("sellerId") ?: ""
            val sellerName = backStackEntry.arguments?.getString("sellerName") ?: "Seller"
            val sellerImage = backStackEntry.arguments?.getString("sellerImage")
            SellerProfileScreen(
                navController = navController,
                sellerId = sellerId,
                sellerName = sellerName,
                sellerImage = sellerImage
            )
        }
    }
}
