package com.example.blinkit

import android.app.Application
import com.example.blinkit.api.ApiClient

/**
 * Application class for global initialization
 */
class BlinkitApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize ApiClient with application context
        ApiClient.initialize(this)
    }
}
