package com.example.blinkit.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.blinkit.R
import com.example.blinkit.databinding.ActivityLoginBinding
import com.example.blinkit.utils.SharedPrefsManager
import com.example.blinkit.viewmodels.AuthViewModel

/**
 * LoginActivity - User login screen
 * Token is saved after successful login and automatically used by ApiClient interceptor
 */
class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        
        setupClickListeners()
        observeViewModel()
    }
    
    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            validateAndLogin()
        }
        
        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
        
        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Feature coming soon", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun validateAndLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        
        // Reset errors
        binding.etEmail.error = null
        binding.etPassword.error = null
        
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
        
        // All validations passed, proceed with login
        performLogin(email, password)
    }
    
    private fun performLogin(email: String, password: String) {
        authViewModel.login(email, password)
    }
    
    private fun observeViewModel() {
        authViewModel.loginResult.observe(this) { result ->
            result.onSuccess { response ->
                // Save token to SharedPreferences
                SharedPrefsManager.saveToken(this, response.token)
                
                // Show success message
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                
                // Navigate to Home
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            
            result.onFailure { error ->
                Toast.makeText(this, error.message ?: "Login failed", Toast.LENGTH_LONG).show()
            }
        }
        
        authViewModel.loading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
            binding.etEmail.isEnabled = !isLoading
            binding.etPassword.isEnabled = !isLoading
        }
    }
}
