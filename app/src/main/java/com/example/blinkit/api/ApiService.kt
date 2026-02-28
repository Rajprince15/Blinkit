package com.example.blinkit.api

import com.example.blinkit.models.*
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service interface defining all API endpoints
 */
interface ApiService {

    // ============ Authentication APIs ============

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>

    @GET("auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ApiResponse<User>>

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<ApiResponse<Any>>

    // ============ Category APIs ============

    @GET("categories")
    suspend fun getCategories(): Response<ApiResponse<List<Category>>>

    @GET("categories/{categoryId}")
    suspend fun getCategoryById(@Path("categoryId") categoryId: Int): Response<ApiResponse<Category>>

    // ============ Product APIs ============

    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<List<Product>>>

    @GET("products/featured")
    suspend fun getFeaturedProducts(
        @Query("limit") limit: Int = 10
    ): Response<ApiResponse<List<Product>>>

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String
    ): Response<ApiResponse<List<Product>>>

    @GET("products/category/{categoryId}")
    suspend fun getProductsByCategory(
        @Path("categoryId") categoryId: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ApiResponse<List<Product>>>

    @GET("products/{productId}")
    suspend fun getProductById(
        @Path("productId") productId: Int
    ): Response<ApiResponse<Product>>
}
