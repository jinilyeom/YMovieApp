package com.ymovie.app.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApiClient {
    private const val TAG = "RetrofitApiClient"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(buildOkHttpClient())
        .build()
    val retrofitInstance get() = retrofit

    private fun buildOkHttpClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", NetworkConstants.AUTHENTICATION_TYPE + NetworkConstants.TMDB_ACCESS_TOKEN_AUTH).build()

            chain.proceed(request)
        }

        val httpLoggingInterceptor = HttpLoggingInterceptor { log ->
            Log.d(TAG, log)
        }.apply {
            val level = HttpLoggingInterceptor.Level.BODY
            setLevel(level)
        }

        return OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            addInterceptor(httpLoggingInterceptor)
        }.build()
    }
}