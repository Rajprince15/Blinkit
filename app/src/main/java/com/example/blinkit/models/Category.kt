package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Category data model
 */
data class Category(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("display_order")
    val displayOrder: Int
)
