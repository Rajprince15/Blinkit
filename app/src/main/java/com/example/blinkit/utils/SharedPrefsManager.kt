package com.example.blinkit.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreferences Manager for storing user data and app settings
 */
class SharedPrefsManager private constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "blinkit_prefs"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_THEME = "app_theme"
        private const val THEME_LIGHT = "light"
        private const val THEME_DARK = "dark"

        @Volatile
        private var instance: SharedPrefsManager? = null

        fun getInstance(context: Context): SharedPrefsManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPrefsManager(context.applicationContext).also { instance = it }
            }
        }
    }

    // Save authentication token
    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    // Get authentication token
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    // Save user data
    fun saveUserData(userId: Int, name: String, email: String, phone: String?) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_PHONE, phone)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    // Get user ID
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    // Get user name
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    // Get user email
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    // Get user phone
    fun getUserPhone(): String? {
        return prefs.getString(KEY_USER_PHONE, null)
    }

    // Check if user is logged in
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Save theme preference
    fun saveTheme(isDark: Boolean) {
        prefs.edit().putString(KEY_THEME, if (isDark) THEME_DARK else THEME_LIGHT).apply()
    }

    // Get theme preference
    fun isDarkTheme(): Boolean {
        return prefs.getString(KEY_THEME, THEME_LIGHT) == THEME_DARK
    }

    // Clear all user data (logout)
    fun clearUserData() {
        prefs.edit().apply {
            remove(KEY_TOKEN)
            remove(KEY_USER_ID)
            remove(KEY_USER_NAME)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_PHONE)
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
    }

    // Clear all data
    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
