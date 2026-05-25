package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CropAnalysisResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: CropAnalysisDataDto? = null
)

data class CropAnalysisDataDto(
    @SerializedName("crop_name")
    val cropName: String? = null,
    @SerializedName("disease_identified")
    val diseaseIdentified: DiseaseIdentifiedDto? = null,
    @SerializedName("symptoms_identified")
    val symptomsIdentified: List<String> = emptyList(),
    @SerializedName("avoid")
    val avoid: List<String> = emptyList(),
    @SerializedName("insecticides")
    val insecticides: List<CropInsecticideDto> = emptyList()
)

data class DiseaseIdentifiedDto(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("confidence")
    val confidence: Int? = null,
    @SerializedName("about")
    val about: String? = null
)

data class CropInsecticideDto(
    @SerializedName("pesticide_name")
    val pesticideName: String? = null,
    @SerializedName("dosage_use")
    val dosageUse: String? = null,
    @SerializedName("target")
    val target: String? = null
)

