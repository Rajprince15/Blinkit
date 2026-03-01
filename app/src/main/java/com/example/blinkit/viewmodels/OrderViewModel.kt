package com.example.blinkit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkit.models.*
import com.example.blinkit.repositories.OrderRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for order operations
 * Token is automatically injected by ApiClient interceptor
 */
class OrderViewModel : ViewModel() {
    private val repo = OrderRepository()

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    private val _currentOrder = MutableLiveData<Order>()
    val currentOrder: LiveData<Order> = _currentOrder

    private val _orderTimeline = MutableLiveData<List<OrderStatusHistory>>()
    val orderTimeline: LiveData<List<OrderStatusHistory>> = _orderTimeline

    private val _operationResult = MutableLiveData<Result<Any>>()
    val operationResult: LiveData<Result<Any>> = _operationResult

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getOrders() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val resp = repo.getOrders()
                if (resp.isSuccessful && resp.body() != null) {
                    val api = resp.body()!!
                    if (api.success && api.data != null) {
                        _orders.value = api.data
                    } else {
                        _error.value = api.message ?: "Failed to load orders"
                    }
                } else {
                    _error.value = "Network error: ${resp.message()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getOrderById(orderId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val resp = repo.getOrderById(orderId)
                if (resp.isSuccessful && resp.body() != null) {
                    val api = resp.body()!!
                    if (api.success && api.data != null) {
                        // Merge items into order object for easy access
                        val orderWithItems = api.data.order.copy(items = api.data.items)
                        _currentOrder.value = orderWithItems
                    } else {
                        _error.value = api.message ?: "Failed to load order"
                    }
                } else {
                    _error.value = "Network error: ${resp.message()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun placeOrder(request: CreateOrderRequest) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val resp = repo.createOrder(request)
                if (resp.isSuccessful && resp.body() != null) {
                    val api = resp.body()!!
                    if (api.success) {
                        _operationResult.value = Result.success(api.data ?: Any())
                    } else {
                        _operationResult.value = Result.failure(Exception(api.message ?: "Failed to create order"))
                    }
                } else {
                    _operationResult.value = Result.failure(Exception(resp.message()))
                }
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun trackOrder(orderId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val resp = repo.trackOrder(orderId)
                if (resp.isSuccessful && resp.body() != null) {
                    val api = resp.body()!!
                    if (api.success && api.data != null) {
                        _orderTimeline.value = api.data
                    } else {
                        _error.value = api.message ?: "Failed to track order"
                    }
                } else {
                    _error.value = "Network error: ${resp.message()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}