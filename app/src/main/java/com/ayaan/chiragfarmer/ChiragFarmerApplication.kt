package com.ayaan.chiragfarmer

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.ayaan.chiragfarmer.data.local.AuthDataStore
import com.ayaan.chiragfarmer.utils.Constants.CLOUDINARY_CLOUD_NAME
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ChiragFarmerApplication : Application(), DefaultLifecycleObserver {

    @Inject
    lateinit var authDataStore: AuthDataStore

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super<Application>.onCreate()
        val config = mapOf(
            CLOUDINARY_CLOUD_NAME to BuildConfig.CLOUDINARY_CLOUD_NAME
        )
        MediaManager.init(this, config)

        // Register lifecycle observer to detect when app goes to background
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStop(owner: LifecycleOwner) {
        super<DefaultLifecycleObserver>.onStop(owner)
        // App moved to background - clear profile status
        applicationScope.launch {
            authDataStore.clearProfileStatus()
        }
    }
}
