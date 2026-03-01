package com.example.samadhannepalapp.view

import com.example.samadhannepalapp.R
import android.content.Intent
import androidx.compose.ui.res.painterResource
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.samadhannepalapp.model.UserIssueModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AuthorityDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthorityDashboardScreen(
                onLogout = {
                    //  After logout, navigate to login page
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

            )
        }
    }
}

@Composable
fun AuthorityDashboardScreen(onLogout: () -> Unit = {}) {
    val database = FirebaseDatabase.getInstance().reference.child("UserIssues")

    val context = LocalContext.current

    var issues by remember { mutableStateOf(listOf<UserIssueModel>()) }

    var selectedFilter by remember { mutableStateOf("Pending") } // 🔹 Default filter

    // Logout confirmation dialog state
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Listen for real-time changes
    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<UserIssueModel>()

                for (child in snapshot.children) {
                    if (child.value is Map<*, *>) {
                        val issue = child.getValue(UserIssueModel::class.java)

                        if (issue != null) {
                            val issueWithId = issue.copy(id = child.key ?: "")
                            val safeIssue =
                                if (issueWithId.timestamp == 0L) issueWithId.copy(timestamp = System.currentTimeMillis()) else issueWithId
                            list.add(safeIssue)
                        }
                    }
                }
                issues = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    val pendingCount = issues.count { it.status == "Pending" }
    val inProgressCount = issues.count { it.status == "In Progress" }
    val resolvedCount = issues.count { it.status == "Resolved" }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // Background image
        Image(
            painter = painterResource(R.drawable.registerpage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // Title + Logout button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Authority Dashboard",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge
                )

                IconButton(onClick = { showLogoutDialog = true  }) {
                    Icon(
                        painter = painterResource(R.drawable.logout), // use a logout icon drawable
                        contentDescription = "Logout",
                        tint = Color(0xFFFFA726)
                    )
                }
            }

            //  Filter Tabs
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                listOf(
                    "Pending" to pendingCount,
                    "In Progress" to inProgressCount,
                    "Resolved" to resolvedCount
                ).forEach { (filter, count) ->
                    Button(
                        onClick = { selectedFilter = filter },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedFilter == filter) Color(0xFFFFA726) else Color(
                                0xFF1E3A8A
                            )
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("$filter ($count)", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val filteredIssues = issues.filter { it.status == selectedFilter }

            if (filteredIssues.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No $selectedFilter issues", color = Color.LightGray)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    items(filteredIssues) { issue ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp), //  smaller padding for smaller cards
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1E3A8A) //  darker gray
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                if (!issue.imageUrl.isNullOrEmpty()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(issue.imageUrl),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp), //  smaller image height
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                // Row for issue info and status indicator
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f) //  VERY IMPORTANT
                                            .padding(end = 8.dp)
                                    ) {
                                        Text(issue.category, color = Color.White)

                                        Text(
                                            issue.description,
                                            color = Color.White,
                                        )

                                        Text("Ward: ${issue.ward}", color = Color.White)
                                    }

                                    // Status indicator on right
                                    val statusColor = when (issue.status) {
                                        "Pending" -> Color(0xFFEF4444) // Red
                                        "In Progress" -> Color(0xFFFACC15) // Yellow
                                        "Resolved" -> Color(0xFF22C55E) // Green
                                        else -> Color.Gray
                                    }

                                    Box(
                                        modifier = Modifier
                                            .background(statusColor, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = issue.status,
                                            color = Color.White
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Buttons based on status
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    when (issue.status) {
                                        "Pending" -> {
                                            Button(
                                                onClick = {
                                                    if (issue.id.isNotEmpty()) {
                                                        database.child(issue.id)
                                                            .child("status")
                                                            .setValue("In Progress")
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFACC15)),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("In Progress", color = Color.Black)
                                            }

                                            Button(
                                                onClick = {
                                                    if (issue.id.isNotEmpty()) {
                                                        database.child(issue.id)
                                                            .child("status")
                                                            .setValue("Resolved")
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Resolved", color = Color.White)
                                            }
                                        }

                                        "In Progress" -> {
                                            Button(
                                                onClick = {
                                                    if (issue.id.isNotEmpty()) {
                                                        database.child(issue.id)
                                                            .child("status")
                                                            .setValue("Resolved")
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text("Resolved", color = Color.White)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Logout confirmation dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Logout", color = Color.White) },
                text = { Text("Are you sure you want to logout?", color = Color.White) },
                confirmButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        currentUser?.delete()?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Account deleted", Toast.LENGTH_SHORT).show()
                                onLogout()
                            } else {
                                Toast.makeText(context, "Failed to delete account", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Text("Yes", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("No", color = Color(0xFFFFA726))
                    }
                },
                containerColor = Color(0xFF2C3E70), // dark blue background
                tonalElevation = 8.dp
            )
        }
    }
}