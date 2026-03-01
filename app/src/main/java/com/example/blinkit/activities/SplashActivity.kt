package com.example.blinkit.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.blinkit.R
import com.example.blinkit.utils.SharedPrefsManager

class SplashActivity : AppCompatActivity() {
    
    private val splashDelay = 2000L // 2 seconds
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        // Check if user is logged in after delay
        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, splashDelay)
    }
    
    private fun checkLoginStatus() {
        val token = SharedPrefsManager.getToken(this)
        
        val intent = if (token != null) {
            // User is logged in, navigate to Home
            Intent(this, HomeActivity::class.java)
        } else {
            // User not logged in, navigate to Login
            Intent(this, LoginActivity::class.java)
        }
        
        startActivity(intent)
        finish()
    }
}