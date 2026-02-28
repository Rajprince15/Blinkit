package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * User data model
 */
data class User(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String?,

    @SerializedName("profile_image")
    val profileImage: String?,

    @SerializedName("created_at")
    val createdAt: String?
)
