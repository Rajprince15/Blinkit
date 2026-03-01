package com.example.blinkit.repositories

import com.example.blinkit.api.ApiClient
import com.example.blinkit.models.*

class OrderRepository {
    private val api = ApiClient.apiService

    suspend fun createOrder(token: String, request: CreateOrderRequest) = api.createOrder("Bearer $token", request)
    suspend fun getOrders(token: String) = api.getOrders("Bearer $token")
    suspend fun getOrderById(token: String, orderId: Int) = api.getOrderById("Bearer $token", orderId)
    suspend fun trackOrder(token: String, orderId: Int) = api.trackOrder("Bearer $token", orderId)
}
