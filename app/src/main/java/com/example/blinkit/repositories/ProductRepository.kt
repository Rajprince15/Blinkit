package com.example.blinkit.repositories

import com.example.blinkit.api.ApiClient

/**
 * Repository for product and category operations
 */
class ProductRepository {

    private val apiService = ApiClient.apiService

    /**
     * Get all categories
     */
    suspend fun getCategories() = apiService.getCategories()

    /**
     * Get category by ID
     */
    suspend fun getCategoryById(categoryId: Int) = apiService.getCategoryById(categoryId)

    /**
     * Get all products
     */
    suspend fun getProducts(page: Int = 1, limit: Int = 20) =
        apiService.getProducts(page, limit)

    /**
     * Get featured products
     */
    suspend fun getFeaturedProducts(limit: Int = 10) =
        apiService.getFeaturedProducts(limit)

    /**
     * Search products
     */
    suspend fun searchProducts(query: String) =
        apiService.searchProducts(query)

    /**
     * Get products by category
     */
    suspend fun getProductsByCategory(categoryId: Int, page: Int = 1, limit: Int = 20) =
        apiService.getProductsByCategory(categoryId, page, limit)

    /**
     * Get product by ID
     */
    suspend fun getProductById(productId: Int) =
        apiService.getProductById(productId)
}
