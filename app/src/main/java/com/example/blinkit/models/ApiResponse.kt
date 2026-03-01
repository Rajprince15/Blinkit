package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Generic API response wrapper
 * Used to standardize all API responses from the backend
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("data")
    val data: T? = null,
    
    @SerializedName("count")
    val count: Int? = null,
    
    @SerializedName("total")
    val total: Int? = null,
    
    @SerializedName("page")
    val page: Int? = null,
    
    @SerializedName("totalPages")
    val totalPages: Int? = null
) {
    /**
     * Check if response has data
     */
    fun hasData(): Boolean {
        return data != null
    }
    
    /**
     * Check if response is successful with data
     */
    fun isSuccessWithData(): Boolean {
        return success && hasData()
    }
}

/**
 * Specialized response for error cases
 */
data class ErrorResponse(
    @SerializedName("success")
    val success: Boolean = false,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("error")
    val error: String? = null
)