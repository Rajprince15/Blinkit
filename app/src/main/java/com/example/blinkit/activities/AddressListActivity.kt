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
import com.example.blinkit.utils.SharedPrefsManager
import com.example.blinkit.viewmodels.AddressViewModel

class AddressListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressListBinding
    private lateinit var viewModel: AddressViewModel
    private lateinit var adapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AddressViewModel::class.java]
        adapter = AddressAdapter(mutableListOf(),
            onEdit = { addr -> openEdit(addr) },
            onDelete = { addr -> deleteAddress(addr) },
            onSetDefault = { addr -> setDefault(addr) }
        )
        binding.rvAddresses.layoutManager = LinearLayoutManager(this)
        binding.rvAddresses.adapter = adapter

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditAddressActivity::class.java))
        }

        observe()
        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun observe() {
        viewModel.addresses.observe(this) { result ->
            result.onSuccess { list ->
                adapter.updateData(list)
                binding.rvAddresses.visibility = if (list.isEmpty()) View.GONE else View.VISIBLE
            }
            result.onFailure { err ->
                Toast.makeText(this, err.message ?: "Failed to load", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.operationResult.observe(this) { res ->
            res.onSuccess {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }
            res.onFailure { err ->
                Toast.makeText(this, err.message ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        viewModel.loadAddresses(getToken())
    }

    private fun getToken(): String = SharedPrefsManager.getToken(this) ?: ""

    private fun openEdit(address: Address) {
        val intent = Intent(this, AddEditAddressActivity::class.java)
        // serialize address to JSON
        val json = com.google.gson.Gson().toJson(address)
        intent.putExtra("address_json", json)
        startActivity(intent)
    }

    private fun deleteAddress(address: Address) {
        viewModel.deleteAddress(getToken(), address.id)
    }

    private fun setDefault(address: Address) {
        viewModel.setDefault(getToken(), address.id)
    }
}
