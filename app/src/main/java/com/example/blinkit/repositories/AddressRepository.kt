package com.example.blinkit.repositories

import com.example.blinkit.api.ApiClient
import com.example.blinkit.models.Address

class AddressRepository {
    private val api = ApiClient.apiService

    suspend fun getAddresses(token: String) = api.getAddresses("Bearer $token")
    suspend fun addAddress(token: String, address: Address) = api.addAddress("Bearer $token", address)
    suspend fun updateAddress(token: String, addressId: Int, address: Address) = api.updateAddress("Bearer $token", addressId, address)
    suspend fun deleteAddress(token: String, addressId: Int) = api.deleteAddress("Bearer $token", addressId)
    suspend fun setDefaultAddress(token: String, addressId: Int) = api.setDefaultAddress("Bearer $token", addressId)
}
