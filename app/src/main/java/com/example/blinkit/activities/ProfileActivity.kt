package com.example.blinkit.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.blinkit.databinding.ActivityProfileBinding
import com.example.blinkit.utils.SharedPrefsManager
import com.example.blinkit.utils.ThemeManager
import com.example.blinkit.viewmodels.AuthViewModel

/**
 * ProfileActivity - User profile and settings screen
 * Token is automatically injected by ApiClient interceptor
 */
class ProfileActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProfileBinding
    private lateinit var authViewModel: AuthViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupViewModel()
        setupClickListeners()
        loadUserProfile()
        setupThemeSwitch()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }
    
    private fun setupViewModel() {
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        
        authViewModel.profileResult.observe(this) { result ->
            result.onSuccess { user ->
                binding.tvUserName.text = user.name
                binding.tvUserEmail.text = user.email
                
                // Set avatar initial
                val initial = user.name.firstOrNull()?.uppercase() ?: "U"
                binding.tvAvatarInitial.text = initial
            }
            result.onFailure { error ->
                Toast.makeText(this, error.message ?: "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupClickListeners() {
        // Edit Profile
        binding.btnEditProfile.setOnClickListener {
            // TODO: Navigate to edit profile screen
            Toast.makeText(this, "Edit profile coming soon", Toast.LENGTH_SHORT).show()
        }
        
        // My Orders
        binding.cardMyOrders.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }
        
        // Saved Addresses
        binding.cardSavedAddresses.setOnClickListener {
            startActivity(Intent(this, AddressListActivity::class.java))
        }
        
        // About App
        binding.cardAboutApp.setOnClickListener {
            showAboutDialog()
        }
        
        // Logout
        binding.cardLogout.setOnClickListener {
            showLogoutDialog()
        }
    }
    
    private fun loadUserProfile() {
        authViewModel.getProfile()
    }
    
    private fun setupThemeSwitch() {
        // Set current theme state
        binding.switchTheme.isChecked = ThemeManager.isDarkTheme(this)
        
        // Handle theme toggle
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            SharedPrefsManager.saveTheme(this, isChecked)
            ThemeManager.applyTheme(this)
            recreate() // Recreate activity to apply theme
        }
    }
    
    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("About Blinkit Clone")
            .setMessage("""
                Blinkit Clone - Grocery Delivery App
                
                Version: 1.0.0
                
                A modern grocery delivery application built with:
                • Android (Kotlin)
                • MVVM Architecture
                • Material Design 3
                • Retrofit & Glide
                
                © 2025 Blinkit Clone. All rights reserved.
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun performLogout() {
        // Clear user session
        SharedPrefsManager.clearToken(this)
        
        // Navigate to login screen
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
