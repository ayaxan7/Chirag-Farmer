package com.yash091099.ChiragFarmersApp.utils

/**
 * Utility to generate share links handled by backend and deep links into the app.
 * Replace `yourdomain.com` with your real backend domain.
 */
object ShareUtils {
    fun generateShareLink(type: String, id: String, domain: String = "yourdomain.com"): String {
        val normalizedType = type.trim().lowercase()
        return "https://$domain/share/$normalizedType/$id"
    }
}

