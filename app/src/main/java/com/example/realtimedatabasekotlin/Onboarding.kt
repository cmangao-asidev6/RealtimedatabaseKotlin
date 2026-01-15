package com.example.realtimedatabasekotlin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import android.widget.Button
import android.widget.LinearLayout
import com.example.realtimedatabasekotlin.adapters.ViewPagerAdapter

class OnboardingActivity : AppCompatActivity() {

    private lateinit var slideViewPager: ViewPager
    private lateinit var dotLayout: LinearLayout
    private lateinit var backBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var skipBtn: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_main)

        slideViewPager = findViewById(R.id.slideViewPager)
        dotLayout = findViewById(R.id.indicator_layout)
        backBtn = findViewById(R.id.backbtn)
        nextBtn = findViewById(R.id.nextbtn)
        skipBtn = findViewById(R.id.skipButton)

        val viewPagerAdapter = ViewPagerAdapter(this)
        slideViewPager.adapter = viewPagerAdapter

        backBtn.setOnClickListener {
            if (getItem(-1) >= 0) {
                slideViewPager.setCurrentItem(getItem(-1), true)
            }
        }

        nextBtn.setOnClickListener {
            if (getItem(1) < 3) {
                slideViewPager.setCurrentItem(getItem(1), true)
            } else {
                markOnboardingCompleted()
            }
        }

        skipBtn.setOnClickListener {
            markOnboardingCompleted()
        }
    }

//    private fun markOnboardingCompleted() {
//        sharedPreferences = getSharedPreferences("OnboardingPrefs", MODE_PRIVATE)
//        with(sharedPreferences.edit()) {
//            putBoolean("isFirstLaunch", false)
//            apply()
//        }
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//    }

    private fun markOnboardingCompleted() {
        // Mark the onboarding as completed by setting the static flag to false
        AppPrefs.isFirstLaunch = false

        // Launch the main activity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }




    private fun getItem(i: Int): Int {
        return slideViewPager.currentItem + i
    }

    // Handle back button press to move to the previous slide
    override fun onBackPressed() {
        if (slideViewPager.currentItem > 0) {
            slideViewPager.setCurrentItem(slideViewPager.currentItem - 1, true)
        } else {
            super.onBackPressed() // If we're at the first slide, proceed with the default behavior
        }
    }
}
