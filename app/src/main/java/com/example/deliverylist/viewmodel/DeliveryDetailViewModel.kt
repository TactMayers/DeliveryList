package com.example.deliverylist.viewmodel

import androidx.lifecycle.ViewModel;
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DeliveryDetailViewModel : ViewModel() {

    fun configureGoogleMap(map: GoogleMap, lat: Double, lng: Double, title: String, zoom: Float = 16f, bearing: Float = 0f) {
        val latLng = LatLng(lat, lng)
        val cameraPosition = CameraPosition.builder()
            .target(latLng)
            .zoom(zoom)
            .bearing(bearing)
            .build()
        map.apply {
            addMarker(MarkerOptions().position(latLng).title(title))
            moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }
}
