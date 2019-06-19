package com.example.deliverylist.network

import com.example.deliverylist.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WebServiceModel {

    companion object {
        val apiInterface: ApiInterface by lazy {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClientHelper.createOkHttpClient())
                .baseUrl(BuildConfig.API_URL)
                .build()
                .create(ApiInterface::class.java)
        }
    }
}