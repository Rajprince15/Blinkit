package com.example.blinkit.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blinkit.R
import com.example.blinkit.databinding.ItemOrderTimelineBinding
import com.example.blinkit.models.OrderStatus
import com.example.blinkit.models.OrderStatusHistory
import java.text.SimpleDateFormat
import java.util.*

class OrderTimelineAdapter : ListAdapter<OrderStatusHistory, OrderTimelineAdapter.TimelineViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val binding = ItemOrderTimelineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimelineViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(getItem(position), position == itemCount - 1)
    }
    
    class TimelineViewHolder(
        private val binding: ItemOrderTimelineBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(statusHistory: OrderStatusHistory, isLast: Boolean) {
            // Set status title
            binding.tvStatusTitle.text = getStatusDisplayName(statusHistory.status)
            
            // Set remarks
            binding.tvStatusRemarks.text = statusHistory.remarks ?: ""
            binding.tvStatusRemarks.visibility = if (statusHistory.remarks.isNullOrEmpty()) View.GONE else View.VISIBLE
            
            // Set time
            binding.tvStatusTime.text = formatDate(statusHistory.createdAt)
            
            // Set icon and color based on status
            val (iconRes, colorInt) = getStatusIconAndColor(statusHistory.status)
            binding.ivStatusIcon.setImageResource(iconRes)
            binding.ivStatusIcon.setColorFilter(colorInt)
            
            // Hide connecting line for last item
            binding.vConnectingLine.visibility = if (isLast) View.GONE else View.VISIBLE
            binding.vConnectingLine.setBackgroundColor(colorInt)
        }
        
        private fun getStatusDisplayName(status: OrderStatus): String {
            return when (status) {
                OrderStatus.PLACED -> "Order Placed"
                OrderStatus.CONFIRMED -> "Order Confirmed"
                OrderStatus.PACKED -> "Order Packed"
                OrderStatus.OUT_FOR_DELIVERY -> "Out for Delivery"
                OrderStatus.DELIVERED -> "Delivered"
                OrderStatus.CANCELLED -> "Cancelled"
            }
        }
        
        private fun getStatusIconAndColor(status: OrderStatus): Pair<Int, Int> {
            return when (status) {
                OrderStatus.PLACED -> Pair(R.drawable.ic_check_circle, Color.parseColor("#2196F3"))
                OrderStatus.CONFIRMED -> Pair(R.drawable.ic_check_circle, Color.parseColor("#4CAF50"))
                OrderStatus.PACKED -> Pair(R.drawable.ic_check_circle, Color.parseColor("#FF9800"))
                OrderStatus.OUT_FOR_DELIVERY -> Pair(R.drawable.ic_delivery, Color.parseColor("#9C27B0"))
                OrderStatus.DELIVERED -> Pair(R.drawable.ic_check_circle, Color.parseColor("#4CAF50"))
                OrderStatus.CANCELLED -> Pair(R.drawable.ic_cancel, Color.parseColor("#F44336"))
            }
        }
        
        private fun formatDate(dateString: String?): String {
            if (dateString == null) return ""
            
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
                val date = inputFormat.parse(dateString)
                date?.let { outputFormat.format(it) } ?: dateString
            } catch (e: Exception) {
                dateString
            }
        }
    }
    
    class DiffCallback : DiffUtil.ItemCallback<OrderStatusHistory>() {
        override fun areItemsTheSame(oldItem: OrderStatusHistory, newItem: OrderStatusHistory): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: OrderStatusHistory, newItem: OrderStatusHistory): Boolean {
            return oldItem == newItem
        }
    }
}
