package com.example.realtimedatabasekotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.realtimedatabasekotlin.R

class ViewPagerAdapter(private val context: Context) : PagerAdapter() {

    private val images = intArrayOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4
    )

    private val headings = intArrayOf(
        R.string.heading_one,
        R.string.heading_two,
        R.string.heading_three,
        R.string.heading_fourth
    )

    private val descriptions = intArrayOf(
        R.string.desc_one,
        R.string.desc_two,
        R.string.desc_three,
        R.string.desc_fourth
    )

    override fun getCount(): Int {
        return headings.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.slider_layout, container, false)

        val slideImage: ImageView = view.findViewById(R.id.titleImage)
        val slideHeading: TextView = view.findViewById(R.id.texttitle)
        val slideDescription: TextView = view.findViewById(R.id.textdeccription)

        slideImage.setImageResource(images[position])
        slideHeading.setText(headings[position])
        slideDescription.setText(descriptions[position])

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}
