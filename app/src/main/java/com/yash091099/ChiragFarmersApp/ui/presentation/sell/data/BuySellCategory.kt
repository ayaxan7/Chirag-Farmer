package com.yash091099.ChiragFarmersApp.ui.presentation.sell.data

import com.yash091099.ChiragFarmersApp.R

data class BuySellCategory(
    val name: String,
    val image: Int,
    val id: Double,
    val apiValue: String? = null,
    val bannerImage: Int? = null,
    val subCategories: List<BuySellCategory> = emptyList()
)

object Categories {
    val sellCategories = listOf(
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

    val buyCategories = listOf(
        BuySellCategory(
            name = "Seeds",
            image = R.drawable.buy_category_seeds,
            id = 1.1,
            apiValue = "seeds",
            bannerImage = R.drawable.seeds_banner,
            subCategories = listOf(
                BuySellCategory("Hybrid Seeds", R.drawable.sell_category_cereals, 11.1, apiValue = "hybrid seeds"),
                BuySellCategory("Vegetable Seeds", R.drawable.sell_category_vegetables, 11.2, apiValue = "vegetable seeds"),
                BuySellCategory("Fruit Seeds", R.drawable.sell_category_fruits, 11.3, apiValue = "fruit seeds"),
                BuySellCategory("Flower Seeds", R.drawable.sell_category_flowers, 11.4, apiValue = "flower seeds"),
                BuySellCategory("Oilseed Seeds", R.drawable.sell_category_oilseeds, 11.5, apiValue = "oilseed seeds"),
                BuySellCategory("Pulse Seeds", R.drawable.sell_category_pulses, 11.6, apiValue = "pulse seeds"),
                BuySellCategory("Organic Seeds", R.drawable.sell_category_organicproduces, 11.7, apiValue = "organic seeds"),
                BuySellCategory("Fodder Seeds", R.drawable.sell_category_other, 11.8, apiValue = "fodder seeds")
            )
        ),
        BuySellCategory(
            name = "Sprayers",
            image = R.drawable.buy_category_sprayers,
            id = 1.2,
            apiValue = "sprayers",
            bannerImage = R.drawable.agri_sprayers_banner,
            subCategories = listOf(
                BuySellCategory("Manual Sprayers", R.drawable.sell_category_other, 12.1, apiValue = "manual sprayers"),
                BuySellCategory("Battery Sprayers", R.drawable.sell_category_other, 12.2, apiValue = "battery sprayers"),
                BuySellCategory("Knapsack Sprayers", R.drawable.sell_category_other, 12.3, apiValue = "knapsack sprayers"),
                BuySellCategory("Power Sprayers", R.drawable.sell_category_other, 12.4, apiValue = "power sprayers"),
                BuySellCategory("Boom Sprayers", R.drawable.sell_category_other, 12.5, apiValue = "boom sprayers"),
                BuySellCategory("Fogging Machines", R.drawable.sell_category_other, 12.6, apiValue = "fogging machines"),
                BuySellCategory("Herbicide Sprayers", R.drawable.sell_category_other, 12.7, apiValue = "herbicide sprayers"),
                BuySellCategory("Nozzle Accessories", R.drawable.sell_category_other, 12.8, apiValue = "nozzle accessories")
            )
        ),
        BuySellCategory(
            name = "Agriculture\nDrone",
            image = R.drawable.buy_category_agri_drone,
            id = 1.3,
            apiValue = "drones",
            bannerImage = R.drawable.agri_drones,
            subCategories = listOf(
                BuySellCategory("Spray Drones", R.drawable.sell_category_other, 13.1, apiValue = "spray drones"),
                BuySellCategory("Mapping Drones", R.drawable.sell_category_other, 13.2, apiValue = "mapping drones"),
                BuySellCategory("Surveillance Drones", R.drawable.sell_category_other, 13.3, apiValue = "surveillance drones"),
                BuySellCategory("Drone Batteries", R.drawable.sell_category_other, 13.4, apiValue = "drone batteries"),
                BuySellCategory("Drone Propellers", R.drawable.sell_category_other, 13.5, apiValue = "drone propellers"),
                BuySellCategory("Drone Cameras", R.drawable.sell_category_other, 13.6, apiValue = "drone cameras"),
                BuySellCategory("Drone Accessories", R.drawable.sell_category_other, 13.7, apiValue = "drone accessories"),
                BuySellCategory("Drone Service Kits", R.drawable.sell_category_other, 13.8, apiValue = "drone service kits")
            )
        ),
        BuySellCategory(
            name = "Tractors",
            image = R.drawable.buy_category_tractor,
            id = 1.4,
            apiValue = "tractors",
            bannerImage = R.drawable.tractors_banner,
            subCategories = listOf(
                BuySellCategory("Mini Tractors", R.drawable.sell_category_other, 14.1, apiValue = "mini tractors"),
                BuySellCategory("Utility Tractors", R.drawable.sell_category_other, 14.2, apiValue = "utility tractors"),
                BuySellCategory("Heavy Duty Tractors", R.drawable.sell_category_other, 14.3, apiValue = "heavy duty tractors"),
                BuySellCategory("Tractor Implements", R.drawable.sell_category_other, 14.4, apiValue = "tractor implements"),
                BuySellCategory("Tractor Tyres", R.drawable.sell_category_other, 14.5, apiValue = "tractor tyres"),
                BuySellCategory("Tractor Accessories", R.drawable.sell_category_other, 14.6, apiValue = "tractor accessories"),
                BuySellCategory("Tractor Oils", R.drawable.sell_category_other, 14.7, apiValue = "tractor oils"),
                BuySellCategory("Tractor Batteries", R.drawable.sell_category_other, 14.8, apiValue = "tractor batteries")
            )
        ),
        BuySellCategory(
            name = "Direct From\nFarmers",
            image = R.drawable.buy_category_direct_from_farmers,
            id = 1.5,
            apiValue = "direct-from-farmers",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory("Cereals", R.drawable.sell_category_cereals, 15.1, apiValue = "cereals"),
                BuySellCategory("Pulses", R.drawable.sell_category_pulses, 15.2, apiValue = "pulses"),
                BuySellCategory("Vegetables", R.drawable.sell_category_vegetables, 15.3, apiValue = "vegetables"),
                BuySellCategory("Fruits", R.drawable.sell_category_fruits, 15.4, apiValue = "fruits"),
                BuySellCategory("Oilseeds", R.drawable.sell_category_oilseeds, 15.5, apiValue = "oilseeds"),
                BuySellCategory("Spices", R.drawable.sell_category_spices, 15.6, apiValue = "spices"),
                BuySellCategory("Organic Produce", R.drawable.sell_category_organicproduces, 15.7, apiValue = "organic produce"),
                BuySellCategory("Livestock Products", R.drawable.sell_category_livestock, 15.8, apiValue = "livestock products")
            )
        )
    )

