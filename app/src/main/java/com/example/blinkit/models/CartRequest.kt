package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Request models for cart operations
 */

/**
 * Add to cart request
 */
data class AddToCartRequest(
    @SerializedName("product_id")
    val productId: Int,
    
    @SerializedName("quantity")
    val quantity: Int = 1
)

/**
 * Update cart item quantity request
 */
data class UpdateCartRequest(
    @SerializedName("quantity")
    val quantity: Int
)

/**
 * Cart summary response
 */
data class CartSummary(
    @SerializedName("items")
    val items: List<CartItem>,
    
    @SerializedName("subtotal")
    val subtotal: Double,
    
    @SerializedName("delivery_fee")
    val deliveryFee: Double,
    
    @SerializedName("total_amount")
    val total: Double,
    
    @SerializedName("item_count")
    val itemCount: Int
) {
    /**
     * Get formatted subtotal
     */
    fun getFormattedSubtotal(): String {
        return "₹${String.format("%.2f", subtotal)}"
    }
    
    /**
     * Get formatted delivery fee
     */
    fun getFormattedDeliveryFee(): String {
        return "₹${String.format("%.2f", deliveryFee)}"
    }
    
    /**
     * Get formatted total
     */
    fun getFormattedTotal(): String {
        return "₹${String.format("%.2f", total)}"
    }
}
/**
 * Response when item is added to cart
 */
data class CartItemResponse(
    @SerializedName("cart_item_id")
    val cartItemId: Int? = null,
    
    @SerializedName("cartItemId")
    val cartItemIdAlt: Int? = null,
    
    @SerializedName("message")
    val message: String? = null
) {
    /**
     * Get the cart item ID from either format
     */
    fun getId(): Int {
        return cartItemId ?: cartItemIdAlt ?: 0
    }
}