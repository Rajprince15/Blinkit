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

    @SerializedName("product_name")
    val productName: String,
    
    @SerializedName("product_image")
    val productImage: String? = null,
    
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
/**
 * Response when order is created
 */
data class OrderCreationResponse(
    @SerializedName("order_id")
    val orderId: Int? = null,
    
    @SerializedName("orderId")
    val orderIdAlt: Int? = null,
    
    @SerializedName("order_number")
    val orderNumber: String,
    
    @SerializedName("message")
    val message: String? = null
) {
    /**
     * Get order ID from either format
     */
    fun getOrderId(): Int {
        return orderId ?: orderIdAlt ?: 0
    }
}