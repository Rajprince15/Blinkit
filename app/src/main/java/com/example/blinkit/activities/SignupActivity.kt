package com.example.blinkit.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.blinkit.R
import com.example.blinkit.databinding.ActivitySignupBinding
import com.example.blinkit.models.SignupRequest
import com.example.blinkit.utils.SharedPrefsManager
import com.example.blinkit.viewmodels.AuthViewModel

class SignupActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySignupBinding
    private lateinit var authViewModel: AuthViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        
        setupClickListeners()
        observeViewModel()
    }
    
    private fun setupClickListeners() {
        binding.btnSignup.setOnClickListener {
            validateAndSignup()
        }
        
        binding.tvLogin.setOnClickListener {
            finish() // Go back to login
        }
    }
    
    private fun validateAndSignup() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        
        // Reset errors
        binding.etName.error = null
        binding.etEmail.error = null
        binding.etPhone.error = null
        binding.etPassword.error = null
        binding.etConfirmPassword.error = null
        
        // Validate name
        if (name.isEmpty()) {
            binding.etName.error = getString(R.string.error_empty_name)
            binding.etName.requestFocus()
            return
        }
        
        // Validate email
        if (email.isEmpty()) {
            binding.etEmail.error = getString(R.string.error_empty_email)
            binding.etEmail.requestFocus()
            return
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = getString(R.string.error_invalid_email)
            binding.etEmail.requestFocus()
            return
        }
        
        // Validate phone
        if (phone.isEmpty()) {
            binding.etPhone.error = getString(R.string.error_empty_phone)
            binding.etPhone.requestFocus()
            return
        }
        
        if (phone.length != 10) {
            binding.etPhone.error = getString(R.string.error_invalid_phone)
            binding.etPhone.requestFocus()
            return
        }
        
        // Validate password
        if (password.isEmpty()) {
            binding.etPassword.error = getString(R.string.error_empty_password)
            binding.etPassword.requestFocus()
            return
        }
        
        if (password.length < 6) {
            binding.etPassword.error = getString(R.string.error_short_password)
            binding.etPassword.requestFocus()
            return
        }
        
        // Validate confirm password
        if (confirmPassword.isEmpty()) {
            binding.etConfirmPassword.error = getString(R.string.error_empty_password)
            binding.etConfirmPassword.requestFocus()
            return
        }
        
        if (password != confirmPassword) {
            binding.etConfirmPassword.error = getString(R.string.error_passwords_not_match)
            binding.etConfirmPassword.requestFocus()
            return
        }
        
        // All validations passed, proceed with signup
        performSignup(name, email, phone, password)
    }
    
    private fun performSignup(name: String, email: String, phone: String, password: String) {
        val signupRequest = SignupRequest(name, email, phone, password)
        authViewModel.signup(signupRequest)
    }
    
    private fun observeViewModel() {
        authViewModel.signupResult.observe(this) { result ->
            result.onSuccess { response ->
                // Save token
                SharedPrefsManager.saveToken(this, response.token)
                
                // Show success message
                Toast.makeText(this, getString(R.string.signup_success), Toast.LENGTH_SHORT).show()
                
                // Navigate to Home
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            
            result.onFailure { error ->
                Toast.makeText(this, error.message ?: "Signup failed", Toast.LENGTH_LONG).show()
            }
        }
        
        authViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSignup.isEnabled = !isLoading
        }
    }
}