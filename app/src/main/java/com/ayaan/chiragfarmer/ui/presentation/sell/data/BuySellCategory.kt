package com.ayaan.chiragfarmer.ui.presentation.sell.data

import com.ayaan.chiragfarmer.R

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
        BuySellCategory("Organic Procedure", R.drawable.sell_category_organicproduces, 0.10),
        BuySellCategory("Livestock Products", R.drawable.sell_category_livestock, 0.11),
        BuySellCategory("Other", R.drawable.sell_category_other, 0.12)
    )
    val BuyCategories = mutableListOf(
        BuySellCategory("Seeds", R.drawable.sell_category_cereals, 1.1),
        BuySellCategory("Seeds", R.drawable.sell_category_cereals, 1.2),
        BuySellCategory("Seeds", R.drawable.sell_category_cereals, 1.3),
        BuySellCategory("Seeds", R.drawable.sell_category_cereals, 1.4),
        BuySellCategory("Seeds", R.drawable.sell_category_cereals, 1.5),
        BuySellCategory("Seeds", R.drawable.sell_category_cereals, 1.6),
    )
}