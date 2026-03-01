package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * OrderItem model representing individual items in an order
 */
data class OrderItem(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("order_id")
    val orderId: Int,
    
    @SerializedName("product_id")
    val productId: Int,
    
    @SerializedName("product_name")
    val productName: String,
    
    @SerializedName("product_image")
    val productImage: String? = null,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("unit_price")
    val unitPrice: Double,
    
    @SerializedName("total_price")
    val totalPrice: Double
) {
    /**
     * Get formatted unit price
     */
    fun getFormattedUnitPrice(): String {
        return "₹${String.format("%.2f", unitPrice)}"
    }
    
    /**
     * Get formatted total price
     */
    fun getFormattedTotalPrice(): String {
        return "₹${String.format("%.2f", totalPrice)}"
    }
}