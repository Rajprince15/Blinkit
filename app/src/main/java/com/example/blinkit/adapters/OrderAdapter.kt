package com.example.blinkit.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blinkit.databinding.ItemOrderBinding
import com.example.blinkit.models.Order
import com.example.blinkit.models.OrderStatus
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter(
    private val onOrderClick: (Order) -> Unit
) : ListAdapter<Order, OrderAdapter.OrderViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding, onOrderClick)
    }
    
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class OrderViewHolder(
        private val binding: ItemOrderBinding,
        private val onOrderClick: (Order) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(order: Order) {
            binding.tvOrderNumber.text = "Order ${order.orderNumber}"
            binding.tvOrderDate.text = formatDate(order.orderDate)
            binding.tvTotalAmount.text = order.getFormattedTotalAmount()
            
            // Calculate item count
            val itemCount = order.items?.sumOf { it.quantity } ?: 0
            binding.tvItemCount.text = "$itemCount items"
            
            // Set status badge
            binding.chipStatus.text = getStatusDisplayName(order.orderStatus)
            binding.chipStatus.setChipBackgroundColorResource(getStatusColor(order.orderStatus))
            
            // Set click listener
            binding.root.setOnClickListener {
                onOrderClick(order)
            }
            
            binding.btnTrackOrder.setOnClickListener {
                onOrderClick(order)
            }
        }
        
        private fun getStatusDisplayName(status: OrderStatus): String {
            return when (status) {
                OrderStatus.PLACED -> "Placed"
                OrderStatus.CONFIRMED -> "Confirmed"
                OrderStatus.PACKED -> "Packed"
                OrderStatus.OUT_FOR_DELIVERY -> "Out for Delivery"
                OrderStatus.DELIVERED -> "Delivered"
                OrderStatus.CANCELLED -> "Cancelled"
            }
        }
        
        private fun getStatusColor(status: OrderStatus): Int {
            return when (status) {
                OrderStatus.PLACED -> android.R.color.holo_blue_light
                OrderStatus.CONFIRMED -> android.R.color.holo_green_light
                OrderStatus.PACKED -> android.R.color.holo_orange_light
                OrderStatus.OUT_FOR_DELIVERY -> android.R.color.holo_purple
                OrderStatus.DELIVERED -> android.R.color.holo_green_dark
                OrderStatus.CANCELLED -> android.R.color.holo_red_light
            }
        }
        
        private fun formatDate(dateString: String?): String {
            if (dateString == null) return ""
            
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                val date = inputFormat.parse(dateString)
                date?.let { outputFormat.format(it) } ?: dateString
            } catch (e: Exception) {
                dateString
            }
        }
    }
    
    class DiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}
