package com.example.samadhannepalapp.view

data class Report(
    val imageUrl: String? = null,
    val ward: String,
    val category: String,
    val description: String,
    var status: String = "Pending",
    val timestamp: Long = System.currentTimeMillis()
)
