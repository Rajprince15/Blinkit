package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * OrderStatusHistory model for tracking order status changes
 */
data class OrderStatusHistory(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("order_id")
    val orderId: Int,
    
    @SerializedName("status")
    val status: OrderStatus,
    
    @SerializedName("remarks")
    val remarks: String? = null,
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("completed")
    val completed: Boolean = true
)

/**
 * Order tracking timeline response
 */
data class OrderTrackingResponse(
    @SerializedName("orderId")
    val orderId: String,
    
    @SerializedName("currentStatus")
    val currentStatus: OrderStatus,
    
    @SerializedName("estimatedDelivery")
    val estimatedDelivery: String? = null,
    
    @SerializedName("timeline")
    val timeline: List<OrderStatusHistory>
)