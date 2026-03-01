package com.example.blinkit.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.blinkit.databinding.ActivityAddEditAddressBinding
import com.example.blinkit.models.Address
import com.example.blinkit.utils.SharedPrefsManager
import com.example.blinkit.viewmodels.AddressViewModel

class AddEditAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditAddressBinding
    private lateinit var viewModel: AddressViewModel
    private var editingAddress: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AddressViewModel::class.java]

        // check if we're editing
        intent.getStringExtra("address_json")?.let { json ->
            editingAddress = com.google.gson.Gson().fromJson(json, Address::class.java)
            populateFields()
        }

        binding.btnSave.setOnClickListener {
            saveAddress()
        }

        observe()
    }

    private fun populateFields() {
        editingAddress?.let { addr ->
            binding.etName.setText(addr.fullName)
            binding.etLine1.setText(addr.addressLine1)
            binding.etLine2.setText(addr.addressLine2)
            binding.etCity.setText(addr.city)
            binding.etState.setText(addr.state)
            binding.etZipcode.setText(addr.pincode)
            binding.etCountry.setText("India")
            binding.cbDefault.isChecked = addr.isDefault
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

        if (name.isEmpty() || line1.isEmpty() || city.isEmpty() || state.isEmpty() || zipcode.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val token = getToken()
        if (editingAddress != null) {
            val addr = editingAddress!!.copy(
                fullName = name,
                addressLine1 = line1,
                addressLine2 = line2,
                city = city,
                state = state,
                pincode = zipcode,
                country = country,
                isDefault = isDefault
            )
            viewModel.updateAddress(token, addr.id, addr)
        } else {
            val newAddr = Address(
                id = 0,
                userId = 0,
                fullName = name,
                phone = "",
                addressLine1 = line1,
                addressLine2 = if (line2.isEmpty()) null else line2,
                city = city,
                state = state,
                pincode = zipcode,
                addressType = Address.AddressType.HOME,
                isDefault = isDefault
            )
            viewModel.addAddress(token, newAddr)
        }
    }

    private fun observe() {
        viewModel.operationResult.observe(this) { res ->
            res.onSuccess {
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                finish()
            }
            res.onFailure { err ->
                Toast.makeText(this, err.message ?: "Error saving", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getToken(): String = SharedPrefsManager.getToken(this) ?: ""
}
