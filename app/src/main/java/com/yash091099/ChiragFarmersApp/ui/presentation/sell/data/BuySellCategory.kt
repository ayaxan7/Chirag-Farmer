package com.yash091099.ChiragFarmersApp.ui.presentation.sell.data

import com.yash091099.ChiragFarmersApp.R

data class BuySellCategory(
    val name: String, val image: Int, val id: Double
)

object Categories {
    val sellCategories = mutableListOf(
        BuySellCategory("Cereals &\nGrains", R.drawable.sell_category_cereals, 0.1),
        BuySellCategory("Pulses &\nLegumes", R.drawable.sell_category_pulses, 0.2),
        BuySellCategory("Fruits", R.drawable.sell_category_fruits, 0.3),
        BuySellCategory("Vegetables", R.drawable.sell_category_vegetables, 0.4),
        BuySellCategory("Oilseeds", R.drawable.sell_category_oilseeds, 0.5),
        BuySellCategory("Spices", R.drawable.sell_category_spices, 0.6),
        BuySellCategory("Tubers and\nRoot Crops", R.drawable.sell_category_tubers, 0.7),
        BuySellCategory("Flowers", R.drawable.sell_category_flowers, 0.8),
        BuySellCategory("Dry Fruits &\nNuts", R.drawable.sell_category_dryfruits, 0.9),
        BuySellCategory("Organic\nProcedure", R.drawable.sell_category_organicproduces, 0.10),
        BuySellCategory("Livestock\nProducts", R.drawable.sell_category_livestock, 0.11),
        BuySellCategory("Other", R.drawable.sell_category_other, 0.12)
    )
    val BuyCategories = mutableListOf(
        BuySellCategory("Seeds", R.drawable.buy_category_seeds, 1.1),
        BuySellCategory("Sprayers", R.drawable.buy_category_sprayers, 1.2),
        BuySellCategory("Agriculture\nDrone", R.drawable.buy_category_agri_drone, 1.3),
        BuySellCategory("Tractors", R.drawable.buy_category_tractor, 1.4),
        BuySellCategory("Direct From\nFarmers", R.drawable.buy_category_direct_from_farmers, 1.5),
//        BuySellCategory("Seeds", R.drawable.sell_category_cereals, 1.6),
    )
}