package com.example.blinkit.api
import android.content.Context
import com.example.blinkit.utils.Constants
import com.example.blinkit.utils.SharedPrefsManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit API Client singleton with automatic token injection
 */
object ApiClient {

    private var retrofit: Retrofit? = null
    private var appContext: Context? = null

    /**
     * Initialize ApiClient with application context
     * Call this from Application class onCreate
     */
    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    /**
     * Auth interceptor that automatically adds JWT token to requests
     */
    private val authInterceptor = Interceptor { chain ->
        val token = appContext?.let { SharedPrefsManager.getToken(it) }
        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        chain.proceed(request)
    }


    /**
     * Get Retrofit instance
     */
    private fun getRetrofitInstance(): Retrofit {
        if (retrofit == null) {
            // Logging interceptor for debugging
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            // OkHttp client with auth interceptor, timeout and logging
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor) // Add auth interceptor first
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            // Build Retrofit instance
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    /**
     * Get API Service instance
     */
    val apiService: ApiService by lazy {
        getRetrofitInstance().create(ApiService::class.java)
    }
}
