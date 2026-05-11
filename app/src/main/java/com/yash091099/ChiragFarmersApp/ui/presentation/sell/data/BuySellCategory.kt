package com.yash091099.ChiragFarmersApp.ui.presentation.sell.data

import com.yash091099.ChiragFarmersApp.R

data class BuySellCategory(
    val name: String,
    val image: Int,
    val id: Int,
    val apiValue: String? = null,
    val bannerImage: Int? = null,
    val subCategories: List<BuySellCategory> = emptyList()
)

object Categories {
    val sellCategories = listOf(
        BuySellCategory("Cereals &\nGrains", R.drawable.sell_category_cereals, 1),
        BuySellCategory("Pulses &\nLegumes", R.drawable.sell_category_pulses, 2),
        BuySellCategory("Fruits", R.drawable.sell_category_fruits, 3),
        BuySellCategory("Vegetables", R.drawable.sell_category_vegetables, 4),
        BuySellCategory("Oilseeds", R.drawable.sell_category_oilseeds, 5),
        BuySellCategory("Spices", R.drawable.sell_category_spices, 6),
        BuySellCategory("Tubers and\nRoot Crops", R.drawable.sell_category_tubers, 7),
        BuySellCategory("Flowers", R.drawable.sell_category_flowers, 8),
        BuySellCategory("Dry Fruits &\nNuts", R.drawable.sell_category_dryfruits, 9),
        BuySellCategory("Organic\nProcedure", R.drawable.sell_category_organicproduces, 10),
        BuySellCategory("Livestock\nProducts", R.drawable.sell_category_livestock, 11),
        BuySellCategory("Other", R.drawable.sell_category_other, 12)
    )

