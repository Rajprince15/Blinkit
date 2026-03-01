package com.example.blinkit.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blinkit.adapters.AddressAdapter
import com.example.blinkit.databinding.ActivityAddressListBinding
import com.example.blinkit.models.Address
import com.example.blinkit.viewmodels.AddressViewModel
import com.google.gson.Gson

/**
 * AddressListActivity - Displays list of saved addresses
 * Token is automatically injected by ApiClient interceptor
 */
class AddressListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressListBinding
    private lateinit var viewModel: AddressViewModel
    private lateinit var adapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AddressViewModel::class.java]
        
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
        loadAddresses()
    }

    override fun onResume() {
        super.onResume()
        loadAddresses()
    }

    private fun setupRecyclerView() {
        adapter = AddressAdapter(
            mutableListOf(),
            onEdit = { address -> openEditAddress(address) },
            onDelete = { address -> deleteAddress(address) },
            onSetDefault = { address -> setDefaultAddress(address) }
        )
        binding.rvAddresses.apply {
            layoutManager = LinearLayoutManager(this@AddressListActivity)
            adapter = this@AddressListActivity.adapter
        }
    }

    private fun setupClickListeners() {
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditAddressActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.addresses.observe(this) { result ->
            result.onSuccess { addressList ->
                adapter.updateData(addressList)
                binding.rvAddresses.visibility = if (addressList.isEmpty()) View.GONE else View.VISIBLE
            }
            result.onFailure { error ->
                Toast.makeText(this, error.message ?: "Failed to load addresses", Toast.LENGTH_SHORT).show()
            }
        }
        
        viewModel.operationResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Operation successful", Toast.LENGTH_SHORT).show()
            }
            result.onFailure { error ->
                Toast.makeText(this, error.message ?: "Operation failed", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            // Handle loading state if needed
        }
    }

    private fun loadAddresses() {
        viewModel.loadAddresses()
    }

    private fun openEditAddress(address: Address) {
        val intent = Intent(this, AddEditAddressActivity::class.java)
        val json = Gson().toJson(address)
        intent.putExtra("address_json", json)
        startActivity(intent)
    }

    private fun deleteAddress(address: Address) {
        viewModel.deleteAddress(address.id)
    }

    private fun setDefaultAddress(address: Address) {
        viewModel.setDefault(address.id)
    }
}
