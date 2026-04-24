package com.yash091099.ChiragFarmersApp

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.utils.Constants.CLOUDINARY_CLOUD_NAME
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltAndroidApp
class ChiragFarmerApplication : Application() {

    @Inject
    lateinit var chiragDataStore: ChiragDataStore

    private val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    override fun onCreate() {
        super.onCreate()

        // Cloudinary init
        val config = mapOf(
            CLOUDINARY_CLOUD_NAME to BuildConfig.CLOUDINARY_CLOUD_NAME
        )
        MediaManager.init(this, config)

        // Observe app lifecycle
        
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_STOP) {
                    applicationScope.launch {
                        chiragDataStore.clearProfileStatus()
                    }
                }
            }
        )
    }
}
