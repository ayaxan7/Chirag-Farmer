package com.ayaan.chiragfarmer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * Utility object for Base64 image encoding and decoding operations.
 *
 * This utility provides functions for:
 * - Converting Base64 strings to Bitmaps and ImageBitmaps
 * - Converting URIs to Base64 strings
 * - Handling data URI prefixes (e.g., "data:image/jpeg;base64,...")
 * - Image compression and format conversion
 *
 * Usage Examples:
 * ```kotlin
 * // Decode Base64 to Bitmap
 * val bitmap = Base64ImageUtils.decodeBase64ToBitmap(base64String)
 *
 * // Decode for Compose
 * val imageBitmap = Base64ImageUtils.decodeBase64ToImageBitmap(base64String)
 *
 * // Encode URI to Base64
 * val base64 = Base64ImageUtils.encodeUriToBase64(context, imageUri)
 * ```
 */
object Base64ImageUtils {

    /**
     * Decodes a Base64 string to a Bitmap.
     * Automatically handles data URI prefixes (e.g., "data:image/jpeg;base64,...")
     *
     * @param base64String The Base64 encoded string (with or without data URI prefix)
     * @return Decoded Bitmap, or null if decoding fails
     *
     * @example
     * ```kotlin
     * val bitmap = Base64ImageUtils.decodeBase64ToBitmap("data:image/png;base64,iVBORw0...")
     * ```
     */
    fun decodeBase64ToBitmap(base64String: String?): Bitmap? {
        if (base64String.isNullOrBlank()) return null

        return try {
            // Remove data URI prefix if present (e.g., "data:image/jpeg;base64,")
            val cleanBase64 = cleanBase64String(base64String)

            // Decode Base64 to byte array
            val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)

            // Convert byte array to Bitmap
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Decodes a Base64 string to an ImageBitmap for use in Jetpack Compose.
     * Automatically handles data URI prefixes.
     *
     * @param base64String The Base64 encoded string (with or without data URI prefix)
     * @return Decoded ImageBitmap, or null if decoding fails
     *
     * @example
     * ```kotlin
     * val imageBitmap = Base64ImageUtils.decodeBase64ToImageBitmap(base64String)
     * if (imageBitmap != null) {
     *     Image(bitmap = imageBitmap, contentDescription = "Image")
     * }
     * ```
     */
    fun decodeBase64ToImageBitmap(base64String: String?): ImageBitmap? {
        return decodeBase64ToBitmap(base64String)?.asImageBitmap()
    }

    /**
     * Encodes a URI to a Base64 string with data URI prefix.
     *
     * @param context Android context
     * @param uri Image URI to encode
     * @param quality JPEG compression quality (0-100), default is 80
     * @param format Image format (JPEG or PNG), default is JPEG
     * @param includeDataUriPrefix Whether to include "data:image/...;base64," prefix, default is true
     * @return Base64 encoded string with data URI prefix, or null if encoding fails
     *
     * @example
     * ```kotlin
     * val base64 = Base64ImageUtils.encodeUriToBase64(
     *     context = context,
     *     uri = imageUri,
     *     quality = 80,
     *     format = Bitmap.CompressFormat.JPEG
     * )
     * ```
     */
    fun encodeUriToBase64(
        context: Context,
        uri: Uri?,
        quality: Int = 80,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        includeDataUriPrefix: Boolean = true
    ): String? {
        if (uri == null) return null

        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int

            inputStream?.use { input ->
                while (input.read(buffer).also { length = it } != -1) {
                    byteArrayOutputStream.write(buffer, 0, length)
                }
            }

            val imageBytes = byteArrayOutputStream.toByteArray()
            val base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

            if (includeDataUriPrefix) {
                // Get MIME type from URI or use default based on format
                val mimeType = context.contentResolver.getType(uri) ?: when (format) {
                    Bitmap.CompressFormat.PNG -> "image/png"
                    Bitmap.CompressFormat.WEBP -> "image/webp"
                    else -> "image/jpeg"
                }
                "data:$mimeType;base64,$base64String"
            } else {
                base64String
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Encodes a Bitmap to a Base64 string with optional compression.
     *
     * @param bitmap Bitmap to encode
     * @param quality JPEG/WEBP compression quality (0-100), default is 80
     * @param format Image format (JPEG, PNG, or WEBP), default is JPEG
     * @param includeDataUriPrefix Whether to include "data:image/...;base64," prefix, default is true
     * @return Base64 encoded string, or null if encoding fails
     *
     * @example
     * ```kotlin
     * val base64 = Base64ImageUtils.encodeBitmapToBase64(
     *     bitmap = myBitmap,
     *     quality = 90,
     *     format = Bitmap.CompressFormat.PNG
     * )
     * ```
     */
    @RequiresApi(Build.VERSION_CODES.R)
    fun encodeBitmapToBase64(
        bitmap: Bitmap?,
        quality: Int = 80,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        includeDataUriPrefix: Boolean = true
    ): String? {
        if (bitmap == null) return null

        return try {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(format, quality, outputStream)
            val byteArray = outputStream.toByteArray()
            val base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP)

            if (includeDataUriPrefix) {
                val mimeType = when (format) {
                    Bitmap.CompressFormat.PNG -> "image/png"
                    Bitmap.CompressFormat.WEBP, Bitmap.CompressFormat.WEBP_LOSSLESS, Bitmap.CompressFormat.WEBP_LOSSY -> "image/webp"
                    else -> "image/jpeg"
                }
                "data:$mimeType;base64,$base64String"
            } else {
                base64String
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Cleans a Base64 string by removing the data URI prefix if present.
     * Handles various formats:
     * - "data:image/png;base64,..." -> removes prefix
     * - "data:image/jpeg;base64,..." -> removes prefix
     * - Plain Base64 string -> returns as is
     *
     * @param base64String The Base64 string to clean
     * @return Clean Base64 string without data URI prefix
     */
    private fun cleanBase64String(base64String: String): String {
        return when {
            base64String.startsWith("data:image") -> {
                // Remove everything up to and including "base64,"
                base64String.substringAfter("base64,")
            }

            base64String.contains("base64,") -> {
                // Handle other data URI formats
                base64String.substringAfter("base64,")
            }

            else -> base64String
        }
    }

    /**
     * Checks if a string is a valid Base64 encoded image with data URI prefix.
     *
     * @param input String to check
     * @return True if the string appears to be a Base64 encoded image
     */
    fun isBase64Image(input: String?): Boolean {
        if (input.isNullOrBlank()) return false

        return input.startsWith("data:image") || input.matches(Regex("^[A-Za-z0-9+/]*={0,2}\$"))
    }

    /**
     * Extracts the MIME type from a Base64 data URI string.
     *
     * @param base64String Base64 string with data URI prefix
     * @return MIME type (e.g., "image/png"), or null if not found
     *
     * @example
     * ```kotlin
     * val mimeType = Base64ImageUtils.extractMimeType("data:image/png;base64,...")
     * // Returns: "image/png"
     * ```
     */
    fun extractMimeType(base64String: String?): String? {
        if (base64String.isNullOrBlank() || !base64String.startsWith("data:")) {
            return null
        }

        return try {
            val mimeTypePart = base64String.substringAfter("data:").substringBefore(";")
            mimeTypePart.takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Decodes multiple Base64 strings to Bitmaps.
     * Useful for processing image lists/galleries.
     *
     * @param base64Strings List of Base64 encoded strings
     * @return List of decoded Bitmaps (null entries are filtered out)
     *
     * @example
     * ```kotlin
     * val bitmaps = Base64ImageUtils.decodeMultipleBase64ToBitmaps(imageList)
     * ```
     */
    fun decodeMultipleBase64ToBitmaps(base64Strings: List<String>?): List<Bitmap> {
        if (base64Strings.isNullOrEmpty()) return emptyList()

        return base64Strings.mapNotNull { decodeBase64ToBitmap(it) }
    }

    /**
     * Decodes the first valid Base64 image from a list.
     * Useful when you only need one image from a list of potential images.
     *
     * @param base64Strings List of Base64 encoded strings
     * @return First successfully decoded Bitmap, or null if none could be decoded
     *
     * @example
     * ```kotlin
     * val firstImage = Base64ImageUtils.decodeFirstBase64ToBitmap(item.itemImages)
     * ```
     */
    fun decodeFirstBase64ToBitmap(base64Strings: List<String>?): Bitmap? {
        if (base64Strings.isNullOrEmpty()) return null

        return base64Strings.firstNotNullOfOrNull { decodeBase64ToBitmap(it) }
    }

    /**
     * Decodes the first valid Base64 image from a list to ImageBitmap.
     *
     * @param base64Strings List of Base64 encoded strings
     * @return First successfully decoded ImageBitmap, or null if none could be decoded
     */
    fun decodeFirstBase64ToImageBitmap(base64Strings: List<String>?): ImageBitmap? {
        return decodeFirstBase64ToBitmap(base64Strings)?.asImageBitmap()
    }

    val sampleBanner =
        "data:image/png;base64,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"
}
