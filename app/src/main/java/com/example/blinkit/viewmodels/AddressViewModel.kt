package com.example.blinkit.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blinkit.models.Address
import com.example.blinkit.repositories.AddressRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for address operations
 * Token is automatically injected by ApiClient interceptor
 */
class AddressViewModel : ViewModel() {
    private val repo = AddressRepository()

    private val _addresses = MutableLiveData<Result<List<Address>>>()
    val addresses: LiveData<Result<List<Address>>> = _addresses

    private val _operationResult = MutableLiveData<Result<Any>>()
    val operationResult: LiveData<Result<Any>> = _operationResult

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun loadAddresses() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val resp = repo.getAddresses()
                if (resp.isSuccessful && resp.body() != null) {
                    val api = resp.body()!!
                    if (api.success && api.data != null) {
                        _addresses.value = Result.success(api.data)
                    } else {
                        _addresses.value = Result.failure(Exception(api.message ?: "Failed to load addresses"))
                    }
                } else {
                    _addresses.value = Result.failure(Exception(resp.message()))
                }
            } catch (e: Exception) {
                _addresses.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun addAddress(address: Address) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val resp = repo.addAddress(address)
                if (resp.isSuccessful && resp.body() != null) {
                    val api = resp.body()!!
                    if (api.success) {
                        _operationResult.value = Result.success(api.data ?: Any())
                        loadAddresses()
                    } else {
                        _operationResult.value = Result.failure(Exception(api.message ?: "Failed"))
                    }
                }
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateAddress(addressId: Int, address: Address) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val resp = repo.updateAddress(addressId, address)
                if (resp.isSuccessful && resp.body() != null) {
                    val api = resp.body()!!
                    if (api.success) {
                        _operationResult.value = Result.success(api.data ?: Any())
                        loadAddresses()
                    } else {
                        _operationResult.value = Result.failure(Exception(api.message ?: "Failed"))
                    }
                }
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteAddress(addressId: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val resp = repo.deleteAddress(addressId)
                if (resp.isSuccessful && resp.body() != null) {
                    val api = resp.body()!!
                    if (api.success) {
                        _operationResult.value = Result.success(api.data ?: Any())
                        loadAddresses()
                    } else {
                        _operationResult.value = Result.failure(Exception(api.message ?: "Failed"))
                    }
                }
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun setDefault(addressId: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val resp = repo.setDefaultAddress(addressId)
                if (resp.isSuccessful && resp.body() != null) {
                    val api = resp.body()!!
                    if (api.success) {
                        _operationResult.value = Result.success(api.data ?: Any())
                        loadAddresses()
                    } else {
                        _operationResult.value = Result.failure(Exception(api.message ?: "Failed"))
                    }
                }
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }
}