package com.example.realtimedatabasekotlin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.realtimedatabasekotlin.databinding.ActivityLoginBinding
import com.example.realtimedatabasekotlin.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is already logged in
        val sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val loggedInUser = sharedPref.getString("loggedInUser", null)

        if (loggedInUser != null) {
            // User is already logged in, set isFirstLaunch to false and redirect to ReadData activity
            AppPrefs.isFirstLaunch = false
            val intent = Intent(this@MainActivity, ReadData::class.java)
            intent.putExtra("userName", loggedInUser)
            startActivity(intent)
            finish()
        } else {
            // Check if onboarding is needed
            if (AppPrefs.isFirstLaunch) {
                // Launch the onboarding activity
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            } else {
                // Proceed to the login screen
                binding = ActivityLoginBinding.inflate(layoutInflater)
                setContentView(binding.root)
                database = FirebaseDatabase.getInstance().getReference("Users")
                setupFirebaseListeners()
            }
        }
    }



    private fun setupFirebaseListeners() {
        binding.loginButton.setOnClickListener {
            val userName = binding.userNameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (userName.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(userName, password)
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupTextView.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun authenticateUser(userName: String, password: String) {
        // Query the database for the entered username
        database.child(userName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Retrieve user data
                    val storedPassword = snapshot.child("password").getValue(String::class.java)

                    // Check if the password matches
                    if (storedPassword == password) {

                        val sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("loggedInUser", userName)
                            apply()
                        }
                        // Login successful
                        Toast.makeText(this@MainActivity, "Login Successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to HomeActivity
                        val intent = Intent(this@MainActivity, ReadData::class.java)
                        intent.putExtra("userName", userName)
                        startActivity(intent)
                        finish()
                    } else {
                        // Incorrect password
                        Toast.makeText(this@MainActivity, "Invalid password!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Username not found
                    Toast.makeText(this@MainActivity, "User not found!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

//    private fun setupFirebaseListeners() {
//        // Register button logic
//        binding.registerBtn.setOnClickListener {
//            val firstName = binding.firstName.text.toString()
//            val lastName = binding.lastName.text.toString()
//            val age = binding.age.text.toString()
//            val userName = binding.userName.text.toString()
//            val password = binding.password.text.toString()
//
//            database = FirebaseDatabase.getInstance().getReference("Users")
//            val User = User(firstName,lastName,age,userName, password)
//            database.child(userName).setValue(User).addOnSuccessListener {
//
//                binding.firstName.text.clear()
//                binding.lastName.text.clear()
//                binding.age.text.clear()
//                binding.userName.text.clear()
//                binding.password.text.clear()
//
//                Toast.makeText(this,"Successfully Saved",Toast.LENGTH_SHORT).show()
//
//            }.addOnFailureListener{
//
//                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
//
//            }
//        }
//
//        // Navigate to ReadData activity
//        binding.gotoLoginBtn.setOnClickListener {
//            val intent = Intent(this, ReadData::class.java)
//            startActivity(intent)
//        }
//    }
}
