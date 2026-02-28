package com.example.blinkit.utils

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

/**
 * Theme Manager for handling app theme toggle
 */
object ThemeManager {

    /**
     * Apply theme based on saved preference
     */
    fun applyTheme(context: Context) {
        val prefs = SharedPrefsManager.getInstance(context)
        val isDark = prefs.isDarkTheme()

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    /**
     * Toggle theme and apply
     */
    fun toggleTheme(context: Context) {
        val prefs = SharedPrefsManager.getInstance(context)
        val currentlyDark = prefs.isDarkTheme()
        val newTheme = !currentlyDark

        prefs.saveTheme(newTheme)
        applyTheme(context)

        // Recreate activity to apply theme
        if (context is Activity) {
            context.recreate()
        }
    }

    /**
     * Check if dark theme is enabled
     */
    fun isDarkTheme(context: Context): Boolean {
        val prefs = SharedPrefsManager.getInstance(context)
        return prefs.isDarkTheme()
    }
}
