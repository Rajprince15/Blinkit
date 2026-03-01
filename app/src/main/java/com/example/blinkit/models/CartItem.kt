package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * CartItem model representing items in shopping cart
 */
data class CartItem(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("user_id")
    val userId: Int,
    
    @SerializedName("product_id")
    val productId: Int,
    
    @SerializedName("quantity")
    var quantity: Int,
    
    @SerializedName("product")
    val product: Product? = null,
    
    @SerializedName("added_at")
    val addedAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null
) {
    /**
     * Calculate total price for this cart item
     */
    fun getTotalPrice(): Double {
        return product?.price?.times(quantity) ?: 0.0
    }
    
    /**
     * Get formatted total price
     */
    fun getFormattedTotalPrice(): String {
        return "₹${String.format("%.2f", getTotalPrice())}"
    }
}