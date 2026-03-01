package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Create order request model
 */
data class CreateOrderRequest(
    @SerializedName("addressId")
    val addressId: Int,
    
    @SerializedName("deliveryAddress")
    val deliveryAddress: String,
    
    @SerializedName("paymentMethod")
    val paymentMethod: String = "COD",
    
    @SerializedName("items")
    val items: List<OrderItemRequest>,
    
    @SerializedName("totalAmount")
    val totalAmount: Double
)

/**
 * Order item in create request
 */
data class OrderItemRequest(
    @SerializedName("productId")
    val productId: Int,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("price")
    val price: Double
)

/**
 * Cancel order request
 */
data class CancelOrderRequest(
    @SerializedName("cancellation_reason")
    val cancellationReason: String
)