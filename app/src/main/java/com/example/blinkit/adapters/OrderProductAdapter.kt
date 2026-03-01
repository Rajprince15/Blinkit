package com.example.blinkit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blinkit.R
import com.example.blinkit.databinding.ItemOrderProductBinding
import com.example.blinkit.models.OrderItem

class OrderProductAdapter : ListAdapter<OrderItem, OrderProductAdapter.OrderProductViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductViewHolder {
        val binding = ItemOrderProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderProductViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: OrderProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class OrderProductViewHolder(
        private val binding: ItemOrderProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(orderItem: OrderItem) {
            binding.tvProductName.text = orderItem.productName
            binding.tvProductQuantity.text = "Qty: ${orderItem.quantity}"
            binding.tvProductPrice.text = orderItem.getFormattedTotalPrice()
            
            // Load product image
            Glide.with(binding.root.context)
                .load(orderItem.productImage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(binding.ivProductImage)
        }
    }
    
    class DiffCallback : DiffUtil.ItemCallback<OrderItem>() {
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem == newItem
        }
    }
}
