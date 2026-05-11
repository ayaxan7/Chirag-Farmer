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
                BuySellCategory(
                    "Paddy Seeds",
                    R.drawable.paddy_seeds,
                    11.1,
                    apiValue = "paddy seeds"
                ),
                BuySellCategory(
                    "Fruits Seeds",
                    R.drawable.fruits_seeds,
                    11.2,
                    apiValue = "fruits seeds"
                ),
                BuySellCategory(
                    "Vegetables Seeds",
                    R.drawable.veg_seeds,
                    11.3,
                    apiValue = "vegetable seeds"
                ),
                BuySellCategory(
                    "Leafy Seeds",
                    R.drawable.leafy_seeds,
                    11.4,
                    apiValue = "leafy seeds"
                ),
                BuySellCategory("Herb Seeds", R.drawable.herb_seeds, 11.5, apiValue = "herb seeds"),
            )
        ), BuySellCategory(
            name = "Sprayers",
            image = R.drawable.buy_category_sprayers,
            id = 1.2,
            apiValue = "sprayers",
            bannerImage = R.drawable.agri_sprayers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Manual Hand Sprayers",
                    R.drawable.sell_category_other,
                    12.1,
                    apiValue = "manual sprayers"
                ),
                BuySellCategory(
                    "Battery Sprayers",
                    R.drawable.sell_category_other,
                    12.2,
                    apiValue = "battery sprayers"
                ),
                BuySellCategory(
                    "Knapsack Sprayers",
                    R.drawable.sell_category_other,
                    12.3,
                    apiValue = "knapsack sprayers"
                ),
                BuySellCategory(
                    "Power Sprayers",
                    R.drawable.sell_category_other,
                    12.4,
                    apiValue = "power sprayers"
                ),
                BuySellCategory(
                    "HTP Sprayers",
                    R.drawable.sell_category_other,
                    12.5,
                    apiValue = "htp sprayers"
                ),
            )
        ), BuySellCategory(
            name = "Agriculture\nDrone",
            image = R.drawable.buy_category_agri_drone,
            id = 1.3,
            apiValue = "drones",
            bannerImage = R.drawable.agri_drones,
            subCategories = listOf(
                BuySellCategory(
                    "Spraying Drones",
                    R.drawable.sell_category_other,
                    13.1,
                    apiValue = "spray drones"
                ),
                BuySellCategory(
                    "Surveillance/\nMonitoring Drones",
                    R.drawable.sell_category_other,
                    13.3,
                    apiValue = "surveillance drones"
                ),
                BuySellCategory(
                    "Mulitspectral & Mapping",
                    R.drawable.sell_category_other,
                    13.4,
                    apiValue = "drone batteries"
                ),
                BuySellCategory(
                    "Drone Propellers",
                    R.drawable.sell_category_other,
                    13.5,
                    apiValue = "drone propellers"
                ),
                BuySellCategory(
                    "Drone Cameras",
                    R.drawable.sell_category_other,
                    13.6,
                    apiValue = "drone cameras"
                ),
                BuySellCategory(
                    "Drone Accessories",
                    R.drawable.sell_category_other,
                    13.7,
                    apiValue = "drone accessories"
                ),
                BuySellCategory(
                    "Drone Service Kits",
                    R.drawable.sell_category_other,
                    13.8,
                    apiValue = "drone service kits"
                )
            )
        ), BuySellCategory(
            name = "Tractors",
            image = R.drawable.buy_category_tractor,
            id = 1.4,
            apiValue = "tractors",
            bannerImage = R.drawable.tractors_banner,
            subCategories = listOf(
                BuySellCategory(
                    "4WD Tractors",
                    R.drawable.sell_category_other,
                    14.1,
                    apiValue = "4WD tractors"
                ),
                BuySellCategory(
                    "Row Crop Tractors",
                    R.drawable.sell_category_other,
                    14.2,
                    apiValue = "row crop tractors"
                ),
                BuySellCategory(
                    "Mini Tractors",
                    R.drawable.sell_category_other,
                    14.3,
                    apiValue = "mini tractors"
                ),
                BuySellCategory(
                    "Rotavator Attachments",
                    R.drawable.sell_category_other,
                    14.4,
                    apiValue = "rotavator attachments"
                ),
                BuySellCategory(
                    "Utility Tractor",
                    R.drawable.sell_category_other,
                    14.5,
                    apiValue = "utility tractor"
                ),
            )
        ), BuySellCategory(
            name = "Direct From\nFarmers",
            image = R.drawable.buy_category_direct_from_farmers,
            id = 1.5,
            apiValue = "direct-from-farmers",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Cereals",
                    R.drawable.sell_category_cereals,
                    15.1,
                    apiValue = "cereals"
                ),
                BuySellCategory(
                    "Pulses",
                    R.drawable.sell_category_pulses,
                    15.2,
                    apiValue = "pulses"
                ),
                BuySellCategory(
                    "Vegetables",
                    R.drawable.sell_category_vegetables,
                    15.3,
                    apiValue = "vegetables"
                ),
                BuySellCategory(
                    "Fruits",
                    R.drawable.sell_category_fruits,
                    15.4,
                    apiValue = "fruits"
                ),
                BuySellCategory(
                    "Oilseeds",
                    R.drawable.sell_category_oilseeds,
                    15.5,
                    apiValue = "oilseeds"
                ),
                BuySellCategory(
                    "Spices",
                    R.drawable.sell_category_spices,
                    15.6,
                    apiValue = "spices"
                ),
                BuySellCategory(
                    "Organic Produce",
                    R.drawable.sell_category_organicproduces,
                    15.7,
                    apiValue = "organic produce"
                ),
                BuySellCategory(
                    "Livestock Products",
                    R.drawable.sell_category_livestock,
                    15.8,
                    apiValue = "livestock products"
                )
            )
        ), BuySellCategory(
            name = "Pesticides",
            image = R.drawable.buy_category_direct_from_farmers,
            id = 1.5,
            apiValue = "pesticides",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Insecticides",
                    R.drawable.sell_category_cereals,
                    15.1,
                    apiValue = "insecticides"
                ),
                BuySellCategory(
                    "Fungicides",
                    R.drawable.sell_category_pulses,
                    15.2,
                    apiValue = "fungicides"
                ),
                BuySellCategory(
                    "Herbicides\n(Weedicides)",
                    R.drawable.sell_category_vegetables,
                    15.3,
                    apiValue = "herbicides"
                ),
                BuySellCategory(
                    "Bio-Pesticides",
                    R.drawable.sell_category_fruits,
                    15.4,
                    apiValue = "bio-pesticides"
                ),
            )
        ), BuySellCategory(
            name = "Harvesting Machines",
            image = R.drawable.harvesting_machines,
            id = 1.5,
            apiValue = "harvesting machines",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Combine Harvester",
                    R.drawable.sell_category_cereals,
                    15.1,
                    apiValue = "combine harvester"
                ),
                BuySellCategory(
                    "Reaper",
                    R.drawable.sell_category_pulses,
                    15.2,
                    apiValue = "reaper"
                ),
                BuySellCategory(
                    "Paddy\nHarvester",
                    R.drawable.sell_category_vegetables,
                    15.3,
                    apiValue = "paddy harvester"
                ),
                BuySellCategory(
                    "Sugarcane Harvesters",
                    R.drawable.sell_category_fruits,
                    15.4,
                    apiValue = "sugarcane harvester"
                ),
                BuySellCategory(
                    "Groundnut Harvesters",
                    R.drawable.sell_category_fruits,
                    15.4,
                    apiValue = "groundnut harvester"
                )
            )
        ), BuySellCategory(
            name = "Soil Testers",
            image = R.drawable.harvesting_machines,
            id = 1.5,
            apiValue = "soil testers",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Soil PH Meter",
                    R.drawable.sell_category_cereals,
                    15.1,
                    apiValue = "soil ph meter"
                ),
                BuySellCategory(
                    "Soil Moisture Meter",
                    R.drawable.sell_category_pulses,
                    15.2,
                    apiValue = "soil moisture meter"
                ),
                BuySellCategory(
                    "NPK Soil Testing Kit",
                    R.drawable.sell_category_vegetables,
                    15.3,
                    apiValue = "npk soil testing kit"
                ),
                BuySellCategory(
                    "Digital Soil Testing Device",
                    R.drawable.sell_category_fruits,
                    15.4,
                    apiValue = "digital soil testing device"
                )
            )
        ), BuySellCategory(
            name = "Power Weeders",
            image = R.drawable.harvesting_machines,
            id = 1.5,
            apiValue = "power weeders",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Mini Power Weeders",
                    R.drawable.sell_category_cereals,
                    15.1,
                    apiValue = "mini power weeders"
                ),
                BuySellCategory(
                    "Petrol Power Weeders",
                    R.drawable.sell_category_pulses,
                    15.2,
                    apiValue = "petrol power weeders"
                ),
                BuySellCategory(
                    "Diesel Power Weeders",
                    R.drawable.sell_category_vegetables,
                    15.3,
                    apiValue = "diesel power weeders"
                ),
                BuySellCategory(
                    "Self-Propelled Weeders",
                    R.drawable.sell_category_fruits,
                    15.4,
                    apiValue = "self-propelled weeders"
                )
            )
        ), BuySellCategory(
            name = "Thresher Winnowers",
            image = R.drawable.harvesting_machines,
            id = 1.5,
            apiValue = "thresher winnower",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Wheat Thresher",
                    R.drawable.sell_category_cereals,
                    15.1,
                    apiValue = "wheat thresher"
                ),
                BuySellCategory(
                    "Multi-Crop\nThresher",
                    R.drawable.sell_category_pulses,
                    15.2,
                    apiValue = "multi-crop thresher"
                ),
                BuySellCategory(
                    "Paddy\nThresher",
                    R.drawable.sell_category_vegetables,
                    15.3,
                    apiValue = "paddy harvester"
                ),
                BuySellCategory(
                    "Motorized Winnower",
                    R.drawable.sell_category_fruits,
                    15.4,
                    apiValue = "motorized winnower"
                )
            )
        ), BuySellCategory(
            name = "Irrigation Tools",
            image = R.drawable.harvesting_machines,
            id = 1.5,
            apiValue = "irrigation tools",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "PVC Pipes\n& fittings",
                    R.drawable.sell_category_cereals,
                    15.1,
                    apiValue = "pvc pipes and fittings"
                ),
                BuySellCategory(
                    "Submersible\nPumps",
                    R.drawable.sell_category_pulses,
                    15.2,
                    apiValue = "submersible pumps"
                ),
                BuySellCategory(
                    "Rain Guns",
                    R.drawable.sell_category_vegetables,
                    15.3,
                    apiValue = "rain guns"
                ),
                BuySellCategory(
                    "Hose Pipes",
                    R.drawable.sell_category_fruits,
                    15.4,
                    apiValue = "hose pipes"
                ),
                BuySellCategory(
                    "Diesel/Electric Water Pumps",
                    R.drawable.sell_category_fruits,
                    15.4,
                    apiValue = "diesel electric water pumps"
                ),
                BuySellCategory(
                    "Timers &\nControllers",
                    R.drawable.sell_category_fruits,
                    15.4,
                    apiValue = "timers and controllers"
                )
            )
        ), BuySellCategory(
            name = "Manual Farm Machinery",
            image = R.drawable.harvesting_machines,
            id = 1.5,
            apiValue = "manual farm machinery",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Axe",
                    R.drawable.sell_category_cereals,
                    15.1,
                    apiValue = "axe"
                ),
                BuySellCategory(
                    "Hoe",
                    R.drawable.sell_category_pulses,
                    15.2,
                    apiValue = "hoe"
                ),
                BuySellCategory(
                    "Sickle",
                    R.drawable.sell_category_vegetables,
                    15.3,
                    apiValue = "sickle"
                ),
                BuySellCategory(
                    "Hedge Shears",
                    R.drawable.sell_category_fruits,
                    15.4,
                    apiValue = "hedge shears"
                )
            )
        )
    )

    private val defaultBuySubcategories = listOf(
        BuySellCategory("All Products", R.drawable.sell_category_other, 10.1),
        BuySellCategory("Seeds", R.drawable.buy_category_seeds, 10.2, apiValue = "seeds"),
        BuySellCategory("Sprayers", R.drawable.buy_category_sprayers, 10.3, apiValue = "sprayers"),
        BuySellCategory(
            "Agricultural Drones",
            R.drawable.buy_category_agri_drone,
            10.4,
            apiValue = "drones"
        ),
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