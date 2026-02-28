package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Product data model
 */
data class Product(
    @SerializedName("id")
    val id: Int,

    @SerializedName("category_id")
    val categoryId: Int,

    @SerializedName("category_name")
    val categoryName: String?,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("brand")
    val brand: String?,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("price")
    val price: Double,

    @SerializedName("original_price")
    val originalPrice: Double?,

    @SerializedName("discount_percentage")
    val discountPercentage: Int,

    @SerializedName("unit")
    val unit: String,

    @SerializedName("stock_quantity")
    val stockQuantity: Int,

    @SerializedName("min_order_quantity")
    val minOrderQuantity: Int,

    @SerializedName("max_order_quantity")
    val maxOrderQuantity: Int,

    @SerializedName("rating")
    val rating: Float,

    @SerializedName("total_reviews")
    val totalReviews: Int,

    @SerializedName("is_featured")
    val isFeatured: Boolean,

    @SerializedName("is_available")
    val isAvailable: Boolean
)
