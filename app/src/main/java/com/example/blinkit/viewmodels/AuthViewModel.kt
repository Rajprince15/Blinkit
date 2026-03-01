package com.example.blinkit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkit.models.AuthResponse
import com.example.blinkit.models.User
import com.example.blinkit.repositories.AuthRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for authentication operations
 
 */
class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _signupResult = MutableLiveData<Result<AuthResponse>>()
    val signupResult: LiveData<Result<AuthResponse>> = _signupResult

    private val _loginResult = MutableLiveData<Result<AuthResponse>>()
    val loginResult: LiveData<Result<AuthResponse>> = _loginResult

    private val _profileResult = MutableLiveData<Result<User>>()
    val profileResult: LiveData<Result<User>> = _profileResult

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    /**
     * User signup
     */
    fun signup(name: String, email: String, password: String, phone: String?) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.signup(name, email, password, phone)

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        _signupResult.value = Result.success(apiResponse.data)
                    } else {
                        _signupResult.value = Result.failure(
                            Exception(apiResponse.message ?: "Signup failed")
                        )
                    }
                } else {
                    _signupResult.value = Result.failure(
                        Exception("Signup failed: ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                _signupResult.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * User login
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.login(email, password)

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        _loginResult.value = Result.success(apiResponse.data)
                    } else {
                        _loginResult.value = Result.failure(
                            Exception(apiResponse.message ?: "Login failed")
                        )
                    }
                } else {
                    _loginResult.value = Result.failure(
                        Exception("Login failed: ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Get user profile
     * Token is automatically injected by ApiClient interceptor
     */
    fun getProfile() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repository.getProfile()

                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        _profileResult.value = Result.success(apiResponse.data)
                    } else {
                        _profileResult.value = Result.failure(
                            Exception(apiResponse.message ?: "Failed to fetch profile")
                        )
                    }
                } else {
                    _profileResult.value = Result.failure(
                        Exception("Failed to fetch profile: ${response.message()}")
                    )
                }
            } catch (e: Exception) {
                _profileResult.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }
}
