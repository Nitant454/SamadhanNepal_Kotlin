package com.example.samadhannepalapp.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Cloudinary once
        CloudinaryManager.init(applicationContext)

        // Directly open UserDashboardActivity
        startActivity(Intent(this, UserDashboardActivity::class.java))
        finish()
    }
}