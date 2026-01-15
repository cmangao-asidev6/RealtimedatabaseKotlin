package com.example.realtimedatabasekotlin
import android.location.Geocoder

import java.util.Locale
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.realtimedatabasekotlin.databinding.ActivityReadDataBinding
import com.example.realtimedatabasekotlin.utils.Address

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ReadData : AppCompatActivity() {

    private lateinit var binding : ActivityReadDataBinding
    private lateinit var database : DatabaseReference
    private val Address by lazy { Address(this) }
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityReadDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val loggedInUser = sharedPref.getString("loggedInUser", null)
        val welcome = findViewById<TextView>(R.id.welcomeText)
        welcome.text = "Welcome $loggedInUser"





        binding.readdataBtn.setOnClickListener {




            val userName : String = binding.etusername.text.toString()
            if  (userName.isNotEmpty()){

                readData(userName)

            }else{

                Toast.makeText(this,"PLease enter the Username",Toast.LENGTH_SHORT).show()

            }

        }


        binding.floatingActionButton.setOnClickListener{
            confirmlogout()
        }
        // Navigate to ReadData activity
        val gotoUpdateBtn = findViewById<Button>(R.id.gotoUpdateBtn)
        gotoUpdateBtn.setOnClickListener {
            val intent = Intent(this, UpdateData::class.java)
            intent.putExtra("userName", loggedInUser)
            startActivity(intent)
        }

        //Navigate Back to Register
        val backToRegisterBtn = findViewById<Button>(R.id.gotoUserList)
        backToRegisterBtn.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }


    }
    private fun confirmlogout() {
        // Create an AlertDialog
        AlertDialog.Builder(this)
            .setTitle("Confirm logout?")
            .setPositiveButton("Yes") { _, _ ->
                val sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    clear() // Clear all saved data
                    apply() // Apply changes
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null) // Dismiss dialog on cancel
            .show()
    }



    private fun readData(userName: String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(userName).get().addOnSuccessListener {

            if (it.exists()){

                val firstname = it.child("firstName").value
                val lastName = it.child("lastName").value
                val age = it.child("age").value
                val latitude = it.child("latitude").value as? Double
                val longitude = it.child("longitude").value as? Double

                if (latitude != null && longitude != null) {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val address = Address.getAddressFromCoordinates(geocoder,latitude, longitude)


                    binding.tvLocation.text ="Location: ${address}"
                } else {
                    binding.tvLocation.text = "Location not available"
                }

                Toast.makeText(this,"Successfully Read",Toast.LENGTH_SHORT).show()
//                binding.etusername.text.clear()
                binding.tvFirstName.text ="First Name: ${firstname.toString()}"
                binding.tvLastName.text ="Last Name: ${lastName.toString()}"
                binding.tvAge.text ="Age: ${age.toString()}"

            } else {
                Toast.makeText(this,"User Doesn't Exist",Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
        }
    }






}