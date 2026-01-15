package com.example.realtimedatabasekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.realtimedatabasekotlin.databinding.ActivityUpdateDataBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateData : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateDataBinding
    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val userData = intent.getStringExtra("userName")
        binding.userName.setText(userData)


        binding.updateBtn.setOnClickListener {

            val userName = userData.toString()
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastname.text.toString()
            val age = binding.age.text.toString()

            updateData(userName,firstName,lastName,age)

        }

        binding.backToSearchUser.setOnClickListener{
            val intent = Intent(this, ReadData::class.java)
            startActivity(intent)
        }

    }

    private fun updateData(userName: String, firstName: String, lastName: String, age: String) {

        database = FirebaseDatabase.getInstance().getReference("Users")
        val user = mapOf<String,String>(
                "firstName" to firstName,
                "lastName" to lastName,
                "age" to age
        )

        database.child(userName).updateChildren(user).addOnSuccessListener {

            binding.userName.text.clear()
            binding.firstName.text.clear()
            binding.lastname.text.clear()
            binding.age.text.clear()
            Toast.makeText(this,"Successfuly Updated",Toast.LENGTH_SHORT).show()


        }.addOnFailureListener{

            Toast.makeText(this,"Failed to Update",Toast.LENGTH_SHORT).show()

        }}
}