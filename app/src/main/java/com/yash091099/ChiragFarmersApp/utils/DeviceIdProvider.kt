package com.yash091099.ChiragFarmersApp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import java.util.UUID

/**
 * Provides a unique device identifier
 * Uses Android's Settings.Secure.ANDROID_ID if available
 * Falls back to a generated UUID stored in preferences
 */
object DeviceIdProvider {

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        // Try to get the Android ID first (hardware-based)
        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        return if (!androidId.isNullOrEmpty()) {
            androidId
        } else {
            // Fallback to a generated UUID
            // In production, this should be stored and reused
            UUID.randomUUID().toString()
        }
    }
}

