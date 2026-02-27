package com.ayaan.chiragfarmer.ui.presentation.navigation.destinations

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.ayaan.chiragfarmer.R

enum class AppDestinations(
    val label: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unSelectedIcon: Int,
) {
    HOME("Home", R.drawable.ic_home_selected, R.drawable.ic_home_unselected),
    ASSIST("Assist", R.drawable.ic_assist_selected,R.drawable.ic_assist_unselected),
    BOOKINGS("Bookings", R.drawable.ic_bookings_selected,R.drawable.ic_bookings_unselected),
    BUY("Buy", R.drawable.ic_buy_selected,R.drawable.ic_buy_selected),
    SELL("Sell", R.drawable.ic_sell_selected,R.drawable.ic_sell_unselected)
}