package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.AddProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddProductResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddToCartRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddToCartResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.DeleteProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.DeleteProductResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.GetCartResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.MixedProductsResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductDetailsResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductDetailedResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductResponseDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.RemoveFromCartRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.RemoveFromCartResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.ToggleSoldOutRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.ToggleSoldOutResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateProductRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateProductResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateQuantityRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateQuantityResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
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
        @Query("subcategory") subcategory: String? = null
    ): ProductResponseDto

    @GET("api/farmers/smart-farming-products")
    suspend fun getSmartFarmingProducts(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("category") category: String,
        @Query("subcategory") subcategory: String? = null
    ): ProductResponseDto

     @GET("api/farmers/mixed-products")
     suspend fun getMixedProducts(
         @Header("Authorization") token: String,
         @Query("screen") screen: String? = null
     ): MixedProductsResponse

     @GET("api/farmers/products/{productId}")
     suspend fun getProductDetails(
         @Header("Authorization") token: String,
         @Path("productId") productId: String
     ): ProductDetailsResponse

      @GET("api/farmers/products/{productId}/detailed")
      suspend fun getProductDetailsDetailed(
          @Header("Authorization") token: String,
          @Path("productId") productId: String
      ): ProductDetailedResponse

     @POST("api/farmers/add-product")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Body request: AddProductRequest
    ): AddProductResponse

    @POST("api/farmers/products/update")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Body request: UpdateProductRequest
    ): UpdateProductResponse

    @POST("api/farmers/products/toggle-sold-out")
    suspend fun toggleSoldOut(
        @Header("Authorization") token: String,
        @Body request: ToggleSoldOutRequest
    ): ToggleSoldOutResponse

    @POST("api/farmers/products/delete")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Body request: DeleteProductRequest
    ): DeleteProductResponse

    @POST("api/cart/add")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body request: AddToCartRequest
    ): AddToCartResponse

    @GET("api/cart/")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): GetCartResponse

     @PATCH("api/cart/update-quantity")
     suspend fun updateQuantity(
         @Header("Authorization") token: String,
         @Body request: UpdateQuantityRequest
     ): UpdateQuantityResponse

     @POST("api/cart/remove")
     suspend fun removeFromCart(
         @Header("Authorization") token: String,
         @Body request: RemoveFromCartRequest
     ): RemoveFromCartResponse
}