    val buyCategories = listOf(
        BuySellCategory(
            name = "Seeds",
            image = R.drawable.buy_category_seeds,
            id = 1,
            apiValue = "seeds",
            bannerImage = R.drawable.seeds_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Paddy Seeds",
                    R.drawable.paddy_seeds,
                    101,
                    apiValue = "paddy seeds"
                ),
                BuySellCategory(
                    "Fruits Seeds",
                    R.drawable.fruits_seeds,
                    102,
                    apiValue = "fruits seeds"
                ),
                BuySellCategory(
                    "Vegetables Seeds",
                    R.drawable.veg_seeds,
                    103,
                    apiValue = "vegetable seeds"
                ),
                BuySellCategory(
                    "Leafy Seeds",
                    R.drawable.leafy_seeds,
                    104,
                    apiValue = "leafy seeds"
                ),
                BuySellCategory("Herb Seeds", R.drawable.herb_seeds, 105, apiValue = "herb seeds"),
            )
        ), BuySellCategory(
            name = "Sprayers",
            image = R.drawable.buy_category_sprayers,
            id = 2,
            apiValue = "sprayers",
            bannerImage = R.drawable.agri_sprayers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Manual Hand\nSprayers",
                    R.drawable.manual_hand_sprayers,
                    201,
                    apiValue = "manual hand sprayers"
                ),
                BuySellCategory(
                    "Battery\nSprayers",
                    R.drawable.battery_sprayers,
                    202,
                    apiValue = "battery sprayers"
                ),
                BuySellCategory(
                    "Knapsack\nSprayers",
                    R.drawable.knapsack_sprayers,
                    203,
                    apiValue = "knapsack sprayers"
                ),
                BuySellCategory(
                    "Power\nSprayers",
                    R.drawable.power_sprayers,
                    204,
                    apiValue = "power sprayers"
                ),
                BuySellCategory(
                    "HTP\nSprayers",
                    R.drawable.htp_sprayers,
                    205,
                    apiValue = "htp sprayers"
                ),
            )
        ), BuySellCategory(
            name = "Agriculture\nDrone",
            image = R.drawable.buy_category_agri_drone,
            id = 3,
            apiValue = "drones",
            bannerImage = R.drawable.agri_drones,
            subCategories = listOf(
                BuySellCategory(
                    "Spraying\nDrones",
                    R.drawable.spraying_drones,
                    301,
                    apiValue = "spraying drones"
                ),
                BuySellCategory(
                    "Surveillance/\nMonitoring Drones",
                    R.drawable.surveillance_drones,
                    302,
                    apiValue = "surveillance drones"
                ),
                BuySellCategory(
                    "Mulitspectral\n& Mapping",
                    R.drawable.multispectral_mapping,
                    303,
                    apiValue = "multispectral and mapping"
                )
            )
        ), BuySellCategory(
            name = "Tractors",
            image = R.drawable.buy_category_tractor,
            id = 4,
            apiValue = "tractors",
            bannerImage = R.drawable.tractors_banner,
            subCategories = listOf(
                BuySellCategory(
                    "4WD\nTractors",
                    R.drawable.fourwdtractors,
                    401,
                    apiValue = "4WD tractors"
                ),
                BuySellCategory(
                    "Row Crop\nTractors",
                    R.drawable.row_crop_tractors,
                    402,
                    apiValue = "row crop tractors"
                ),
                BuySellCategory(
                    "Mini\nTractors",
                    R.drawable.mini_tractors,
                    403,
                    apiValue = "mini tractors"
                ),
                BuySellCategory(
                    "Rotavator\nAttachments",
                    R.drawable.rotavator_attachments,
                    404,
                    apiValue = "rotavator attachments"
                ),
                BuySellCategory(
                    "Utility\nTractor",
                    R.drawable.utility_tractors,
                    405,
                    apiValue = "utility tractor"
                ),
            )
        ), BuySellCategory(
            name = "Direct From\nFarmers",
            image = R.drawable.buy_category_direct_from_farmers,
            id = 5,
            apiValue = "direct-from-farmers",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Cereals",
                    R.drawable.sell_category_cereals,
                    501,
                    apiValue = "cereals"
                ),
                BuySellCategory(
                    "Pulses",
                    R.drawable.sell_category_pulses,
                    502,
                    apiValue = "pulses"
                ),
                BuySellCategory(
                    "Vegetables",
                    R.drawable.sell_category_vegetables,
                    503,
                    apiValue = "vegetables"
                ),
                BuySellCategory(
                    "Fruits",
                    R.drawable.sell_category_fruits,
                    504,
                    apiValue = "fruits"
                ),
                BuySellCategory(
                    "Oilseeds",
                    R.drawable.sell_category_oilseeds,
                    505,
                    apiValue = "oilseeds"
                ),
                BuySellCategory(
                    "Spices",
                    R.drawable.sell_category_spices,
                    506,
                    apiValue = "spices"
                ),
                BuySellCategory(
                    "Organic Produce",
                    R.drawable.sell_category_organicproduces,
                    507,
                    apiValue = "organic produce"
                ),
                BuySellCategory(
                    "Livestock Products",
                    R.drawable.sell_category_livestock,
                    508,
                    apiValue = "livestock products"
                )
            )
        ), BuySellCategory(
            name = "Pesticides",
            image = R.drawable.pesticides,
            id = 6,
            apiValue = "pesticides",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Insecticides",
                    R.drawable.insecticides,
                    601,
                    apiValue = "insecticides"
                ),
                BuySellCategory(
                    "Fungicides",
                    R.drawable.fungicides,
                    602,
                    apiValue = "fungicides"
                ),
                BuySellCategory(
                    "Herbicides\n(Weedicides)",
                    R.drawable.herbicides,
                    603,
                    apiValue = "herbicides"
                ),
                BuySellCategory(
                    "Bio-Pesticides",
                    R.drawable.pesticides,
                    604,
                    apiValue = "bio-pesticides"
                ),
            )
        ), BuySellCategory(
            name = "Harvesting\nMachines",
            image = R.drawable.harvesting_machines,
            id = 7,
            apiValue = "harvesting machines",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Combine Harvester",
                    R.drawable.combine_harveters,
                    701,
                    apiValue = "combine harvester"
                ),
                BuySellCategory(
                    "Reaper",
                    R.drawable.reaper,
                    702,
                    apiValue = "reaper"
                ),
                BuySellCategory(
                    "Paddy\nHarvester",
                    R.drawable.paddy_harvester,
                    703,
                    apiValue = "paddy harvester"
                ),
                BuySellCategory(
                    "Sugarcane Harvesters",
                    R.drawable.sugarcane_harveters,
                    704,
                    apiValue = "sugarcane harvester"
                ),
                BuySellCategory(
                    "Groundnut Harvesters",
                    R.drawable.groundnut_harvesters,
                    705,
                    apiValue = "groundnut harvester"
                )
            )
        ), BuySellCategory(
            name = "Soil\nTesters",
            image = R.drawable.soil_testers,
            id = 8,
            apiValue = "soil testers",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Soil PH Meter",
                    R.drawable.soil_ph,
                    801,
                    apiValue = "soil ph meter"
                ),
                BuySellCategory(
                    "Soil Moisture Meter",
                    R.drawable.soil_moisture,
                    802,
                    apiValue = "soil moisture meter"
                ),
                BuySellCategory(
                    "NPK Soil Testing Kit",
                    R.drawable.npk_soil_testing,
                    803,
                    apiValue = "npk soil testing kit"
                ),
                BuySellCategory(
                    "Digital Soil Testing Device",
                    R.drawable.digital_soil_testing,
                    804,
                    apiValue = "digital soil testing device"
                )
            )
        ), BuySellCategory(
            name = "Power\nWeeders",
            image = R.drawable.power_weeders,
            id = 9,
            apiValue = "power weeders",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Mini Power Weeders",
                    R.drawable.mini_power_weeders,
                    901,
                    apiValue = "mini power weeders"
                ),
                BuySellCategory(
                    "Petrol Power Weeders",
                    R.drawable.petrol_power_weeders,
                    902,
                    apiValue = "petrol power weeders"
                ),
                BuySellCategory(
                    "Diesel Power Weeders",
                    R.drawable.diesel_power_weeders,
                    903,
                    apiValue = "diesel power weeders"
                ),
                BuySellCategory(
                    "Self-Propelled Weeders",
                    R.drawable.self_propelled_weeders,
                    904,
                    apiValue = "self-propelled weeders"
                )
            )
        ), BuySellCategory(
            name = "Thresher\nWinnowers",
            image = R.drawable.thresher,
            id = 10,
            apiValue = "thresher winnower",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Wheat Thresher",
                    R.drawable.wheat_thresher,
                    1001,
                    apiValue = "wheat thresher"
                ),
                BuySellCategory(
                    "Multi-Crop\nThresher",
                    R.drawable.multi_crop_thresher,
                    1002,
                    apiValue = "multi-crop thresher"
                ),
                BuySellCategory(
                    "Paddy\nThresher",
                    R.drawable.pady_thresher,
                    1003,
                    apiValue = "paddy harvester"
                ),
                BuySellCategory(
                    "Motorized\nWinnower",
                    R.drawable.motorized_winnowers,
                    1004,
                    apiValue = "motorized winnower"
                )
            )
        ), BuySellCategory(
            name = "Irrigation\nTools",
            image = R.drawable.irrigation_tools,
            id = 11,
            apiValue = "irrigation tools",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "PVC Pipes\n& fittings",
                    R.drawable.pvc_pipes,
                    1101,
                    apiValue = "pvc pipes and fittings"
                ),
                BuySellCategory(
                    "Submersible\nPumps",
                    R.drawable.submersible_pipes,
                    1102,
                    apiValue = "submersible pumps"
                ),
                BuySellCategory(
                    "Rain\nGuns",
                    R.drawable.rain_guns,
                    1103,
                    apiValue = "rain guns"
                ),
                BuySellCategory(
                    "Hose\nPipes",
                    R.drawable.hoes_pipes,
                    1104,
                    apiValue = "hose pipes"
                ),
                BuySellCategory(
                    "Diesel/Electric\nWater Pumps",
                    R.drawable.diesel_pumps,
                    1105,
                    apiValue = "diesel electric water pumps"
                ),
                BuySellCategory(
                    "Timers &\nControllers",
                    R.drawable.timers_controllers,
                    1106,
                    apiValue = "timers and controllers"
                )
            )
        ), BuySellCategory(
            name = "Manual Farm\nMachinery",
            image = R.drawable.farm_machinery,
            id = 12,
            apiValue = "manual farm machinery",
            bannerImage = R.drawable.direct_from_farmers_banner,
            subCategories = listOf(
                BuySellCategory(
                    "Axe",
                    R.drawable.axe,
                    1201,
                    apiValue = "axe"
                ),
                BuySellCategory(
                    "Hoe",
                    R.drawable.hoe,
                    1202,
                    apiValue = "hoe"
                ),
                BuySellCategory(
                    "Sickle",
                    R.drawable.sickle,
                    1203,
                    apiValue = "sickle"
                ),
                BuySellCategory(
                    "Hedge\nShears",
                    R.drawable.hedge_shears,
                    1204,
                    apiValue = "hedge shears"
                )
            )
        )
    )

    private val defaultBuySubcategories = listOf(
        BuySellCategory("All Products", R.drawable.sell_category_other, 1),
        BuySellCategory("Seeds", R.drawable.buy_category_seeds, 2, apiValue = "seeds"),
        BuySellCategory("Sprayers", R.drawable.buy_category_sprayers, 3, apiValue = "sprayers"),
        BuySellCategory(
            "Agricultural Drones",
            R.drawable.buy_category_agri_drone,
            4,
            apiValue = "drones"
        ),
        BuySellCategory("Tractors", R.drawable.buy_category_tractor, 5, apiValue = "tractors")
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