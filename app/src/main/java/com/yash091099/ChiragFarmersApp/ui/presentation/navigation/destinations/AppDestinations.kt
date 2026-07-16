package com.yash091099.ChiragFarmersApp.ui.presentation.navigation.destinations

import androidx.annotation.DrawableRes
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route

enum class AppDestinations(
    val labelRes: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unSelectedIcon: Int,
    val route: String
) {
    HOME(R.string.nav_home, R.drawable.ic_home_selected, R.drawable.ic_home_unselected, Route.Home.path),
    BUY(R.string.nav_buy, R.drawable.ic_buy_selected, R.drawable.ic_buy_unselected, Route.Buy.path),
    ASSIST(R.string.nav_assist, R.drawable.ic_assist_selected, R.drawable.ic_assist_unselected, Route.Assist.path),
    SELL(R.string.nav_sell, R.drawable.ic_sell_selected, R.drawable.ic_sell_unselected, Route.Sell.path),
    BOOKINGS(R.string.nav_bookings, R.drawable.ic_bookings_selected, R.drawable.ic_bookings_unselected, Route.Bookings.path),
}