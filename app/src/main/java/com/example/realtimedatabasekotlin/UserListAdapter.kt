package com.example.realtimedatabasekotlin

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.realtimedatabasekotlin.utils.Address
import java.util.Locale

class UserListAdapter(
    private val context: Context,
    private val users: MutableList<User>,
    private val onDelete: (User) -> Unit
) : BaseAdapter() {

    private val geocoder = Geocoder(context, Locale.getDefault())
    private val addressHelper = Address(context as AppCompatActivity)
    override fun getCount(): Int = users.size

    override fun getItem(position: Int): Any = users[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.row_user, parent, false)

        val tvFirstName = view.findViewById<TextView>(R.id.tvFirstName)
        val tvLastName = view.findViewById<TextView>(R.id.tvLastName)
        val tvAge = view.findViewById<TextView>(R.id.tvAge)
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        val ivDelete = view.findViewById<ImageView>(R.id.ivDelete)
        val tvLocation = view.findViewById<TextView>(R.id.tvLocation)



        val user = users[position]
        val latitude = user.latitude ?: 0.0
        val longitude = user.longitude ?: 0.0
        tvUserName.text = "Username: ${user.userName}"
        tvFirstName.text = "First Name: ${user.firstName}"
        tvLastName.text = "Last Name: ${user.lastName}"
        tvAge.text = "Age: ${user.age}"

        val address = try {
            addressHelper.getAddressFromCoordinates(geocoder, latitude, longitude)
        } catch (e: Exception) {
            "Unable to retrieve address"
        }
        tvLocation.text = "Location: $address"


        ivDelete.setOnClickListener {
            onDelete(user)
        }

        return view
    }
}
