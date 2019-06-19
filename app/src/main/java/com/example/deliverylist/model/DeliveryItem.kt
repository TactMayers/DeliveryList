package com.example.deliverylist.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Delivery item model
 */
@Entity(tableName = "delivery_item")
data class DeliveryItem (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @Embedded
    val location: Location
) : Parcelable {

    constructor(parcel: Parcel) : this (
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Location::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(description)
        parcel.writeString(imageUrl)
        parcel.writeParcelable(location, flags)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<DeliveryItem> {

        override fun createFromParcel(parcel: Parcel): DeliveryItem {
            return DeliveryItem(parcel)
        }

        override fun newArray(size: Int): Array<DeliveryItem?> {
            return arrayOfNulls(size)
        }
    }
}