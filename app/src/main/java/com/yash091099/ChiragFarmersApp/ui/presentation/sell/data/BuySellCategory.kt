package com.yash091099.ChiragFarmersApp.ui.presentation.sell.data

import com.yash091099.ChiragFarmersApp.R

data class BuySellCategory(
    val name: String,
    val image: Int,
    val id: Int,
    val apiValue: String? = null,
    val bannerImage: Int? = null,
    val subCategories: List<BuySellCategory> = emptyList(),
    val displayNameRes: Int? = null,
    val bannerImageHi: Int? = null,
    val bannerImageTe: Int? = null,
    val bannerImagePa: Int? = null
)

object Categories {
    val sellCategories = listOf(
        BuySellCategory("Cereals &\nGrains", R.drawable.sell_category_cereals, 1, displayNameRes = R.string.category_sell_cereals_grains),
        BuySellCategory("Pulses &\nLegumes", R.drawable.sell_category_pulses, 2, displayNameRes = R.string.category_sell_pulses_legumes),
        BuySellCategory("Fruits", R.drawable.sell_category_fruits, 3, displayNameRes = R.string.category_sell_fruits),
        BuySellCategory("Vegetables", R.drawable.sell_category_vegetables, 4, displayNameRes = R.string.category_sell_vegetables),
        BuySellCategory("Oilseeds", R.drawable.sell_category_oilseeds, 5, displayNameRes = R.string.category_sell_oilseeds),
        BuySellCategory("Spices", R.drawable.sell_category_spices, 6, displayNameRes = R.string.category_sell_spices),
        BuySellCategory("Tubers and\nRoot Crops", R.drawable.sell_category_tubers, 7, displayNameRes = R.string.category_sell_tubers_root),
        BuySellCategory("Flowers", R.drawable.sell_category_flowers, 8, displayNameRes = R.string.category_sell_flowers),
        BuySellCategory("Dry Fruits &\nNuts", R.drawable.sell_category_dryfruits, 9, displayNameRes = R.string.category_sell_dry_fruits_nuts),
        BuySellCategory("Organic\nProcedure", R.drawable.sell_category_organicproduces, 10, displayNameRes = R.string.category_sell_organic),
        BuySellCategory("Livestock\nProducts", R.drawable.sell_category_livestock, 11, displayNameRes = R.string.category_sell_livestock),
        BuySellCategory("Other", R.drawable.sell_category_other, 12, displayNameRes = R.string.category_sell_other)
    )

