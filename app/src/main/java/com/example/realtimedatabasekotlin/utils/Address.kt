package com.example.realtimedatabasekotlin.utils

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity

class Address(private val activity: AppCompatActivity){
    public fun getAddressFromCoordinates(geocode: Geocoder, latitude: Double, longitude: Double): String {

        val addresses: List<Address> = geocode.getFromLocation(latitude, longitude, 1)!!

        return if (addresses.isNotEmpty()) {
            val address = addresses[0]
            // You can customize the address to display (e.g., city, street, country)
            "${address.adminArea}, ${address.countryName}"
        } else {
            "No address found"
        }
    }
}
