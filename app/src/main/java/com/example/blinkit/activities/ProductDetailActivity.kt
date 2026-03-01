"package com.example.blinkit.activities

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blinkit.R
import com.example.blinkit.adapters.ProductAdapter
import com.example.blinkit.databinding.ActivityProductDetailBinding
import com.example.blinkit.models.Product
import com.example.blinkit.utils.ImageLoader
import com.example.blinkit.viewmodels.ProductViewModel

/**
 * Activity to display detailed product information
 */
class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var relatedProductsAdapter: ProductAdapter
    private var currentProduct: Product? = null
    private var quantity: Int = 1

    companion object {
        const val EXTRA_PRODUCT_ID = \"product_id\"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getIntExtra(EXTRA_PRODUCT_ID, -1)
        if (productId == -1) {
            Toast.makeText(this, \"Invalid product\", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupViewModel()

        // Load product details
        productViewModel.loadProductDetails(productId)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        relatedProductsAdapter = ProductAdapter(
            onProductClick = { product ->
                // Navigate to this product's details
                val intent = intent
                intent.putExtra(EXTRA_PRODUCT_ID, product.id)
                finish()
                startActivity(intent)
            },
            onAddToCart = { product ->
                Toast.makeText(this, \"${product.name} added to cart\", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvRelatedProducts.apply {
            layoutManager = LinearLayoutManager(this@ProductDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = relatedProductsAdapter
        }
    }

    private fun setupClickListeners() {
        // Quantity controls
        binding.btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantityUI()
            }
        }

        binding.btnIncrease.setOnClickListener {
            val maxQty = currentProduct?.maxOrderQuantity ?: 10
            if (quantity < maxQty) {
                quantity++
                updateQuantityUI()
            } else {
                Toast.makeText(this, \"Maximum quantity: $maxQty\", Toast.LENGTH_SHORT).show()
            }
        }

        // Add to cart
        binding.btnAddToCart.setOnClickListener {
            currentProduct?.let { product ->
                if (product.isInStock()) {
                    Toast.makeText(
                        this,
                        \"${product.name} (x$quantity) added to cart\",
                        Toast.LENGTH_SHORT
                    ).show()
                    // TODO: Implement cart API call in Phase 4
                } else {
                    Toast.makeText(this, \"Product is out of stock\", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupViewModel() {
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        // Observe product details
        productViewModel.productDetails.observe(this) { result ->
            result.onSuccess { product ->
                currentProduct = product
                displayProductDetails(product)
                
                // Load related products from same category
                productViewModel.loadProductsByCategory(product.categoryId)
            }
            result.onFailure { error ->
                Toast.makeText(this, error.message ?: \"Failed to load product\", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Observe related products
        productViewModel.products.observe(this) { result ->
            result.onSuccess { products ->
                // Filter out current product and show max 10
                val related = products.filter { it.id != currentProduct?.id }.take(10)
                if (related.isNotEmpty()) {
                    relatedProductsAdapter.submitList(related)
                }
            }
        }

        // Observe loading
        productViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun displayProductDetails(product: Product) {
        // Load image
        ImageLoader.loadRounded(
            binding.ivProductImage,
            product.imageUrl,
            cornerRadius = 0
        )

        // Product info
        binding.tvProductName.text = product.name
        binding.tvBrand.text = product.brand ?: \"\"
        
        // Rating
        binding.tvRating.text = product.rating.toString()
        val reviewsText = if (product.totalReviews == 1) {
            getString(R.string.review, product.totalReviews)
        } else {
            getString(R.string.reviews, product.totalReviews)
        }
        binding.tvReviews.text = \"($reviewsText)\"

        // Stock status
        if (product.isInStock()) {
            binding.tvStockStatus.text = getString(R.string.in_stock)
            binding.tvStockStatus.setTextColor(getColor(R.color.in_stock))
            binding.btnAddToCart.isEnabled = true
        } else {
            binding.tvStockStatus.text = getString(R.string.out_of_stock)
            binding.tvStockStatus.setTextColor(getColor(R.color.out_of_stock))
            binding.btnAddToCart.isEnabled = false
        }

        // Price
        binding.tvPrice.text = product.getFormattedPrice()
        
        if (product.hasDiscount() && product.originalPrice != null) {
            binding.tvOriginalPrice.visibility = View.VISIBLE
            binding.tvDiscount.visibility = View.VISIBLE
            binding.tvOriginalPrice.text = product.getFormattedOriginalPrice()
            binding.tvOriginalPrice.paintFlags = binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvDiscount.text = getString(R.string.discount, product.discountPercentage)
        } else {
            binding.tvOriginalPrice.visibility = View.GONE
            binding.tvDiscount.visibility = View.GONE
        }

        binding.tvUnit.text = \"Per ${product.unit}\"

        // Description
        binding.tvDescription.text = product.description ?: \"No description available\"

        // Quantity limit
        binding.tvQuantityLimit.text = \"(Max: ${product.maxOrderQuantity})\"
        
        // Reset quantity to min
        quantity = product.minOrderQuantity
        updateQuantityUI()
    }

    private fun updateQuantityUI() {
        binding.tvQuantity.text = quantity.toString()
        
        val minQty = currentProduct?.minOrderQuantity ?: 1
        binding.btnDecrease.isEnabled = quantity > minQty
    }
}
"