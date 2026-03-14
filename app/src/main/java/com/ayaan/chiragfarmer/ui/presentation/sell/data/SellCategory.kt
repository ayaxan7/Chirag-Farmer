package com.ayaan.chiragfarmer.ui.presentation.sell.data

import com.ayaan.chiragfarmer.R

data class SellCategory(
    val name:String,
    val image: Int
)
object Categories{
    val sellCategories=listOf(
        SellCategory("Cereals &\nGrains", R.drawable.sell_category_cereals),
        SellCategory("Pulses &\nLegumes", R.drawable.sell_category_pulses),
        SellCategory("Fruits", R.drawable.sell_category_fruits),
        SellCategory("Vegetables", R.drawable.sell_category_vegetables),
        SellCategory("Oilseeds", R.drawable.sell_category_oilseeds),
        SellCategory("Spices", R.drawable.sell_category_spices),
        SellCategory("Tubers and\nRoot Crops", R.drawable.sell_category_tubers),
        SellCategory("Flowers", R.drawable.sell_category_flowers),
        SellCategory("Dry Fruits &\nNuts", R.drawable.sell_category_dryfruits),
        SellCategory("Organic Procedure", R.drawable.sell_category_organicproduces),
        SellCategory("Livestock Products", R.drawable.sell_category_livestock),
        SellCategory("Other", R.drawable.sell_category_other)
    )
}