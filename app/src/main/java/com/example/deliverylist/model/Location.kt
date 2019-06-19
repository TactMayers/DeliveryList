package com.example.deliverylist.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo

/**
 * Location model
 */
data class Location (
    @ColumnInfo(name = "lat")
    val lat: String,
    @ColumnInfo(name = "lng")
    val lng: String,
    @ColumnInfo(name = "address")
    val address: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(lat)
        parcel.writeString(lng)
        parcel.writeString(address)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Location> {

        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }
}