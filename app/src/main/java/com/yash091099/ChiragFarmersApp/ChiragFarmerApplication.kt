package com.yash091099.ChiragFarmersApp

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.utils.Constants.CLOUDINARY_CLOUD_NAME
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import org.osmdroid.config.Configuration
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

        // Firebase initialization is handled automatically by the google-services plugin
        // and the FirebaseInitProvider content provider
        Log.d("ChiragFarmerApplication", "App initialization started")

        // Initialize OSMDroid
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

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
