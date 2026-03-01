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
    suspend fun getProfile(): Response<ApiResponse<User>>

    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponse<Any>>

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

    // ============ Cart APIs ============

    @GET("cart")
    suspend fun getCart(): Response<ApiResponse<CartSummary>>

    @POST("cart/add")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<ApiResponse<CartItemResponse>>

    @PUT("cart/update/{cartItemId}")
    suspend fun updateCartItem(
        
        @Path("cartItemId") cartItemId: Int,
        @Body request: UpdateCartRequest
    ): Response<ApiResponse<Any>>

    @DELETE("cart/remove/{cartItemId}")
    suspend fun removeCartItem(@Path("cartItemId") cartItemId: Int): Response<ApiResponse<Any>>

    @DELETE("cart/clear")
    suspend fun clearCart(): Response<ApiResponse<Any>>

    // ============ Address APIs ============

    @GET("addresses")
    suspend fun getAddresses(): Response<ApiResponse<List<Address>>>

    @POST("addresses")
    suspend fun addAddress(@Body address: Address): Response<ApiResponse<Any>>

    @PUT("addresses/{addressId}")
    suspend fun updateAddress(
        
        @Path("addressId") addressId: Int,
        @Body address: Address
    ): Response<ApiResponse<Any>>

    @DELETE("addresses/{addressId}")
    suspend fun deleteAddress(@Path("addressId") addressId: Int): Response<ApiResponse<Any>>

    @PUT("addresses/{addressId}/default")
    suspend fun setDefaultAddress(@Path("addressId") addressId: Int): Response<ApiResponse<Any>>

    // ============ Order APIs ============

    @POST("orders/create")
    suspend fun createOrder(@Body request: CreateOrderRequest): Response<ApiResponse<OrderCreationResponse>>

    @GET("orders")
    suspend fun getOrders(): Response<ApiResponse<List<Order>>>  

    @GET("orders/{orderId}")
    suspend fun getOrderById(@Path("orderId") orderId: Int): Response<ApiResponse<OrderWithItems>>

    @GET("orders/{orderId}/track")
    suspend fun trackOrder(@Path("orderId") orderId: Int): Response<ApiResponse<List<OrderStatusHistory>>>
}
