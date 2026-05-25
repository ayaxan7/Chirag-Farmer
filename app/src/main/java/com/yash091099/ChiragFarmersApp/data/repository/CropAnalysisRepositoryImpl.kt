package com.yash091099.ChiragFarmersApp.data.repository

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.CropAnalysisApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.CropAnalysisDataDto
import com.yash091099.ChiragFarmersApp.domain.repository.CropAnalysisRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.core.net.toUri
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CropAnalysisRepositoryImpl @Inject constructor(
    private val apiService: CropAnalysisApiService,
    private val chiragDataStore: ChiragDataStore,
    @ApplicationContext private val context: Context
) : CropAnalysisRepository {

    override suspend fun analyzeCrop(imageUri: String): Result<CropAnalysisDataDto> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrBlank()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val uri = imageUri.toUri()
            val imagePart = createImagePart(uri)
                ?: return Result.failure(Exception("Unable to read selected image"))

            val response = apiService.analyzeCrop("Bearer $token", imagePart)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun createImagePart(uri: Uri): MultipartBody.Part? {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
        val extension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(mimeType)
            ?: "jpg"
        val fileName = "crop_image.${extension.trimStart('.')}"

        val bytes = contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        } ?: return null

        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", fileName, requestBody)
    }
}


