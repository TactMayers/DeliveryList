package com.example.deliverylist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.deliverylist.model.DeliveryItem

/**
 * Dao interface for delivery item database using Room library
 */
@Dao
interface DeliveryItemDao {

    @Query("SELECT * FROM delivery_item")
    fun getAllDeliveryItems(): List<DeliveryItem>

    @Query("SELECT * FROM delivery_item LIMIT :offset, :count")
    fun getPartialDeliveryItems(offset: Int, count: Int): List<DeliveryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(deliveryItems: List<DeliveryItem>)

    @Query("DELETE FROM delivery_item")
    fun deleteAll()
}