package com.example.realtimedatabasekotlin

import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.realtimedatabasekotlin.databinding.ActivityRegisterBinding
import com.example.realtimedatabasekotlin.utils.Address
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: DatabaseReference
    private var latitude: Double? = null
    private var longitude: Double? = null
    private val locationHelper by lazy { LocationHelper(this) }
    private val Address by lazy { Address(this) }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check location permission and retrieve location
        locationHelper.checkAndRequestLocationPermission { location ->
            location?.let {
                latitude = it.latitude
                longitude = it.longitude
                binding.tvLatLong.text = "$latitude, $longitude"

                // Try to get the address from the location
                val geocoder = Geocoder(this, Locale.getDefault())
                val address = Address.getAddressFromCoordinates(geocoder, latitude!!, longitude!!)
                binding.tvLocation.text = address
            } ?: showToast("Location not available")
        }

        binding.loginTextView.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
        }

        binding.registerBtn.setOnClickListener {
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val age = binding.age.text.toString()
            val userName = binding.userName.text.toString()
            val password = binding.password.text.toString()

            if (latitude != null && longitude != null) {
                if (firstName.isNotEmpty() && lastName.isNotEmpty() && age.isNotEmpty() && userName.isNotEmpty() && password.isNotEmpty()) {
                    database = FirebaseDatabase.getInstance().getReference("Users")
                    val user = User(firstName, lastName, age, userName, password, latitude!!, longitude!!)
                    database.child(userName).setValue(user).addOnSuccessListener {
                        binding.firstName.text.clear()
                        binding.lastName.text.clear()
                        binding.age.text.clear()
                        binding.userName.text.clear()
                        binding.password.text.clear()

                        showToast("Successfully Saved")
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        startActivity(intent)
                    }.addOnFailureListener {
                        showToast("Failed to save user")
                    }
                } else {
                    showToast("Please fill in all the fields!")
                }
            } else {
                showToast("Failed to retrieve location data")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationHelper.handlePermissionResult(requestCode, grantResults) { location ->
            location?.let {
                latitude = it.latitude
                longitude = it.longitude
                binding.tvLatLong.text = "$latitude, $longitude"
                val geocoder = Geocoder(this, Locale.getDefault())
                val address = Address.getAddressFromCoordinates(geocoder, latitude!!, longitude!!)
                binding.tvLocation.text = address
            } ?: showToast("Location not available")
        }
    }
}

