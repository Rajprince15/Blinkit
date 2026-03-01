package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Product model representing grocery items
 */
data class Product(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("category_id")
    val categoryId: Int,
    
    @SerializedName("category_name")
    val categoryName: String? = null,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("brand")
    val brand: String? = null,
    
    @SerializedName("image_url")
    val imageUrl: String? = null,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("original_price")
    val originalPrice: Double? = null,
    
    @SerializedName("discount_percentage")
    val discountPercentage: Int = 0,
    
    @SerializedName("unit")
    val unit: String = "piece",
    
    @SerializedName("stock_quantity")
    val stockQuantity: Int = 0,
    
    @SerializedName("min_order_quantity")
    val minOrderQuantity: Int = 1,
    
    @SerializedName("max_order_quantity")
    val maxOrderQuantity: Int = 10,
    
    @SerializedName("rating")
    val rating: Double = 0.0,
    
    @SerializedName("total_reviews")
    val totalReviews: Int = 0,
    
    @SerializedName("is_featured")
    val isFeatured: Boolean = false,
    
    @SerializedName("is_available")
    val isAvailable: Boolean = true,
    
    @SerializedName("created_at")
    val createdAt: String? = null
) {
    /**
     * Check if product has discount
     */
    fun hasDiscount(): Boolean {
        return originalPrice != null && originalPrice > price
    }
    
    /**
     * Get discount amount
     */
    fun getDiscountAmount(): Double {
        return if (hasDiscount()) {
            originalPrice!! - price
        } else {
            0.0
        }
    }
    
    /**
     * Check if product is in stock
     */
    fun isInStock(): Boolean {
        return stockQuantity > 0 && isAvailable
    }
    
    /**
     * Get formatted price
     */
    fun getFormattedPrice(): String {
        return "₹${String.format("%.2f", price)}"
    }
    
    /**
     * Get formatted original price
     */
    fun getFormattedOriginalPrice(): String? {
        return originalPrice?.let { "₹${String.format("%.2f", it)}" }
    }
}