package com.ayaan.chiragfarmer.utils

import android.content.Context
import android.net.Uri
import com.ayaan.chiragfarmer.BuildConfig
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object CloudinaryUploader {

    /**
     * Uploads a single image [Uri] to Cloudinary using unsigned upload and returns the secure_url.
     * This is a suspend function and should be called from a coroutine.
     *
     * @param context Application context
     * @param uri     URI of the image to upload
     * @return        The Cloudinary secure_url string of the uploaded image
     */
    suspend fun uploadImage(context: Context, uri: Uri): String =
        suspendCancellableCoroutine { continuation ->
            val requestId = MediaManager.get()
                .upload(uri)
                .unsigned(BuildConfig.CLOUDINARY_UPLOAD_PRESET)
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {}

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val secureUrl = resultData["secure_url"] as? String
                        if (secureUrl != null) {
                            continuation.resume(secureUrl)
                        } else {
                            continuation.resumeWithException(
                                Exception("Cloudinary upload succeeded but secure_url was missing")
                            )
                        }
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        continuation.resumeWithException(
                            Exception("Cloudinary upload failed: ${error.description}")
                        )
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        continuation.resumeWithException(
                            Exception("Cloudinary upload rescheduled: ${error.description}")
                        )
                    }
                })
                .dispatch(context)

            continuation.invokeOnCancellation {
                MediaManager.get().cancelRequest(requestId)
            }
        }
}
