package com.capstone.educollab1.ui.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    // Base URL untuk API utama
    private const val BASE_URL = "https://educollab-capstone.et.r.appspot.com"

    // Base URL untuk API Machine Learning
    private const val ML_BASE_URL = "https://predict-app-dot-educollab-capstone.et.r.appspot.com"

    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val mlRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ML_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Service untuk API utama
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // Service untuk API Machine Learning
    val mlApiService: MLApiService by lazy {
        mlRetrofit.create(MLApiService::class.java)
    }
}
