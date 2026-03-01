"package com.example.blinkit.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.blinkit.R
import com.example.blinkit.adapters.ProductAdapter
import com.example.blinkit.databinding.ActivityProductListBinding
import com.example.blinkit.models.Product
import com.example.blinkit.viewmodels.ProductViewModel

/**
 * Activity to display list of products by category with filters
 */
class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter
    private var categoryId: Int = -1
    private var categoryName: String = \"\"
    private var currentProducts: List<Product> = emptyList()

    companion object {
        const val EXTRA_CATEGORY_ID = \"category_id\"
        const val EXTRA_CATEGORY_NAME = \"category_name\"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get category info from intent
        categoryId = intent.getIntExtra(EXTRA_CATEGORY_ID, -1)
        categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME) ?: \"Products\"

        setupToolbar()
        setupRecyclerView()
        setupFilters()
        setupViewModel()

        // Load products
        if (categoryId != -1) {
            productViewModel.loadProductsByCategory(categoryId)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tvCategoryName.text = categoryName

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            onProductClick = { product ->
                navigateToProductDetails(product)
            },
            onAddToCart = { product ->
                addToCart(product)
            }
        )

        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(this@ProductListActivity, 2)
            adapter = productAdapter
        }

        // Swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            if (categoryId != -1) {
                productViewModel.loadProductsByCategory(categoryId)
            }
        }

        // Retry button
        binding.btnRetry.setOnClickListener {
            if (categoryId != -1) {
                productViewModel.loadProductsByCategory(categoryId)
            }
        }
    }

    private fun setupFilters() {
        binding.chipGroupFilters.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_all -> {
                    showProducts(currentProducts)
                }
                R.id.chip_sort_price_low -> {
                    val sorted = currentProducts.sortedBy { it.price }
                    showProducts(sorted)
                }
                R.id.chip_sort_price_high -> {
                    val sorted = currentProducts.sortedByDescending { it.price }
                    showProducts(sorted)
                }
                R.id.chip_sort_rating -> {
                    val sorted = currentProducts.sortedByDescending { it.rating }
                    showProducts(sorted)
                }
                R.id.chip_sort_popular -> {
                    val sorted = currentProducts.sortedByDescending { it.totalReviews }
                    showProducts(sorted)
                }
            }
        }
    }

    private fun setupViewModel() {
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        // Observe products
        productViewModel.products.observe(this) { result ->
            result.onSuccess { products ->
                currentProducts = products
                showProducts(products)
            }
            result.onFailure { error ->
                showError(error.message ?: \"Failed to load products\")
            }
        }

        // Observe loading
        productViewModel.loading.observe(this) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
            binding.progressBar.visibility = if (isLoading && currentProducts.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun showProducts(products: List<Product>) {
        if (products.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.layoutError.visibility = View.GONE
            binding.rvProducts.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.layoutError.visibility = View.GONE
            binding.rvProducts.visibility = View.VISIBLE
            productAdapter.submitList(products)
        }
    }

    private fun showError(message: String) {
        binding.layoutError.visibility = View.VISIBLE
        binding.layoutEmpty.visibility = View.GONE
        binding.rvProducts.visibility = View.GONE
        binding.tvError.text = message
    }

    private fun navigateToProductDetails(product: Product) {
        val intent = Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.id)
        }
        startActivity(intent)
    }

    private fun addToCart(product: Product) {
        Toast.makeText(this, \"${product.name} added to cart\", Toast.LENGTH_SHORT).show()
        // TODO: Implement cart API call in Phase 4
    }
}
"