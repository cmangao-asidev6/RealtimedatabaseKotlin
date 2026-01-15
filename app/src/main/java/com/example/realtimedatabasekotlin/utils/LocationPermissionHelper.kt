package com.example.realtimedatabasekotlin

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
class LocationHelper(private val activity: AppCompatActivity) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    fun checkAndRequestLocationPermission(callback: (Location?) -> Unit) {
        if (hasLocationPermission()) {
            getLastKnownLocation(callback)
        } else {
            requestLocationPermission(callback)
        }
    }

    private fun hasLocationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestLocationPermission(callback: (Location?) -> Unit) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun getLastKnownLocation(callback: (Location?) -> Unit) {
        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    callback(location)
                }
                .addOnFailureListener {
                    callback(null)
                }
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    fun handlePermissionResult(requestCode: Int, grantResults: IntArray, callback: (Location?) -> Unit) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastKnownLocation(callback)
            } else {
                callback(null) // Permission denied, return null
            }
        }
    }
}

