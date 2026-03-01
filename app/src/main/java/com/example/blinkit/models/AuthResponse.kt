package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Authentication response model
 * Returned after successful login/signup
 */
data class AuthResponse(
    @SerializedName("userId")
    val userId: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("phone")
    val phone: String? = null,
    
    @SerializedName("token")
    val token: String,
    
    @SerializedName("profile_image")
    val profileImage: String? = null
)

/**
 * Login request model
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)

/**
 * Signup request model
 */
data class SignupRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("phone")
    val phone: String? = null
)

/**
 * Update profile request model
 */
data class UpdateProfileRequest(
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("phone")
    val phone: String? = null
)