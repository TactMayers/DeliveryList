package com.example.deliverylist.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Helper class for creating an OkHttpClient
 */
class OkHttpClientHelper {

    companion object {
        fun createOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            return builder.build()
        }
    }
}