package com.yash091099.ChiragFarmersApp.utils

import com.yash091099.ChiragFarmersApp.BuildConfig

/**
 * Utility to generate share links handled by backend and deep links into the app.
 */
object ShareUtils {
    fun generateShareLink(type: String, id: String): String {
        val shareUrl = BuildConfig.BASE_URL+"share"
//        val protocol = if (isDevelopment) "http" else "https"

        val normalizedType = type.trim().lowercase()

        return "$shareUrl/$normalizedType/$id"
    }
}