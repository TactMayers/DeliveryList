package com.example.deliverylist.network

import com.example.deliverylist.model.DeliveryItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API interface of delivery API. Defined as required by Retrofit.
 */
interface ApiInterface {
    @GET("/deliveries")
    fun getDeliveryList(
        @Query("offset") offset: Int?,
        @Query("limit") limit: Int?): Call<List<DeliveryItem>>
}