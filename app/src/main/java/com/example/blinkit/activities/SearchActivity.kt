"package com.example.blinkit.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blinkit.adapters.ProductAdapter
import com.example.blinkit.adapters.RecentSearchAdapter
import com.example.blinkit.databinding.ActivitySearchBinding
import com.example.blinkit.utils.SharedPrefsManager
import com.example.blinkit.viewmodels.ProductViewModel

/**
 * Activity for searching products with recent searches
 */
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recentSearchAdapter: RecentSearchAdapter
    
    private val searchHandler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null
    private val SEARCH_DELAY_MS = 500L // 500ms debounce

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupRecyclerViews()
        setupViewModel()
        loadRecentSearches()
    }

    private fun setupViews() {
        // Focus on search input
        binding.etSearch.requestFocus()

        // Back button
        binding.ivBack.setOnClickListener {
            finish()
        }

        // Clear button
        binding.ivClear.setOnClickListener {
            binding.etSearch.text?.clear()
        }

        // Search input listener with debounce
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim() ?: \"\"
                
                // Show/hide clear button
                binding.ivClear.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE

                // Cancel previous search
                searchRunnable?.let { searchHandler.removeCallbacks(it) }

                if (query.isEmpty()) {
                    showRecentSearches()
                } else {
                    // Schedule new search with debounce
                    searchRunnable = Runnable {
                        performSearch(query)
                    }
                    searchHandler.postDelayed(searchRunnable!!, SEARCH_DELAY_MS)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Clear recent searches
        binding.tvClearRecent.setOnClickListener {
            SharedPrefsManager.clearRecentSearches(this)
            loadRecentSearches()
        }
    }

    private fun setupRecyclerViews() {
        // Recent searches adapter
        recentSearchAdapter = RecentSearchAdapter(
            onSearchClick = { query ->
                binding.etSearch.setText(query)
                binding.etSearch.setSelection(query.length)
                performSearch(query)
            },
            onRemoveClick = { query ->
                SharedPrefsManager.removeRecentSearch(this, query)
                loadRecentSearches()
            }
        )

        binding.rvRecentSearches.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = recentSearchAdapter
        }

        // Search results adapter
        productAdapter = ProductAdapter(
            onProductClick = { product ->
                val intent = Intent(this, ProductDetailActivity::class.java).apply {
                    putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.id)
                }
                startActivity(intent)
            },
            onAddToCart = { product ->
                Toast.makeText(this, \"${product.name} added to cart\", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvSearchResults.apply {
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
            adapter = productAdapter
        }
    }

    private fun setupViewModel() {
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        // Observe search results
        productViewModel.searchResults.observe(this) { result ->
            result.onSuccess { products ->
                if (products.isEmpty()) {
                    showEmptyState()
                } else {
                    showSearchResults(products)
                }
            }
            result.onFailure { error ->
                Toast.makeText(this, error.message ?: \"Search failed\", Toast.LENGTH_SHORT).show()
                showEmptyState()
            }
        }

        // Observe loading
        productViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun loadRecentSearches() {
        val recentSearches = SharedPrefsManager.getRecentSearches(this)
        if (recentSearches.isEmpty()) {
            binding.tvNoRecent.visibility = View.VISIBLE
            binding.rvRecentSearches.visibility = View.GONE
        } else {
            binding.tvNoRecent.visibility = View.GONE
            binding.rvRecentSearches.visibility = View.VISIBLE
            recentSearchAdapter.submitList(recentSearches)
        }
    }

    private fun performSearch(query: String) {
        if (query.isEmpty()) return

        // Save to recent searches
        SharedPrefsManager.saveRecentSearch(this, query)

        // Perform search
        productViewModel.searchProducts(query)
    }

    private fun showRecentSearches() {
        binding.layoutRecentSearches.visibility = View.VISIBLE
        binding.layoutSearchResults.visibility = View.GONE
        binding.layoutEmpty.visibility = View.GONE
        loadRecentSearches()
    }

    private fun showSearchResults(products: List<com.example.blinkit.models.Product>) {
        binding.layoutRecentSearches.visibility = View.GONE
        binding.layoutSearchResults.visibility = View.VISIBLE
        binding.layoutEmpty.visibility = View.GONE
        
        binding.tvResultsCount.text = \"${products.size} Results Found\"
        productAdapter.submitList(products)
    }

    private fun showEmptyState() {
        binding.layoutRecentSearches.visibility = View.GONE
        binding.layoutSearchResults.visibility = View.GONE
        binding.layoutEmpty.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        searchRunnable?.let { searchHandler.removeCallbacks(it) }
    }
}
"