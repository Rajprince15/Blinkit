package com.example.blinkit.repositories

import com.example.blinkit.api.ApiClient
import com.example.blinkit.models.*

class CartRepository {
    private val api = ApiClient.apiService

    suspend fun getCart(token: String) = api.getCart("Bearer $token")
    suspend fun addToCart(token: String, request: AddToCartRequest) = api.addToCart("Bearer $token", request)
    suspend fun updateCartItem(token: String, cartItemId: Int, request: UpdateCartRequest) = api.updateCartItem("Bearer $token", cartItemId, request)
    suspend fun removeCartItem(token: String, cartItemId: Int) = api.removeCartItem("Bearer $token", cartItemId)
    suspend fun clearCart(token: String) = api.clearCart("Bearer $token")
}
