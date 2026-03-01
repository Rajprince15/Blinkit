package com.example.blinkit.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.blinkit.R
import com.example.blinkit.databinding.ItemProductBinding
import com.example.blinkit.models.Product

class ProductAdapter(
    private val onProductClick: (Product) -> Unit,
    private val onAddToCart: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(product: Product) {
            binding.tvProductName.text = product.name
            binding.tvProductBrand.text = product.brand ?: ""
            binding.tvPrice.text = "₹${product.price}"
            binding.tvRating.text = product.rating.toString()
            binding.tvUnit.text = product.unit
            
            // Handle original price and discount
            if (product.originalPrice != null && product.originalPrice > product.price) {
                binding.tvOriginalPrice.visibility = View.VISIBLE
                binding.tvOriginalPrice.text = "₹${product.originalPrice}"
                binding.tvOriginalPrice.paintFlags = binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.tvOriginalPrice.visibility = View.GONE
            }
            
            // Load image with Glide
            Glide.with(binding.root.context)
                .load(product.imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .transform(RoundedCorners(16))
                .into(binding.ivProduct)
            
            // Click listeners
            binding.root.setOnClickListener {
                onProductClick(product)
            }
            
            binding.btnAddToCart.setOnClickListener {
                onAddToCart(product)
            }
        }
    }
    
    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}