    val buyCategories = listOf(
        BuySellCategory(
            name = "Seeds",
            image = R.drawable.buy_category_seeds,
            id = 1,
            apiValue = "seeds",
            bannerImage = R.drawable.seeds_banner,
            bannerImageHi = R.drawable.seeds_banner_hi,
            bannerImageTe = R.drawable.seeds_banner_te,
            bannerImagePa = R.drawable.seeds_banner_pa,
            displayNameRes = R.string.category_seeds,
            subCategories = listOf(
                BuySellCategory(
                    "Paddy Seeds",
                    R.drawable.paddy_seeds,
                    101,
                    apiValue = "paddy seeds",
                    displayNameRes = R.string.category_paddy_seeds
                ),
                BuySellCategory(
                    "Fruits Seeds",
                    R.drawable.fruits_seeds,
                    102,
                    apiValue = "fruits seeds",
                    displayNameRes = R.string.category_fruit_seeds
                ),
                BuySellCategory(
                    "Vegetables Seeds",
                    R.drawable.veg_seeds,
                    103,
                    apiValue = "vegetable seeds",
                    displayNameRes = R.string.category_vegetable_seeds
                ),
                BuySellCategory(
                    "Leafy Seeds",
                    R.drawable.leafy_seeds,
                    104,
                    apiValue = "leafy seeds",
                    displayNameRes = R.string.category_leafy_seeds
                ),
                BuySellCategory("Herb Seeds", R.drawable.herb_seeds, 105, apiValue = "herb seeds", displayNameRes = R.string.category_herb_seeds),
            )
        ), BuySellCategory(
            name = "Sprayers",
            image = R.drawable.buy_category_sprayers,
            id = 2,
            apiValue = "sprayers",
            bannerImage = R.drawable.agri_sprayers_banner,
            bannerImageHi = R.drawable.agri_sprayers_banner_hi,
            bannerImageTe = R.drawable.agri_sprayers_banner_te,
            bannerImagePa = R.drawable.agri_sprayers_banner_pa,
            displayNameRes = R.string.category_sprayers,
            subCategories = listOf(
                BuySellCategory(
                    "Manual Hand\nSprayers",
                    R.drawable.manual_hand_sprayers,
                    201,
                    apiValue = "manual hand sprayers",
                    displayNameRes = R.string.category_manual_sprayers
                ),
                BuySellCategory(
                    "Battery\nSprayers",
                    R.drawable.battery_sprayers,
                    202,
                    apiValue = "battery sprayers",
                    displayNameRes = R.string.category_battery_sprayers
                ),
                BuySellCategory(
                    "Knapsack\nSprayers",
                    R.drawable.knapsack_sprayers,
                    203,
                    apiValue = "knapsack sprayers",
                    displayNameRes = R.string.category_knapsack_sprayers
                ),
                BuySellCategory(
                    "Power\nSprayers",
                    R.drawable.power_sprayers,
                    204,
                    apiValue = "power sprayers",
                    displayNameRes = R.string.category_power_sprayers
                ),
                BuySellCategory(
                    "HTP\nSprayers",
                    R.drawable.htp_sprayers,
                    205,
                    apiValue = "htp sprayers",
                    displayNameRes = R.string.category_htp_sprayers
                ),
            )
        ), BuySellCategory(
            name = "Agriculture\nDrone",
            image = R.drawable.buy_category_agri_drone,
            id = 3,
            apiValue = "drones",
            bannerImage = R.drawable.agri_drones,
            bannerImageHi = R.drawable.agri_drones_hi,
            bannerImageTe = R.drawable.agri_drones_te,
            bannerImagePa = R.drawable.agri_drones_pa,
            displayNameRes = R.string.category_agriculture_drone,
            subCategories = listOf(
                BuySellCategory(
                    "Spraying\nDrones",
                    R.drawable.spraying_drones,
                    301,
                    apiValue = "spraying drones",
                    displayNameRes = R.string.category_spraying_drones
                ),
                BuySellCategory(
                    "Surveillance/\nMonitoring Drones",
                    R.drawable.surveillance_drones,
                    302,
                    apiValue = "surveillance drones",
                    displayNameRes = R.string.category_surveillance_drones
                ),
                BuySellCategory(
                    "Mulitspectral\n& Mapping",
                    R.drawable.multispectral_mapping,
                    303,
                    apiValue = "multispectral and mapping",
                    displayNameRes = R.string.category_multispectral_drones
                )
            )
        ), BuySellCategory(
            name = "Tractors",
            image = R.drawable.buy_category_tractor,
            id = 4,
            apiValue = "tractors",
            bannerImage = R.drawable.tractors_banner,
            bannerImageHi = R.drawable.tractors_banner_hi,
            bannerImageTe = R.drawable.tractors_banner_te,
            bannerImagePa = R.drawable.tractors_banner_pa,
            displayNameRes = R.string.category_tractors,
            subCategories = listOf(
                BuySellCategory(
                    "4WD\nTractors",
                    R.drawable.fourwdtractors,
                    401,
                    apiValue = "4WD tractors",
                    displayNameRes = R.string.category_4wd_tractors
                ),
                BuySellCategory(
                    "Row Crop\nTractors",
                    R.drawable.row_crop_tractors,
                    402,
                    apiValue = "row crop tractors",
                    displayNameRes = R.string.category_row_crop_tractors
                ),
                BuySellCategory(
                    "Mini\nTractors",
                    R.drawable.mini_tractors,
                    403,
                    apiValue = "mini tractors",
                    displayNameRes = R.string.category_mini_tractors
                ),
                BuySellCategory(
                    "Rotavator\nAttachments",
                    R.drawable.rotavator_attachments,
                    404,
                    apiValue = "rotavator attachments",
                    displayNameRes = R.string.category_rotavator
                ),
                BuySellCategory(
                    "Utility\nTractor",
                    R.drawable.utility_tractors,
                    405,
                    apiValue = "utility tractor",
                    displayNameRes = R.string.category_utility_tractor
                ),
            )
        ), BuySellCategory(
            name = "Direct From\nFarmers",
            image = R.drawable.buy_category_direct_from_farmers,
            id = 5,
            apiValue = "direct-from-farmers",
            bannerImage = R.drawable.direct_from_farmers_banner,
            bannerImageHi = R.drawable.direct_from_farmers_banner_hi,
            bannerImageTe = R.drawable.direct_from_farmers_banner_te,
            bannerImagePa = R.drawable.direct_from_farmers_banner_pa,
            displayNameRes = R.string.category_direct_from_farmers,
            subCategories = listOf(
                BuySellCategory(
                    "Cereals",
                    R.drawable.sell_category_cereals,
                    501,
                    apiValue = "cereals",
                    displayNameRes = R.string.category_cereals
                ),
                BuySellCategory(
                    "Pulses",
                    R.drawable.sell_category_pulses,
                    502,
                    apiValue = "pulses",
                    displayNameRes = R.string.category_pulses
                ),
                BuySellCategory(
                    "Vegetables",
                    R.drawable.sell_category_vegetables,
                    503,
                    apiValue = "vegetables",
                    displayNameRes = R.string.category_vegetables_cat
                ),
                BuySellCategory(
                    "Fruits",
                    R.drawable.sell_category_fruits,
                    504,
                    apiValue = "fruits",
                    displayNameRes = R.string.category_fruits_cat
                ),
                BuySellCategory(
                    "Oilseeds",
                    R.drawable.sell_category_oilseeds,
                    505,
                    apiValue = "oilseeds",
                    displayNameRes = R.string.category_oilseeds_cat
                ),
                BuySellCategory(
                    "Spices",
                    R.drawable.sell_category_spices,
                    506,
                    apiValue = "spices",
                    displayNameRes = R.string.category_spices_cat
                ),
                BuySellCategory(
                    "Organic Produce",
                    R.drawable.sell_category_organicproduces,
                    507,
                    apiValue = "organic produce",
                    displayNameRes = R.string.category_organic_produce
                ),
                BuySellCategory(
                    "Livestock Products",
                    R.drawable.sell_category_livestock,
                    508,
                    apiValue = "livestock products",
                    displayNameRes = R.string.category_livestock_products
                )
            )
        ), BuySellCategory(
            name = "Pesticides",
            image = R.drawable.pesticides,
            id = 6,
            apiValue = "pesticides",
            bannerImage = R.drawable.direct_from_farmers_banner,
            bannerImageHi = R.drawable.direct_from_farmers_banner_hi,
            bannerImageTe = R.drawable.direct_from_farmers_banner_te,
            bannerImagePa = R.drawable.direct_from_farmers_banner_pa,
            displayNameRes = R.string.category_pesticides,
            subCategories = listOf(
                BuySellCategory(
                    "Insecticides",
                    R.drawable.insecticides,
                    601,
                    apiValue = "insecticides",
                    displayNameRes = R.string.category_insecticides
                ),
                BuySellCategory(
                    "Fungicides",
                    R.drawable.fungicides,
                    602,
                    apiValue = "fungicides",
                    displayNameRes = R.string.category_fungicides
                ),
                BuySellCategory(
                    "Herbicides\n(Weedicides)",
                    R.drawable.herbicides,
                    603,
                    apiValue = "herbicides",
                    displayNameRes = R.string.category_herbicides
                ),
                BuySellCategory(
                    "Bio-Pesticides",
                    R.drawable.pesticides,
                    604,
                    apiValue = "bio-pesticides",
                    displayNameRes = R.string.category_bio_pesticides
                ),
            )
        ), BuySellCategory(
            name = "Harvesting\nMachines",
            image = R.drawable.harvesting_machines,
            id = 7,
            apiValue = "harvesting machines",
            bannerImage = R.drawable.direct_from_farmers_banner,
            bannerImageHi = R.drawable.direct_from_farmers_banner_hi,
            bannerImageTe = R.drawable.direct_from_farmers_banner_te,
            bannerImagePa = R.drawable.direct_from_farmers_banner_pa,
            displayNameRes = R.string.category_harvesting_machines,
            subCategories = listOf(
                BuySellCategory(
                    "Combine Harvester",
                    R.drawable.combine_harveters,
                    701,
                    apiValue = "combine harvester",
                    displayNameRes = R.string.category_combine_harvester
                ),
                BuySellCategory(
                    "Reaper",
                    R.drawable.reaper,
                    702,
                    apiValue = "reaper",
                    displayNameRes = R.string.category_reaper
                ),
                BuySellCategory(
                    "Paddy\nHarvester",
                    R.drawable.paddy_harvester,
                    703,
                    apiValue = "paddy harvester",
                    displayNameRes = R.string.category_paddy_harvester
                ),
                BuySellCategory(
                    "Sugarcane Harvesters",
                    R.drawable.sugarcane_harveters,
                    704,
                    apiValue = "sugarcane harvester",
                    displayNameRes = R.string.category_sugarcane_harvester
                ),
                BuySellCategory(
                    "Groundnut Harvesters",
                    R.drawable.groundnut_harvesters,
                    705,
                    apiValue = "groundnut harvester",
                    displayNameRes = R.string.category_groundnut_harvester
                )
            )
        ), BuySellCategory(
            name = "Soil\nTesters",
            image = R.drawable.soil_testers,
            id = 8,
            apiValue = "soil testers",
            bannerImage = R.drawable.direct_from_farmers_banner,
            bannerImageHi = R.drawable.direct_from_farmers_banner_hi,
            bannerImageTe = R.drawable.direct_from_farmers_banner_te,
            bannerImagePa = R.drawable.direct_from_farmers_banner_pa,
            displayNameRes = R.string.category_soil_testers,
            subCategories = listOf(
                BuySellCategory(
                    "Soil PH Meter",
                    R.drawable.soil_ph,
                    801,
                    apiValue = "soil ph meter",
                    displayNameRes = R.string.category_soil_ph_meter
                ),
                BuySellCategory(
                    "Soil Moisture Meter",
                    R.drawable.soil_moisture,
                    802,
                    apiValue = "soil moisture meter",
                    displayNameRes = R.string.category_soil_moisture_meter
                ),
                BuySellCategory(
                    "NPK Soil Testing Kit",
                    R.drawable.npk_soil_testing,
                    803,
                    apiValue = "npk soil testing kit",
                    displayNameRes = R.string.category_npk_kit
                ),
                BuySellCategory(
                    "Digital Soil Testing Device",
                    R.drawable.digital_soil_testing,
                    804,
                    apiValue = "digital soil testing device",
                    displayNameRes = R.string.category_digital_soil_tester
                )
            )
        ), BuySellCategory(
            name = "Power\nWeeders",
            image = R.drawable.power_weeders,
            id = 9,
            apiValue = "power weeders",
            bannerImage = R.drawable.direct_from_farmers_banner,
            bannerImageHi = R.drawable.direct_from_farmers_banner_hi,
            bannerImageTe = R.drawable.direct_from_farmers_banner_te,
            bannerImagePa = R.drawable.direct_from_farmers_banner_pa,
            displayNameRes = R.string.category_power_weeders,
            subCategories = listOf(
                BuySellCategory(
                    "Mini Power Weeders",
                    R.drawable.mini_power_weeders,
                    901,
                    apiValue = "mini power weeders",
                    displayNameRes = R.string.category_mini_power_weeders
                ),
                BuySellCategory(
                    "Petrol Power Weeders",
                    R.drawable.petrol_power_weeders,
                    902,
                    apiValue = "petrol power weeders",
                    displayNameRes = R.string.category_petrol_weeders
                ),
                BuySellCategory(
                    "Diesel Power Weeders",
                    R.drawable.diesel_power_weeders,
                    903,
                    apiValue = "diesel power weeders",
                    displayNameRes = R.string.category_diesel_weeders
                ),
                BuySellCategory(
                    "Self-Propelled Weeders",
                    R.drawable.self_propelled_weeders,
                    904,
                    apiValue = "self-propelled weeders",
                    displayNameRes = R.string.category_self_propelled_weeders
                )
            )
        ), BuySellCategory(
            name = "Thresher\nWinnowers",
            image = R.drawable.thresher,
            id = 10,
            apiValue = "thresher winnower",
            bannerImage = R.drawable.direct_from_farmers_banner,
            bannerImageHi = R.drawable.direct_from_farmers_banner_hi,
            bannerImageTe = R.drawable.direct_from_farmers_banner_te,
            bannerImagePa = R.drawable.direct_from_farmers_banner_pa,
            displayNameRes = R.string.category_thresher_winnowers,
            subCategories = listOf(
                BuySellCategory(
                    "Wheat Thresher",
                    R.drawable.wheat_thresher,
                    1001,
                    apiValue = "wheat thresher",
                    displayNameRes = R.string.category_wheat_thresher
                ),
                BuySellCategory(
                    "Multi-Crop\nThresher",
                    R.drawable.multi_crop_thresher,
                    1002,
                    apiValue = "multi-crop thresher",
                    displayNameRes = R.string.category_multi_crop_thresher
                ),
                BuySellCategory(
                    "Paddy\nThresher",
                    R.drawable.pady_thresher,
                    1003,
                    apiValue = "paddy harvester",
                    displayNameRes = R.string.category_paddy_thresher
                ),
                BuySellCategory(
                    "Motorized\nWinnower",
                    R.drawable.motorized_winnowers,
                    1004,
                    apiValue = "motorized winnower",
                    displayNameRes = R.string.category_motorized_winnower
                )
            )
        ), BuySellCategory(
            name = "Irrigation\nTools",
            image = R.drawable.irrigation_tools,
            id = 11,
            apiValue = "irrigation tools",
            bannerImage = R.drawable.direct_from_farmers_banner,
            bannerImageHi = R.drawable.direct_from_farmers_banner_hi,
            bannerImageTe = R.drawable.direct_from_farmers_banner_te,
            bannerImagePa = R.drawable.direct_from_farmers_banner_pa,
            displayNameRes = R.string.category_irrigation_tools,
            subCategories = listOf(
                BuySellCategory(
                    "PVC Pipes\n& fittings",
                    R.drawable.pvc_pipes,
                    1101,
                    apiValue = "pvc pipes and fittings",
                    displayNameRes = R.string.category_pvc_pipes
                ),
                BuySellCategory(
                    "Submersible\nPumps",
                    R.drawable.submersible_pipes,
                    1102,
                    apiValue = "submersible pumps",
                    displayNameRes = R.string.category_submersible_pumps
                ),
                BuySellCategory(
                    "Rain\nGuns",
                    R.drawable.rain_guns,
                    1103,
                    apiValue = "rain guns",
                    displayNameRes = R.string.category_rain_guns
                ),
                BuySellCategory(
                    "Hose\nPipes",
                    R.drawable.hoes_pipes,
                    1104,
                    apiValue = "hose pipes",
                    displayNameRes = R.string.category_hose_pipes
                ),
                BuySellCategory(
                    "Diesel/Electric\nWater Pumps",
                    R.drawable.diesel_pumps,
                    1105,
                    apiValue = "diesel electric water pumps",
                    displayNameRes = R.string.category_water_pumps
                ),
                BuySellCategory(
                    "Timers &\nControllers",
                    R.drawable.timers_controllers,
                    1106,
                    apiValue = "timers and controllers",
                    displayNameRes = R.string.category_timers_controllers
                )
            )
        ), BuySellCategory(
            name = "Manual Farm\nMachinery",
            image = R.drawable.farm_machinery,
            id = 12,
            apiValue = "manual farm machinery",
            bannerImage = R.drawable.direct_from_farmers_banner,
            bannerImageHi = R.drawable.direct_from_farmers_banner_hi,
            bannerImageTe = R.drawable.direct_from_farmers_banner_te,
            bannerImagePa = R.drawable.direct_from_farmers_banner_pa,
            displayNameRes = R.string.category_manual_farm_machinery,
            subCategories = listOf(
                BuySellCategory(
                    "Axe",
                    R.drawable.axe,
                    1201,
                    apiValue = "axe",
                    displayNameRes = R.string.category_axe
                ),
                BuySellCategory(
                    "Hoe",
                    R.drawable.hoe,
                    1202,
                    apiValue = "hoe",
                    displayNameRes = R.string.category_hoe
                ),
                BuySellCategory(
                    "Sickle",
                    R.drawable.sickle,
                    1203,
                    apiValue = "sickle",
                    displayNameRes = R.string.category_sickle
                ),
                BuySellCategory(
                    "Hedge\nShears",
                    R.drawable.hedge_shears,
                    1204,
                    apiValue = "hedge shears",
                    displayNameRes = R.string.category_hedge_shears
                )
            )
        )
    )

