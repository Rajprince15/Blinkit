package com.example.blinkit.repositories

import com.example.blinkit.api.ApiClient
import com.example.blinkit.models.Address

/**
 * Repository for address operations
 * Auth token is automatically injected by ApiClient interceptor
 */
class AddressRepository {
    private val api = ApiClient.apiService

    suspend fun getAddresses() = api.getAddresses()
    suspend fun addAddress(address: Address) = api.addAddress(address)
    suspend fun updateAddress(addressId: Int, address: Address) = api.updateAddress(addressId, address)
    suspend fun deleteAddress(addressId: Int) = api.deleteAddress(addressId)
    suspend fun setDefaultAddress(addressId: Int) = api.setDefaultAddress(addressId)
}