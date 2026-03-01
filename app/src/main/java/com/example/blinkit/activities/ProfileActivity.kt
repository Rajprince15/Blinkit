"package com.example.blinkit.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.blinkit.databinding.ActivityProfileBinding
import com.example.blinkit.utils.SharedPrefsManager
import com.example.blinkit.utils.ThemeManager
import com.example.blinkit.viewmodels.AuthViewModel

class ProfileActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProfileBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sharedPrefsManager: SharedPrefsManager
   
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sharedPrefsManager = SharedPrefsManager.getInstance(this)

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
        
        authViewModel.user.observe(this) { user ->
            user?.let {
                binding.tvUserName.text = it.name
                binding.tvUserEmail.text = it.email
                
                // Set avatar initial
                val initial = it.name.firstOrNull()?.uppercase() ?: \"U\"
                binding.tvAvatarInitial.text = initial
            }
        }
    }
    
    private fun setupClickListeners() {
        // Edit Profile
        binding.btnEditProfile.setOnClickListener {
            // TODO: Navigate to edit profile screen
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
        authViewModel.getUserProfile()
    }
    
    private fun setupThemeSwitch() {
        // Set current theme state
        binding.switchTheme.isChecked = ThemeManager.isDarkTheme(this)
        
        // Handle theme toggle
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            sharedPrefsManager.saveTheme(isChecked)
            ThemeManager.applyTheme(this)
            recreate() // Recreate activity to apply theme
        }
    }
    
    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle(\"About Blinkit Clone\")
            .setMessage(\"\"\"
                Blinkit Clone - Grocery Delivery App
                
                Version: 1.0.0
                
                A modern grocery delivery application built with:
                • Android (Kotlin)
                • MVVM Architecture
                • Material Design 3
                • Retrofit & Glide
                
                © 2025 Blinkit Clone. All rights reserved.
            \"\"\".trimIndent())
            .setPositiveButton(\"OK\", null)
            .show()
    }
    
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle(\"Logout\")
            .setMessage(\"Are you sure you want to logout?\")
            .setPositiveButton(\"Logout\") { _, _ ->
                logout()
            }
            .setNegativeButton(\"Cancel\", null)
            .show()
    }
    
    private fun logout() {
        // Clear user session
        sharedPrefsManager.clearToken()
        
        // Navigate to login screen
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
"