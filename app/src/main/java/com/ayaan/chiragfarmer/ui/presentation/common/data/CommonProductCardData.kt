package com.ayaan.chiragfarmer.ui.presentation.common.data

data class CommonProductCardData(
    val imageRes: Int? = null,
    val imageUrl: String? = null,
    val productName: String,
    val brandName: String,
    val currentPrice: String,
    val originalPrice: String? = null,
    val rating: String,
    val isSoldOut: Boolean = true
)