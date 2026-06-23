package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.AddProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddProductResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.DeleteProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.DeleteProductResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.MixedProductsResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductReviewsResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.RateProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.RateProductResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductDetailedResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductDetailsResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductResponseDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.ReviewReactionRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.ReviewReactionResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.SearchProductResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.SellerDetailsResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.ToggleSoldOutRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.ToggleSoldOutResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateProductResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @GET("api/farmers/products")
    suspend fun getFarmerProducts(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("type") type: String,
        @Query("search") search: String? = null
    ): ProductResponseDto

    @GET("api/farmers/all-products")
    suspend fun getAllProducts(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("category") category: String,
        @Query("subcategory") subcategory: String? = null,
        @Query("minPrice") minPrice: String? = null,
        @Query("maxPrice") maxPrice: String? = null,
        @Query("sort") sort: String? = null,
        @Query("rating") rating: String? = null,
        @Query("location") location: String? = null
    ): ProductResponseDto

    @GET("api/farmers/smart-farming-products")
    suspend fun getSmartFarmingProducts(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("category") category: String,
        @Query("subcategory") subcategory: String? = null,
        @Query("minPrice") minPrice: String? = null,
        @Query("maxPrice") maxPrice: String? = null,
        @Query("sort") sort: String? = null,
        @Query("rating") rating: String? = null,
        @Query("location") location: String? = null
    ): ProductResponseDto

    @GET("api/farmers/mixed-products")
    suspend fun getMixedProducts(
        @Header("Authorization") token: String, @Query("screen") screen: String? = null
    ): MixedProductsResponse

    @GET("api/farmers/products/{productId}")
    suspend fun getProductDetails(
        @Header("Authorization") token: String, @Path("productId") productId: String
    ): ProductDetailsResponse

    @GET("api/farmers/products/{productId}/detailed")
    suspend fun getProductDetailsDetailed(
        @Header("Authorization") token: String, @Path("productId") productId: String
    ): ProductDetailedResponse

    @GET("api/farmers/products/{productId}/reviews")
    suspend fun getProductReviews(
        @Header("Authorization") token: String,
        @Path("productId") productId: String
    ): ProductReviewsResponse

    @POST("api/farmers/add-product")
    suspend fun addProduct(
        @Header("Authorization") token: String, @Body request: AddProductRequest
    ): AddProductResponse

    @POST("api/farmers/products/update")
    suspend fun updateProduct(
        @Header("Authorization") token: String, @Body request: UpdateProductRequest
    ): UpdateProductResponse

    @POST("api/farmers/products/toggle-sold-out")
    suspend fun toggleSoldOut(
        @Header("Authorization") token: String, @Body request: ToggleSoldOutRequest
    ): ToggleSoldOutResponse

    @POST("api/farmers/products/delete")
    suspend fun deleteProduct(
        @Header("Authorization") token: String, @Body request: DeleteProductRequest
    ): DeleteProductResponse

    @GET("api/farmers/search")
    suspend fun searchProducts(
        @Header("Authorization") token: String,
        @Query("query") query: String
    ): SearchProductResponse

    @GET("api/farmers/seller/{sellerId}")
    suspend fun getSellerDetails(
        @Header("Authorization") token: String,
        @Path("sellerId") sellerId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("minPrice") minPrice: String? = null,
        @Query("maxPrice") maxPrice: String? = null,
        @Query("sort") sort: String? = null
    ): SellerDetailsResponse

    @POST("api/farmers/rate-product")
    suspend fun rateProduct(
        @Header("Authorization") token: String,
        @Body request: RateProductRequest
    ): RateProductResponse

    @POST("api/farmers/rate-product/react")
    suspend fun reactToReview(
        @Header("Authorization") token: String,
        @Body request: ReviewReactionRequest
    ): ReviewReactionResponse
}
