package com.example.blinkit.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blinkit.R
import com.example.blinkit.adapters.OrderTimelineAdapter
import com.example.blinkit.adapters.OrderProductAdapter
import com.example.blinkit.databinding.ActivityOrderTrackingBinding
import com.example.blinkit.viewmodels.OrderViewModel
import java.text.SimpleDateFormat
import java.util.*

class OrderTrackingActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityOrderTrackingBinding
    private lateinit var viewModel: OrderViewModel
    private lateinit var timelineAdapter: OrderTimelineAdapter
    private lateinit var productAdapter: OrderProductAdapter
    
    private var orderId: Int = 0
    private val autoRefreshHandler = Handler(Looper.getMainLooper())
    private val autoRefreshInterval = 30000L // 30 seconds
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Get order ID from intent
        orderId = intent.getIntExtra("ORDER_ID", 0)
        
        if (orderId == 0) {
            Toast.makeText(this, "Invalid order", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        setupToolbar()
        setupRecyclerViews()
        setupViewModel()
        setupClickListeners()
        
        loadOrderDetails()
        startAutoRefresh()
    }
    
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupRecyclerViews() {
        // Timeline RecyclerView
        timelineAdapter = OrderTimelineAdapter()
        binding.rvTimeline.apply {
            layoutManager = LinearLayoutManager(this@OrderTrackingActivity)
            adapter = timelineAdapter
        }
        
        // Order Items RecyclerView
        productAdapter = OrderProductAdapter()
        binding.rvOrderItems.apply {
            layoutManager = LinearLayoutManager(this@OrderTrackingActivity)
            adapter = productAdapter
        }
    }
    
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[OrderViewModel::class.java]
        
        // Observe order details
        viewModel.currentOrder.observe(this) { order ->
            order?.let {
                displayOrderDetails(it)
                binding.btnCancelOrder.visibility = if (it.canBeCancelled()) View.VISIBLE else View.GONE
            }
        }
        
        // Observe order timeline
        viewModel.orderTimeline.observe(this) { timeline ->
            timeline?.let {
                timelineAdapter.submitList(it)
            }
        }
        
        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        // Observe error
        viewModel.error.observe(this) { error ->
            error?.let {
                showError(it)
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.btnCancelOrder.setOnClickListener {
            showCancelOrderDialog()
        }
        
        binding.btnNeedHelp.setOnClickListener {
            // TODO: Implement help/support functionality
            Toast.makeText(this, "Contact Support: support@blinkit.com", Toast.LENGTH_LONG).show()
        }
        
        binding.btnRetry.setOnClickListener {
            loadOrderDetails()
        }
    }
    
    private fun loadOrderDetails() {
        binding.errorContainer.visibility = View.GONE
        binding.contentContainer.visibility = View.GONE
        
        // Load order details
        viewModel.getOrderById(orderId)
        
        // Load order timeline
        viewModel.trackOrder(orderId)
    }
    
    private fun displayOrderDetails(order: com.example.blinkit.models.Order) {
        binding.contentContainer.visibility = View.VISIBLE
        binding.errorContainer.visibility = View.GONE
        
        // Order info
        binding.tvOrderNumber.text = "Order ${order.orderNumber}"
        binding.tvOrderDate.text = "Placed on ${formatDate(order.orderDate)}"
        
        // Estimated delivery
        val estimatedText = if (order.estimatedDelivery != null) {
            "Estimated Delivery: ${formatDate(order.estimatedDelivery)}"
        } else {
            "Estimated Delivery: Within 10 minutes"
        }
        binding.tvEstimatedDelivery.text = estimatedText
        
        // Order items
        order.items?.let {
            productAdapter.submitList(it)
        }
        
        // Delivery address
        binding.tvDeliveryAddress.text = order.deliveryAddress
        
        // Price details
        binding.tvSubtotal.text = "₹${String.format("%.2f", order.subtotal)}"
        binding.tvDeliveryFee.text = "₹${String.format("%.2f", order.deliveryFee)}"
        binding.tvTotalAmount.text = "₹${String.format("%.2f", order.totalAmount)}"
    }
    
    private fun showCancelOrderDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cancel Order")
            .setMessage("Are you sure you want to cancel this order?")
            .setPositiveButton("Yes") { _, _ ->
                cancelOrder()
            }
            .setNegativeButton("No", null)
            .show()
    }
    
    private fun cancelOrder() {
        // TODO: Implement cancel order API call
        Toast.makeText(this, "Order cancellation requested", Toast.LENGTH_SHORT).show()
    }
    
    private fun showError(message: String) {
        binding.contentContainer.visibility = View.GONE
        binding.errorContainer.visibility = View.VISIBLE
        binding.tvError.text = message
    }
    
    private fun formatDate(dateString: String?): String {
        if (dateString == null) return ""
        
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }
    
    private fun startAutoRefresh() {
        autoRefreshHandler.postDelayed(object : Runnable {
            override fun run() {
                // Refresh order status
                viewModel.trackOrder(orderId)
                autoRefreshHandler.postDelayed(this, autoRefreshInterval)
            }
        }, autoRefreshInterval)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        autoRefreshHandler.removeCallbacksAndMessages(null)
    }
}
