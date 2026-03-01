package com.example.samadhannepalapp.view

import java.text.SimpleDateFormat
import java.util.*

fun timeAgo(timestamp: Long): String {
    if (timestamp <= 0L) return "Just now"

    val now = System.currentTimeMillis()
    val diff = now - timestamp

    val minutes = diff / 60000
    val hours = diff / 3600000
    val days = diff / 86400000

    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "$minutes minutes ago"
        hours < 24 -> "$hours hours ago"
        days < 7 -> "$days days ago"
        else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
    }
}
