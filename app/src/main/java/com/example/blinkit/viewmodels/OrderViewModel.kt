package com.example.blinkit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkit.models.*
import com.example.blinkit.repositories.OrderRepository
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    private val repo = OrderRepository()

    private val _orders = MutableLiveData<Result<List<Order>>>()
    val orders: LiveData<Result<List<Order>>> = _orders

    private val _currentOrder = MutableLiveData<Result<Order>>()
    val currentOrder: LiveData<Result<Order>> = _currentOrder

    private val _tracking = MutableLiveData<Result<List<OrderStatusHistory>>>()
    val tracking: LiveData<Result<List<OrderStatusHistory>>> = _tracking

    private val _operationResult = MutableLiveData<Result<Any>>()
    val operationResult: LiveData<Result<Any>> = _operationResult

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun fetchOrders(token: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val resp = repo.getOrders(token)
                if (resp.isSuccessful && resp.body() != null) {
                    val api = resp.body()!!
                    if (api.success && api.data != null) {
                        _orders.value = Result.success(api.data)
                    } else {
                        _orders.value = Result.failure(Exception(api.message ?: "Failed to load orders"))
                    }
                } else {
                    _orders.value = Result.failure(Exception(resp.message()))
                }
            } catch (e: Exception) {
                _orders.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun placeOrder(token: String, request: CreateOrderRequest) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val resp = repo.createOrder(token, request)
                if (resp.isSuccessful && resp.body() != null) {
                    val api = resp.body()!!
                    if (api.success) {
                        _operationResult.value = Result.success(api.data ?: Any())
                    } else {
                        _operationResult.value = Result.failure(Exception(api.message ?: "Failed to create order"))
                    }
                }
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun trackOrder(token: String, orderId: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val resp = repo.trackOrder(token, orderId)
                if (resp.isSuccessful && resp.body() != null) {
                    val api = resp.body()!!
                    if (api.success && api.data != null) {
                        _tracking.value = Result.success(api.data)
                    } else {
                        _tracking.value = Result.failure(Exception(api.message ?: "Failed to track order"))
                    }
                } else {
                    _tracking.value = Result.failure(Exception(resp.message()))
                }
            } catch (e: Exception) {
                _tracking.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }
}
