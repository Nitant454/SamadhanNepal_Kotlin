package com.example.samadhannepalapp.view

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CloudinaryManager.init(this)
    }
}