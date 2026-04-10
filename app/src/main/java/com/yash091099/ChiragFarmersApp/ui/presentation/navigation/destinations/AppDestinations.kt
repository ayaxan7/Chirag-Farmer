package com.yash091099.ChiragFarmersApp.ui.presentation.navigation.destinations

import androidx.annotation.DrawableRes
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route

enum class AppDestinations(
    val label: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unSelectedIcon: Int,
    val route: String
) {
    HOME("Home", R.drawable.ic_home_selected, R.drawable.ic_home_unselected, Route.Home.path),
    ASSIST("Assist", R.drawable.ic_assist_selected, R.drawable.ic_assist_unselected, Route.Assist.path),
    BOOKINGS("Bookings", R.drawable.ic_bookings_selected, R.drawable.ic_bookings_unselected, Route.Bookings.path),
    BUY("Buy", R.drawable.ic_buy_selected, R.drawable.ic_buy_unselected, Route.Buy.path),
    SELL("Sell", R.drawable.ic_sell_selected, R.drawable.ic_sell_unselected, Route.Sell.path)
}