    private val defaultBuySubcategories = listOf(
//        BuySellCategory("All Products", R.drawable.sell_category_other, 1, displayNameRes = R.string.category_all_products),
        BuySellCategory("Seeds", R.drawable.buy_category_seeds, 2, apiValue = "seeds", displayNameRes = R.string.category_seeds_cat),
        BuySellCategory("Sprayers", R.drawable.buy_category_sprayers, 3, apiValue = "sprayers", displayNameRes = R.string.category_sprayers_cat),
        BuySellCategory(
            "Agricultural Drones",
            R.drawable.buy_category_agri_drone,
            4,
            apiValue = "drones",
            displayNameRes = R.string.category_agricultural_drones
        ),
        BuySellCategory("Tractors", R.drawable.buy_category_tractor, 5, apiValue = "tractors", displayNameRes = R.string.category_tractors_cat)
    )

    fun getBuyCategory(categoryName: String): BuySellCategory? {
        return buyCategories.firstOrNull { it.matches(categoryName) }
    }

    fun getBuyBannerImage(categoryName: String, langTag: String = "en"): Int {
        val category = getBuyCategory(categoryName)
        val baseBanner = category?.bannerImage ?: R.drawable.buy_banner
        return when {
            langTag.startsWith("hi") -> category?.bannerImageHi ?: baseBanner
            langTag.startsWith("te") -> category?.bannerImageTe ?: baseBanner
            langTag.startsWith("pa") -> category?.bannerImagePa ?: baseBanner
            else -> baseBanner
        }
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
