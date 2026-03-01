package com.example.blinkit.repositories

import com.example.blinkit.api.ApiClient
import com.example.blinkit.models.*

/**
 * Repository for order operations
 * Auth token is automatically injected by ApiClient interceptor
 */

class OrderRepository {
    private val api = ApiClient.apiService

    suspend fun createOrder(request: CreateOrderRequest) = api.createOrder(request)
    suspend fun getOrders() = api.getOrders()
    suspend fun getOrderById(orderId: Int) = api.getOrderById(orderId)
    suspend fun trackOrder(orderId: Int) = api.trackOrder(orderId)
}
