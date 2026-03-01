package com.example.blinkit.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blinkit.R
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Main container activity with bottom navigation
 * Manages navigation between Home, Categories, Cart, and Profile
 */
class MainContainerActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)

        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Set default selected item
        val startDestination = intent.getStringExtra("start_destination") ?: "home"
        when (startDestination) {
            "home" -> bottomNavigation.selectedItemId = R.id.navigation_home
            "cart" -> bottomNavigation.selectedItemId = R.id.navigation_cart
            "profile" -> bottomNavigation.selectedItemId = R.id.navigation_profile
        }

        // Handle bottom navigation item selection
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    })
                    true
                }
                R.id.navigation_categories -> {
                    // TODO: Navigate to CategoryListActivity when created
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.navigation_cart -> {
                    startActivity(Intent(this, CartActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    })
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    })
                    true
                }
                else -> false
            }
        }

        // Navigate to the initial destination
        when (startDestination) {
            "home" -> startActivity(Intent(this, HomeActivity::class.java))
            "cart" -> startActivity(Intent(this, CartActivity::class.java))
            "profile" -> startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Update cart badge count here if needed
        // TODO: Implement cart badge counter
    }
}
