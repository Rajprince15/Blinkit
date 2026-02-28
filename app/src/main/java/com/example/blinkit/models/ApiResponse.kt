package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Generic API Response wrapper
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: T?,

    @SerializedName("count")
    val count: Int?,

    @SerializedName("total")
    val total: Int?,

    @SerializedName("page")
    val page: Int?,

    @SerializedName("totalPages")
    val totalPages: Int?
)
