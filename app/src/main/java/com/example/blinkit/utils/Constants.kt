package com.example.blinkit.utils

/**
 * Constants used throughout the application
 */
object Constants {

    // Base URL for API calls
    // For emulator use: http://10.0.2.2:5000/api/
    // For physical device use: http://YOUR_IP:5000/api/
    const val BASE_URL = "http://10.0.2.2:5000/api/"

    // API Endpoints
    const val ENDPOINT_SIGNUP = "auth/signup"
    const val ENDPOINT_LOGIN = "auth/login"
    const val ENDPOINT_PROFILE = "auth/profile"
    const val ENDPOINT_LOGOUT = "auth/logout"

    const val ENDPOINT_CATEGORIES = "categories"
    const val ENDPOINT_PRODUCTS = "products"
    const val ENDPOINT_PRODUCTS_FEATURED = "products/featured"
    const val ENDPOINT_PRODUCTS_SEARCH = "products/search"
    const val ENDPOINT_PRODUCTS_BY_CATEGORY = "products/category/{categoryId}"

    // Request Codes
    const val REQUEST_LOGIN = 1001
    const val REQUEST_SIGNUP = 1002

    // Validation
    const val MIN_PASSWORD_LENGTH = 6
    const val PHONE_LENGTH = 10

    // Pagination
    const val PAGE_SIZE = 20
    const val FEATURED_PRODUCTS_LIMIT = 10

    // Intent Keys
    const val KEY_PRODUCT_ID = "product_id"
    const val KEY_CATEGORY_ID = "category_id"
    const val KEY_CATEGORY_NAME = "category_name"

    // Error Messages
    const val ERROR_NETWORK = "Network error. Please check your internet connection."
    const val ERROR_SERVER = "Server error. Please try again later."
    const val ERROR_UNKNOWN = "Something went wrong. Please try again."

    // Success Messages
    const val SUCCESS_LOGIN = "Login successful!"
    const val SUCCESS_SIGNUP = "Account created successfully!"
    const val SUCCESS_LOGOUT = "Logged out successfully!"
}
