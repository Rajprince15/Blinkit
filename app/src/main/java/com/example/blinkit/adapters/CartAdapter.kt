package com.example.blinkit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blinkit.R
import com.example.blinkit.models.CartItem
import com.bumptech.glide.Glide

class CartAdapter(
    private val items: MutableList<CartItem> = mutableListOf(),
    private val onQuantityChanged: (item: CartItem, newQty: Int) -> Unit,
    private val onRemove: (item: CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    fun updateData(newItems: List<CartItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivImage: ImageView = view.findViewById(R.id.ivProductImage)
        private val tvName: TextView = view.findViewById(R.id.tvProductName)
        private val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        private val tvQty: TextView = view.findViewById(R.id.tvQuantity)
        private val btnIncrease: View = view.findViewById(R.id.btnIncrease)
        private val btnDecrease: View = view.findViewById(R.id.btnDecrease)
        private val btnRemove: View = view.findViewById(R.id.btnRemove)

        fun bind(item: CartItem) {
            tvName.text = item.productName
            tvPrice.text = "₹${item.price}"
            tvQty.text = item.quantity.toString()
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(ivImage)

            btnIncrease.setOnClickListener {
                onQuantityChanged(item, item.quantity + 1)
            }
            btnDecrease.setOnClickListener {
                if (item.quantity > 1) {
                    onQuantityChanged(item, item.quantity - 1)
                }
            }
            btnRemove.setOnClickListener {
                onRemove(item)
            }
        }
    }
}