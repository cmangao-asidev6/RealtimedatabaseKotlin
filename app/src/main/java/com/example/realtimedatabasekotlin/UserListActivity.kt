package com.example.realtimedatabasekotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserListActivity : AppCompatActivity() {


    private lateinit var userListView: ListView
    private val users = mutableListOf<User>()
    private lateinit var adapter: UserListAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userlist)

        userListView = findViewById(R.id.userListView)
//        adapter = UserListAdapter(this, users)
        adapter = UserListAdapter(this, users) { userToDelete ->
            confirmAndDeleteUser(userToDelete)
        }
        userListView.adapter = adapter

        // Initialize the Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Fetch users from Firebase
        fetchUsersFromFirebase()
//            loadMockData()

        //Navigate Back to Register
        val backButtonInUser = findViewById<Button>(R.id.backButtonInUser)
        backButtonInUser.setOnClickListener {
            val intent = Intent(this, ReadData::class.java)
            startActivity(intent)
        }
    }

    // Mock data method
//    private fun loadMockData() {
//        users.apply {
//            add(User("John", "Doe", "28", "johndoe", ))
//            add(User("Jane", "Smith", "32", "janesmith", ))
//            add(User("Michael", "Johnson", "24", "mjohnson", ))
//            add(User("Emily", "Davis", "29", "emilyd", ))
//        }
//        adapter.notifyDataSetChanged()  // Update the adapter with the mock data
//    }
    private fun confirmAndDeleteUser(user: User) {
        val userName = user.userName ?: return  // Return early if userName is null

        // Create an AlertDialog
        AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete $userName?")
            .setPositiveButton("Delete") { _, _ ->
                // If user confirms, proceed with deletion
                deleteUser(user)
            }
            .setNegativeButton("Cancel", null) // Dismiss dialog on cancel
            .show()
    }

    private fun deleteUser(user: User) {
        val userName = user.userName ?: return  // Return early if userName is null
        database.child(userName).removeValue().addOnSuccessListener {
            Toast.makeText(this, "$userName deleted successfully", Toast.LENGTH_SHORT).show()
            users.remove(user)  // Remove the user from the list
            adapter.notifyDataSetChanged()  // Refresh the list view
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to delete $userName", Toast.LENGTH_SHORT).show()
        }
    }




    private fun fetchUsersFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        user?.let {
                            users.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@UserListActivity, "No users found", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@UserListActivity,
                    "Failed to load users: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}


