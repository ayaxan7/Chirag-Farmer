package com.ayaan.chiragfarmer

import android.app.Application
import com.ayaan.chiragfarmer.utils.Constants.CLOUDINARY_CLOUD_NAME
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChiragFarmerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = mapOf(
            CLOUDINARY_CLOUD_NAME to BuildConfig.CLOUDINARY_CLOUD_NAME
        )
        MediaManager.init(this, config)
    }
}

