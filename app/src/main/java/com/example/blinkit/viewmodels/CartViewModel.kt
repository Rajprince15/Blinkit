package com.example.blinkit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkit.models.*
import com.example.blinkit.repositories.CartRepository
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val repo = CartRepository()

    private val _cartSummary = MutableLiveData<Result<CartSummary>>()
    val cartSummary: LiveData<Result<CartSummary>> = _cartSummary

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun loadCart(token: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repo.getCart(token)
                if (response.isSuccessful && response.body() != null) {
                    val api = response.body()!!
                    if (api.success && api.data != null) {
                        _cartSummary.value = Result.success(api.data)
                    } else {
                        _cartSummary.value = Result.failure(Exception(api.message ?: "Failed to load cart"))
                    }
                } else {
                    _cartSummary.value = Result.failure(Exception("Error: ${response.message()}"))
                }
            } catch (e: Exception) {
                _cartSummary.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun addToCart(token: String, request: AddToCartRequest) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repo.addToCart(token, request)
                // reload after modification
                loadCart(token)
            } catch (_: Exception) {
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateCartItem(token: String, cartItemId: Int, qty: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repo.updateCartItem(token, cartItemId, UpdateCartRequest(qty))
                loadCart(token)
            } catch (_: Exception) {
            } finally {
                _loading.value = false
            }
        }
    }

    fun removeCartItem(token: String, cartItemId: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repo.removeCartItem(token, cartItemId)
                loadCart(token)
            } catch (_: Exception) {
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearCart(token: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repo.clearCart(token)
                loadCart(token)
            } catch (_: Exception) {
            } finally {
                _loading.value = false
            }
        }
    }
}
