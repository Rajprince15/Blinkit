package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Create order request model
 */
data class CreateOrderRequest(
    @SerializedName("address_id")
    val addressId: Int,
    
    @SerializedName("delivery_address")
    val deliveryAddress: String,
    
    @SerializedName("payment_method")
    val paymentMethod: String = "COD",
    
    @SerializedName("items")
    val items: List<OrderItemRequest>,
    
    @SerializedName("total_amount")
    val totalAmount: Double
)

/**
 * Order item in create request
 */
data class OrderItemRequest(
    @SerializedName("product_id")
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