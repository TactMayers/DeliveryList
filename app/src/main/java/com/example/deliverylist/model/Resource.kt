package com.example.deliverylist.model

/**
 * A wrapper class for data models retrieved from database/network.
 * Provides extra information regarding to status of the retrieval.
 */
data class Resource<T> (
    val isSuccess: Boolean,
    val responseCode: Int,
    val data: T
)