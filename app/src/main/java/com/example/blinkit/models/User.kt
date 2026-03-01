package com.example.blinkit.models

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * User model representing user account information
 */
data class User(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("phone")
    val phone: String? = null,
    
    @SerializedName("profile_image")
    val profileImage: String? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("last_login")
    val lastLogin: String? = null
)