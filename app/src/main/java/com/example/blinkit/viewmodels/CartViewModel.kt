package com.example.blinkit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkit.models.*
import com.example.blinkit.repositories.CartRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for cart operations
 * Token is automatically injected by ApiClient interceptor
 */
class CartViewModel : ViewModel() {
    private val repo = CartRepository()

    private val _cartSummary = MutableLiveData<Result<CartSummary>>()
    val cartSummary: LiveData<Result<CartSummary>> = _cartSummary

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun loadCart() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = repo.getCart()
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

    fun addToCart(request: AddToCartRequest) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repo.addToCart(request)
                // reload after modification
                loadCart()
            } catch (_: Exception) {
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateCartItem(cartItemId: Int, qty: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repo.updateCartItem(cartItemId, UpdateCartRequest(qty))
                loadCart()
            } catch (_: Exception) {
            } finally {
                _loading.value = false
            }
        }
    }

    fun removeCartItem(cartItemId: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repo.removeCartItem(cartItemId)
                loadCart()
            } catch (_: Exception) {
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            try {
                _loading.value = true
                repo.clearCart()
                loadCart()
            } catch (_: Exception) {
            } finally {
                _loading.value = false
            }
        }
    }
}








I still need to check and update:

ViewModels - Remove token parameters from repository calls
Activities - Update any old code still passing tokens manually
Verify API response models match between backend and Android