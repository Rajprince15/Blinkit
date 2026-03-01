package com.example.blinkit.repositories

import com.example.blinkit.api.ApiClient
import com.example.blinkit.models.*
/**
 * Repository for cart operations
 * Auth token is automatically injected by ApiClient interceptor
 */
class CartRepository {
    private val api = ApiClient.apiService

    suspend fun getCart() = api.getCart()
    suspend fun addToCart(request: AddToCartRequest) = api.addToCart(request)
    suspend fun updateCartItem(cartItemId: Int, request: UpdateCartRequest) = api.updateCartItem(cartItemId, request)
    suspend fun removeCartItem(cartItemId: Int) = api.removeCartItem(cartItemId)
    suspend fun clearCart() = api.clearCart()
}
