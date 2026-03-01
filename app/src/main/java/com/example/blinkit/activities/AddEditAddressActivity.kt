package com.example.blinkit.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.blinkit.databinding.ActivityAddEditAddressBinding
import com.example.blinkit.models.Address
import com.example.blinkit.viewmodels.AddressViewModel
import com.google.gson.Gson

/**
 * AddEditAddressActivity - Add or edit delivery address
 * Token is automatically injected by ApiClient interceptor
 */
class AddEditAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditAddressBinding
    private lateinit var viewModel: AddressViewModel
    private var editingAddress: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AddressViewModel::class.java]

        // Check if we're editing an existing address
        intent.getStringExtra("address_json")?.let { json ->
            editingAddress = Gson().fromJson(json, Address::class.java)
            populateFields()
        }

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            saveAddress()
        }
    }

    private fun populateFields() {
        editingAddress?.let { address ->
            binding.etName.setText(address.fullName)
            binding.etLine1.setText(address.addressLine1)
            binding.etLine2.setText(address.addressLine2 ?: "")
            binding.etCity.setText(address.city)
            binding.etState.setText(address.state)
            binding.etZipcode.setText(address.pincode)
            binding.etCountry.setText(address.country ?: "India")
            binding.cbDefault.isChecked = address.isDefault
        }
    }

    private fun saveAddress() {
        val name = binding.etName.text.toString().trim()
        val line1 = binding.etLine1.text.toString().trim()
        val line2 = binding.etLine2.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val state = binding.etState.text.toString().trim()
        val zipcode = binding.etZipcode.text.toString().trim()
        val country = binding.etCountry.text.toString().trim()
        val isDefault = binding.cbDefault.isChecked

        // Validate required fields
        if (name.isEmpty() || line1.isEmpty() || city.isEmpty() || state.isEmpty() || zipcode.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (editingAddress != null) {
            // Update existing address
            val updatedAddress = editingAddress!!.copy(
                fullName = name,
                addressLine1 = line1,
                addressLine2 = if (line2.isEmpty()) null else line2,
                city = city,
                state = state,
                pincode = zipcode,
                country = country,
                isDefault = isDefault
            )
            viewModel.updateAddress(updatedAddress.id, updatedAddress)
        } else {
            // Add new address
            val newAddress = Address(
                id = 0,
                userId = 0,
                fullName = name,
                phone = "",
                addressLine1 = line1,
                addressLine2 = if (line2.isEmpty()) null else line2,
                city = city,
                state = state,
                pincode = zipcode,
                country = country,
                addressType = Address.AddressType.HOME,
                isDefault = isDefault
            )
            viewModel.addAddress(newAddress)
        }
    }

    private fun observeViewModel() {
        viewModel.operationResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Address saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            result.onFailure { error ->
                Toast.makeText(this, error.message ?: "Error saving address", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.btnSave.isEnabled = !isLoading
        }
    }
}
