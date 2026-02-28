package com.example.blinkit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkit.models.Category
import com.example.blinkit.models.Product
import com.example.blinkit.repositories.ProductRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for product and category operations
 */
class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _categories = MutableLiveData<Result<List<Category>>>()
    val categories: LiveData<Result<List<Category>>> = _categories

    private val _featuredProducts = MutableLiveData<Result<List<Product>>>()
    val featuredProducts: LiveData<Result<List<Product>>> = _featuredProducts

    private val _products = MutableLiveData<Result<List<Product>>>()
    val products: LiveData<Result<List<Product>>> = _products

    private val _productDetails = MutableLiveData<Result<Product>>()
    val productDetails: LiveData<Result<Product>> = _productDetails

    private val _searchResults = MutableLiveData<Result<List<Product>>>()
    val searchResults: LiveData<Result<List<Product>>> = _searchResults

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    /**
     * Load categories
     */
    fun loadCategories() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.getCategories()

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        _categories.value = Result.success(apiResponse.data)
                    } else {
                        _categories.value = Result.failure(
                            Exception(apiResponse.message ?: "Failed to load categories")
                        )
                    }
                } else {
                    _categories.value = Result.failure(
                        Exception("Failed to load categories: ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                _categories.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Load featured products
     */
    fun loadFeaturedProducts(limit: Int = 10) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.getFeaturedProducts(limit)

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        _featuredProducts.value = Result.success(apiResponse.data)
                    } else {
                        _featuredProducts.value = Result.failure(
                            Exception(apiResponse.message ?: "Failed to load featured products")
                        )
                    }
                } else {
                    _featuredProducts.value = Result.failure(
                        Exception("Failed to load featured products: ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                _featuredProducts.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Load products by category
     */
    fun loadProductsByCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.getProductsByCategory(categoryId)

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        _products.value = Result.success(apiResponse.data)
                    } else {
                        _products.value = Result.failure(
                            Exception(apiResponse.message ?: "Failed to load products")
                        )
                    }
                } else {
                    _products.value = Result.failure(
                        Exception("Failed to load products: ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                _products.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Load product details
     */
    fun loadProductDetails(productId: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.getProductById(productId)

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        _productDetails.value = Result.success(apiResponse.data)
                    } else {
                        _productDetails.value = Result.failure(
                            Exception(apiResponse.message ?: "Failed to load product details")
                        )
                    }
                } else {
                    _productDetails.value = Result.failure(
                        Exception("Failed to load product details: ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                _productDetails.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Search products
     */
    fun searchProducts(query: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.searchProducts(query)

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        _searchResults.value = Result.success(apiResponse.data)
                    } else {
                        _searchResults.value = Result.failure(
                            Exception(apiResponse.message ?: "No results found")
                        )
                    }
                } else {
                    _searchResults.value = Result.failure(
                        Exception("Search failed: ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                _searchResults.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }
}
