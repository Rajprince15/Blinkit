package com.example.blinkit.repositories

import com.example.blinkit.api.ApiClient
import com.example.blinkit.models.LoginRequest
import com.example.blinkit.models.SignupRequest

/**
 * Repository for authentication operations
 */
class AuthRepository {

    private val apiService = ApiClient.apiService

    /**
     * User signup
     */
    suspend fun signup(name: String, email: String, password: String, phone: String?) =
        apiService.signup(SignupRequest(name, email, password, phone))

    /**
     * User login
     */
    suspend fun login(email: String, password: String) =
        apiService.login(LoginRequest(email, password))

    /**
     * Get user profile
     */
    suspend fun getProfile(token: String) =
        apiService.getProfile("Bearer $token")

    /**
     * Logout user
     */
    suspend fun logout(token: String) =
        apiService.logout("Bearer $token")
}
