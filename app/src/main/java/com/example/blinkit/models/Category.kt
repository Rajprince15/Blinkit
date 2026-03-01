package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Category model representing product categories
 */
data class Category(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("image_url")
    val imageUrl: String? = null,
    
    @SerializedName("display_order")
    val displayOrder: Int = 0,
    
    @SerializedName("is_active")
    val isActive: Boolean = true
)