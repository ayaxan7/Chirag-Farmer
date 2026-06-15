package com.yash091099.ChiragFarmersApp

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.cloudinary.android.MediaManager
import com.phonepe.intent.sdk.api.PhonePeKt
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.utils.Constants.CLOUDINARY_CLOUD_NAME
import com.yash091099.ChiragFarmersApp.utils.logging.CrashlyticsTree
import com.yash091099.ChiragFarmersApp.utils.logging.SentryTree
import dagger.hilt.android.HiltAndroidApp
import io.sentry.Sentry
import io.sentry.android.core.SentryAndroid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltAndroidApp
class ChiragFarmerApplication : Application() {

    @Inject
    lateinit var chiragDataStore: ChiragDataStore

    @Inject
    lateinit var crashlyticsTree: CrashlyticsTree

    private val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    override fun onCreate() {
        super.onCreate()

        SentryAndroid.init(this) { options ->
            options.dsn = BuildConfig.SENTRY_DSN
            options.isEnableAutoSessionTracking = true
            options.tracesSampleRate = 0.0

            options.environment =
                if (BuildConfig.DEBUG) "development" else "production"

            options.release =
                "${BuildConfig.APPLICATION_ID}@${BuildConfig.VERSION_NAME}+${BuildConfig.VERSION_CODE}"
        }
        Sentry.configureScope {
            it.setTag(
                "build_type",
                if (BuildConfig.DEBUG) "debug" else "release"
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.plant(SentryTree())
        } else {
            Timber.plant(crashlyticsTree)
            Timber.plant(SentryTree())
        }

        Timber.tag("SENTRY").i(
            "environment=%s release=%s buildType=%s",
            if (BuildConfig.DEBUG) "debug" else "production",
            "${BuildConfig.APPLICATION_ID}@${BuildConfig.VERSION_NAME}+${BuildConfig.VERSION_CODE}",
            if (BuildConfig.DEBUG) "debug" else "release"
        )

        // OSMDroid
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        // Cloudinary
        MediaManager.init(
            this,
            mapOf(
                CLOUDINARY_CLOUD_NAME to BuildConfig.CLOUDINARY_CLOUD_NAME
            )
        )

        // PhonePe
        initPhonePeSdk()

        // Lifecycle observer
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

    private fun initPhonePeSdk() {
        try {

            val merchantId = BuildConfig.PHONEPE_MERCHANT_ID

            if (merchantId.isBlank()) {
                Timber.e("PHONEPE_MERCHANT_ID is empty in BuildConfig")
                return
            }

            val initialized = PhonePeKt.init(
                context = this,
                merchantId = merchantId,
                flowId = UUID.randomUUID().toString(),
                phonePeEnvironment = PhonePeEnvironment.RELEASE,
                enableLogging = false,
                appId = null
            )

            if (initialized) {
                Timber.d("PhonePe SDK initialized successfully")
            } else {
                Timber.e("PhonePe SDK initialization failed")
            }

        } catch (e: Exception) {
            Timber.e(e, "PhonePe SDK initialization error")
        }
    }
}