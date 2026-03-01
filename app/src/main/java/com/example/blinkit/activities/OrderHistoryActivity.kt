package com.example.blinkit.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blinkit.databinding.ActivityOrderHistoryBinding
import com.example.blinkit.adapters.OrderAdapter
import com.example.blinkit.models.Order
import com.example.blinkit.viewmodels.OrderViewModel
import com.google.android.material.tabs.TabLayout

class OrderHistoryActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityOrderHistoryBinding
    private lateinit var viewModel: OrderViewModel
    private lateinit var orderAdapter: OrderAdapter
    
    private var currentFilter: OrderFilter = OrderFilter.ACTIVE
    
    enum class OrderFilter {
        ACTIVE, PAST
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupRecyclerView()
        setupViewModel()
        setupTabLayout()
        setupClickListeners()
        
        loadOrders()
    }
    
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter { order ->
            // Navigate to order tracking
            val intent = Intent(this, OrderTrackingActivity::class.java)
            intent.putExtra("ORDER_ID", order.id)
            startActivity(intent)
        }
        
        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(this@OrderHistoryActivity)
            adapter = orderAdapter
        }
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        
        // Observe orders
        viewModel.orders.observe(this) { orders ->
            orders?.let {
                filterAndDisplayOrders(it)
            }
        }
        
        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.swipeRefresh.isRefreshing = isLoading
        }
        
        // Observe error
        viewModel.error.observe(this) { error ->
            error?.let {
                showError(it)
            }
        }
    }
    
    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentFilter = when (tab?.position) {
                    0 -> OrderFilter.ACTIVE
                    1 -> OrderFilter.PAST
                    else -> OrderFilter.ACTIVE
                }
                
                // Refilter orders
                viewModel.orders.value?.let {
                    filterAndDisplayOrders(it)
                }
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    
    private fun setupClickListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            loadOrders()
        }
        
        binding.btnStartShopping.setOnClickListener {
            finish()
        }
        
        binding.btnRetry.setOnClickListener {
            loadOrders()
        }
    }
    
    private fun loadOrders() {
        binding.errorContainer.visibility = View.GONE
        binding.emptyStateContainer.visibility = View.GONE
        viewModel.getOrders()
    }
    
    private fun filterAndDisplayOrders(orders: List<Order>) {
        val filteredOrders = when (currentFilter) {
            OrderFilter.ACTIVE -> orders.filter { it.isActive() }
            OrderFilter.PAST -> orders.filter { !it.isActive() }
        }
        
        if (filteredOrders.isEmpty()) {
            showEmptyState()
        } else {
            binding.errorContainer.visibility = View.GONE
            binding.emptyStateContainer.visibility = View.GONE
            binding.rvOrders.visibility = View.VISIBLE
            orderAdapter.submitList(filteredOrders)
        }
    }
    
    private fun showEmptyState() {
        binding.rvOrders.visibility = View.GONE
        binding.errorContainer.visibility = View.GONE
        binding.emptyStateContainer.visibility = View.VISIBLE
        
        val message = when (currentFilter) {
            OrderFilter.ACTIVE -> "No active orders"
            OrderFilter.PAST -> "No past orders"
        }
        binding.tvEmptyMessage.text = message
    }
    
    private fun showError(message: String) {
        binding.rvOrders.visibility = View.GONE
        binding.emptyStateContainer.visibility = View.GONE
        binding.errorContainer.visibility = View.VISIBLE
        binding.tvError.text = message
    }
}
