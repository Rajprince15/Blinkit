package com.example.blinkit.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blinkit.R
import com.example.blinkit.adapters.CartAdapter
import com.example.blinkit.databinding.ActivityCartBinding
import com.example.blinkit.models.CartItem
import com.example.blinkit.models.UpdateCartRequest
import com.example.blinkit.utils.SharedPrefsManager
import com.example.blinkit.viewmodels.CartViewModel

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]

        setupRecycler()
        observeViewModel()

        loadCart()

        binding.btnCheckout.setOnClickListener {
            Toast.makeText(this, "Proceed to checkout (not implemented)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecycler() {
        cartAdapter = CartAdapter(mutableListOf(), onQuantityChanged = { item, qty ->
            cartViewModel.updateCartItem(getToken(), item.id, qty)
        }, onRemove = { item ->
            cartViewModel.removeCartItem(getToken(), item.id)
        })
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }
    }

    private fun observeViewModel() {
        cartViewModel.cartSummary.observe(this) { result ->
            result.onSuccess { summary ->
                cartAdapter.updateData(summary.items)
                binding.tvSubtotal.text = "₹${summary.subtotal}"
                binding.tvTotal.text = "₹${summary.total}"
                binding.tvEmpty.visibility = if (summary.items.isEmpty()) View.VISIBLE else View.GONE
            }
            result.onFailure { error ->
                Toast.makeText(this, error.message ?: "Failed to load cart", Toast.LENGTH_LONG).show()
            }
        }
        cartViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun loadCart() {
        cartViewModel.loadCart(getToken())
    }

    private fun getToken(): String {
        return SharedPrefsManager.getToken(this) ?: ""
    }
}