package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Address model representing delivery addresses
 */
data class Address(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("user_id")
    val userId: Int,
    
    @SerializedName("full_name")
    val fullName: String,
    
    @SerializedName("phone")
    val phone: String,
    
    @SerializedName("address_line1")
    val addressLine1: String,
    
    @SerializedName("address_line2")
    val addressLine2: String? = null,
    
    @SerializedName("city")
    val city: String,
    
    @SerializedName("state")
    val state: String,
    
    @SerializedName("pincode")
    val pincode: String,
    
    @SerializedName("address_type")
    val addressType: AddressType = AddressType.HOME,
    
    @SerializedName("is_default")
    val isDefault: Boolean = false,
    
    @SerializedName("created_at")
    val createdAt: String? = null
) {
    /**
     * Get full formatted address
     */
    fun getFullAddress(): String {
        return buildString {
            append(addressLine1)
            if (!addressLine2.isNullOrBlank()) {
                append(", ")
                append(addressLine2)
            }
            append(", ")
            append(city)
            append(", ")
            append(state)
            append(" - ")
            append(pincode)
        }
    }
    
    /**
     * Get address type label
     */
    fun getAddressTypeLabel(): String {
        return addressType.name.lowercase().replaceFirstChar { it.uppercase() }
    }
}

/**
 * Enum for address types
 */
enum class AddressType {
    @SerializedName("HOME")
    HOME,
    
    @SerializedName("WORK")
    WORK,
    
    @SerializedName("OTHER")
    OTHER
}