package com.example.deliverylist.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.deliverylist.model.DeliveryItem

/**
 * Room Database for delivery item
 */
@Database(entities = [DeliveryItem::class], version = 1)
abstract class DeliveryRoomDatabase : RoomDatabase() {
    abstract fun deliveryItemDao(): DeliveryItemDao

    companion object {
        @Volatile
        private var INSTANCE: DeliveryRoomDatabase? = null

        fun getDatabase(context: Context): DeliveryRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DeliveryRoomDatabase::class.java,
                    "delivery_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}