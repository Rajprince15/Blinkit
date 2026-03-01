package com.example.blinkit.activities
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blinkit.R
import com.example.blinkit.adapters.CategoryAdapter
import com.example.blinkit.adapters.ProductAdapter
import com.example.blinkit.databinding.ActivityHomeBinding
import com.example.blinkit.utils.SharedPrefsManager
import com.example.blinkit.viewmodels.ProductViewModel

class HomeActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityHomeBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        
        setupRecyclerViews()
        setupClickListeners()
        observeViewModel()
        
        // Load data
        productViewModel.loadCategories()
        productViewModel.loadFeaturedProducts()
    }
    
    private fun setupRecyclerViews() {
        // Categories RecyclerView
        categoryAdapter = CategoryAdapter { category ->
            // Navigate to ProductListActivity with categoryId
            val intent = Intent(this, ProductListActivity::class.java).apply {
                putExtra(ProductListActivity.EXTRA_CATEGORY_ID, category.id)
                putExtra(ProductListActivity.EXTRA_CATEGORY_NAME, category.name)
            }
            startActivity(intent)
        }
        
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
        
        // Products RecyclerView
        productAdapter = ProductAdapter(
            onProductClick = { product ->
                // Navigate to ProductDetailActivity
                val intent = Intent(this, ProductDetailActivity::class.java).apply {
                    putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.id)
                }
                startActivity(intent)
            },
            onAddToCart = { product ->
                Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
                // TODO: Add to cart API call
            }
        )
        
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = productAdapter
        }
    }
    
    private fun setupClickListeners() {
        // Theme toggle
        binding.ivThemeToggle.setOnClickListener {
            toggleTheme()
        }
        
        // Search
        binding.ivSearch.setOnClickListener {
            // Navigate to SearchActivity
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        
        // Bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    true
                }
                R.id.navigation_categories -> {
                    Toast.makeText(this, "Categories coming soon", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_cart -> {
                    Toast.makeText(this, "Cart coming soon", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_profile -> {
                    Toast.makeText(this, "Profile coming soon", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
    
    private fun toggleTheme() {
        val isDarkMode = SharedPrefsManager.isDarkMode(this)
        val newMode = !isDarkMode
        
        SharedPrefsManager.setDarkMode(this, newMode)
        
        // Apply theme
        if (newMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        
        // Recreate activity to apply theme
        recreate()
    }
    
    private fun observeViewModel() {
        // Categories
        productViewModel.categories.observe(this) { result ->
            result.onSuccess { categories ->
                categoryAdapter.submitList(categories)
            }
            result.onFailure { error ->
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = error.message ?: "Failed to load categories"
            }
        }
        
        // Featured Products
        productViewModel.featuredProducts.observe(this) { result ->
            result.onSuccess { products ->
                productAdapter.submitList(products)
            }
            result.onFailure { error ->
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = error.message ?: "Failed to load products"
            }
        }
        
        // Loading
        productViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}