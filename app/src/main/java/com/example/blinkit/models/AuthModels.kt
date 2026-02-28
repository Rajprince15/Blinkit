package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Login Request model
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

/**
 * Signup Request model
 */
data class SignupRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("phone")
    val phone: String?
)

/**
 * Auth Response model
 */
data class AuthResponse(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String?,

    @SerializedName("token")
    val token: String
)
