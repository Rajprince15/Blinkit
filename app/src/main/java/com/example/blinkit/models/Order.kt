package com.example.blinkit.models

import com.google.gson.annotations.SerializedName

/**
 * Order model representing customer orders
 */
data class Order(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("order_number")
    val orderNumber: String,
    
    @SerializedName("user_id")
    val userId: Int,
    
    @SerializedName("address_id")
    val addressId: Int,
    
    @SerializedName("delivery_address")
    val deliveryAddress: String,
    
    @SerializedName("payment_method")
    val paymentMethod: PaymentMethod,
    
    @SerializedName("subtotal")
    val subtotal: Double,
    
    @SerializedName("delivery_fee")
    val deliveryFee: Double = 0.0,
    
    @SerializedName("discount_amount")
    val discountAmount: Double = 0.0,
    
    @SerializedName("total_amount")
    val totalAmount: Double,
    
    @SerializedName("order_status")
    val orderStatus: OrderStatus,
    
    @SerializedName("order_date")
    val orderDate: String,
    
    @SerializedName("estimated_delivery")
    val estimatedDelivery: String? = null,
    
    @SerializedName("delivered_at")
    val deliveredAt: String? = null,
    
    @SerializedName("cancelled_at")
    val cancelledAt: String? = null,
    
    @SerializedName("cancellation_reason")
    val cancellationReason: String? = null,
    
    @SerializedName("items")
    val items: List<OrderItem>? = null
) {
    /**
     * Check if order is active
     */
    fun isActive(): Boolean {
        return orderStatus != OrderStatus.DELIVERED && orderStatus != OrderStatus.CANCELLED
    }
    
    /**
     * Check if order can be cancelled
     */
    fun canBeCancelled(): Boolean {
        return orderStatus == OrderStatus.PLACED || orderStatus == OrderStatus.CONFIRMED
    }
    
    /**
     * Get status color for UI
     */
    fun getStatusColor(): Int {
        return when (orderStatus) {
            OrderStatus.PLACED -> android.graphics.Color.parseColor("#2196F3")
            OrderStatus.CONFIRMED -> android.graphics.Color.parseColor("#4CAF50")
            OrderStatus.PACKED -> android.graphics.Color.parseColor("#FF9800")
            OrderStatus.OUT_FOR_DELIVERY -> android.graphics.Color.parseColor("#9C27B0")
            OrderStatus.DELIVERED -> android.graphics.Color.parseColor("#4CAF50")
            OrderStatus.CANCELLED -> android.graphics.Color.parseColor("#F44336")
        }
    }
    
    /**
     * Get formatted total amount
     */
    fun getFormattedTotalAmount(): String {
        return "₹${String.format("%.2f", totalAmount)}"
    }
}

/**
 * Wrapper for getOrderById API response which returns {order, items}
 */
data class OrderWithItems(
    @SerializedName("order")
    val order: Order,
    
    @SerializedName("items")
    val items: List<OrderItem>
)

/**
 * Enum for order status
 */
enum class OrderStatus {
    @SerializedName("PLACED")
    PLACED,
    
    @SerializedName("CONFIRMED")
    CONFIRMED,
    
    @SerializedName("PACKED")
    PACKED,
    
    @SerializedName("OUT_FOR_DELIVERY")
    OUT_FOR_DELIVERY,
    
    @SerializedName("DELIVERED")
    DELIVERED,
    
    @SerializedName("CANCELLED")
    CANCELLED
}

/**
 * Enum for payment methods
 */
enum class PaymentMethod {
    @SerializedName("COD")
    COD,
    
    @SerializedName("CARD")
    CARD,
    
    @SerializedName("UPI")
    UPI,
    
    @SerializedName("WALLET")
    WALLET
}