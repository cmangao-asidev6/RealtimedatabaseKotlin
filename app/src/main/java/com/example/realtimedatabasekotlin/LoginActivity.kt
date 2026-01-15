package com.example.realtimedatabasekotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.realtimedatabasekotlin.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Users")

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
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
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
                    // Save username to SharedPreferences
                    val sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("loggedInUser", userName)
                        apply()
                    }

                    // Check if the password matches
                    if (storedPassword == password) {
                        // Login successful
                        Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to HomeActivity
                        val intent = Intent(this@LoginActivity, ReadData::class.java)
                        intent.putExtra("userName", userName)
                        startActivity(intent)
                        finish()
                    } else {
                        // Incorrect password
                        Toast.makeText(this@LoginActivity, "Invalid password!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Username not found
                    Toast.makeText(this@LoginActivity, "User not found!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}