package com.yash091099.ChiragFarmersApp.utils

import android.util.Log
import com.yash091099.ChiragFarmersApp.BuildConfig
import org.hashids.Hashids

/**
 * Utility to generate share links handled by backend and deep links into the app.
 * Uses Hashids to encode ObjectIds for security - prevents enumeration attacks
 * and hides internal database IDs from public URLs.
 */
object ShareUtils {
    
    private const val MIN_HASH_LENGTH = 8
    private val hashids = Hashids(BuildConfig.HASHIDS_SALT, MIN_HASH_LENGTH)
    
    /**
     * Encodes a MongoDB ObjectId to a short, URL-safe string.
     * The ObjectId (24-char hex) is split into 6 chunks of 4 chars each,
     * converted to integers, and encoded using Hashids.
     * 
     * @param objectId The MongoDB ObjectId as a 24-character hex string
     * @return Encoded hash string
     */
    fun encodeId(objectId: String): String {
        if (objectId.length != 24) return objectId // Return as-is if not valid ObjectId
        
        // Split the 24-char hex string into 6 chunks of 4 chars each
        val numbers = objectId.chunked(4).map { chunk ->
            chunk.toLongOrNull(16) ?: 0L
        }.toLongArray()
        
        return hashids.encode(*numbers)
    }
    
    /**
     * Decodes a hash string back to the original MongoDB ObjectId.
     * 
     * @param hash The encoded hash string
     * @return The original ObjectId or null if invalid
     */
    fun decodeId(hash: String): String? {
        return try {
            val numbers = hashids.decode(hash)
            if (numbers.size != 6) return null
            
            // Convert the array of longs back to hex string
            val hex = numbers.joinToString("") { 
                it.toString(16).padStart(4, '0') 
            }
            
            // Validate it's a proper 24-char hex string (ObjectId format)
            if (hex.length != 24 || !hex.matches(Regex("[a-f0-9]+"))) null else hex
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Generates a share URL with encoded ObjectId.
     * 
     * @param type The type of resource ("product" or "farmer")
     * @param id The MongoDB ObjectId
     * @return Full share URL with encoded ID
     */
    fun generateShareLink(type: String, id: String): String {
        val shareUrl = BuildConfig.BASE_URL + "share"
//        val shareUrl = "https://backend.chiragvendor.com/" + "share"
        val normalizedType = type.trim().lowercase()
        Log.d("ShareUtils", "objectID: $id")
        val encodedId = encodeId(id)
        Log.d("ShareUtils", "Encoded ID: $encodedId")
        val finalUrl = "$shareUrl/$normalizedType/$encodedId"
        Log.d("ShareUtils", "Generated share URL: $finalUrl")
        return finalUrl.trim()
    }
}