    private val defaultBuySubcategories = listOf(
        BuySellCategory("All Products", R.drawable.sell_category_other, 10.1),
        BuySellCategory("Seeds", R.drawable.buy_category_seeds, 10.2, apiValue = "seeds"),
        BuySellCategory("Sprayers", R.drawable.buy_category_sprayers, 10.3, apiValue = "sprayers"),
        BuySellCategory("Agricultural Drones", R.drawable.buy_category_agri_drone, 10.4, apiValue = "drones"),
        BuySellCategory("Tractors", R.drawable.buy_category_tractor, 10.5, apiValue = "tractors")
    )

    fun getBuyCategory(categoryName: String): BuySellCategory? {
        return buyCategories.firstOrNull { it.matches(categoryName) }
    }

    fun getBuyBannerImage(categoryName: String): Int {
        return getBuyCategory(categoryName)?.bannerImage ?: R.drawable.buy_banner
    }

    fun getBuyApiCategory(categoryName: String): String? {
        return getBuyCategory(categoryName)?.apiValue
    }

    fun getBuySubcategories(categoryName: String): List<BuySellCategory> {
        return getBuyCategory(categoryName)?.subCategories?.takeIf { it.isNotEmpty() }
            ?: defaultBuySubcategories
    }
}

private fun BuySellCategory.matches(categoryName: String): Boolean {
    return normalizeCategoryName(name) == normalizeCategoryName(categoryName)
}

private fun normalizeCategoryName(value: String): String {
    return value.replace("\n", " ").trim().lowercase()
}