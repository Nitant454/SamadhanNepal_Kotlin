package com.example.samadhannepalapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.compose.ui.text.input.TextFieldValue
import com.example.samadhannepalapp.model.UserIssueModel
import com.example.samadhannepalapp.viewmodel.UserIssueViewModel

@Composable
fun MyReportedIssueScreen(
    viewModel: UserIssueViewModel,
    onBackClick: () -> Unit,
    onReportClick: (UserIssueModel) -> Unit
) {
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
    val issues by viewModel.issues.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) } //  Added search

    var deleteIssueId by remember { mutableStateOf<String?>(null) }

    val myIssues = issues.filter { it.userId == userId && it.category.contains(searchQuery.text, ignoreCase = true) }



    LaunchedEffect(userId) {
        viewModel.observeUserIssuesRealtime(userId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "My Reports",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by category") },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier.fillMaxWidth(),

        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            if (myIssues.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No reports found", color = Color.Gray)
                    }
                }
            } else {
                items(myIssues) { issue ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),

                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2F5A)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (!issue.imageUrl.isNullOrEmpty()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(issue.imageUrl),
                                        contentDescription = null,
                                        modifier = Modifier.size(70.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                                Column(
                                ) {
                                    Text(
                                        issue.category,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        "Ward: ${issue.ward}",
                                        color = Color.White
                                    )

                                    Text(
                                        issue.description,
                                        color = Color.LightGray,
                                    )
                                }
                            }
                        }


                        //  RIGHT SIDE (Status + Delete)
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {

                            val statusColor = when (issue.status) {
                                "Pending" -> Color(0xFFEF4444)
                                "In Progress" -> Color(0xFFFACC15)
                                "Resolved" -> Color(0xFF22C55E)
                                else -> Color.Gray
                            }

                            Box(
                                modifier = Modifier
                                    .background(statusColor, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(issue.status, color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            IconButton(
                                onClick = {
                                    deleteIssueId = issue.id   //  Open confirmation dialog
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Issue",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    // Delete Confirmation Dialog
    deleteIssueId?.let { issueId ->

        AlertDialog(
            onDismissRequest = { deleteIssueId = null },

            confirmButton = {
                TextButton(
                    onClick = {
                        FirebaseDatabase.getInstance()
                            .getReference("UserIssues")
                            .child(issueId)
                            .removeValue()   //  ACTUAL DELETE

                        deleteIssueId = null
                    }
                ) {
                    Text("Yes", color = Color.Red)
                }
            },

            dismissButton = {
                TextButton(
                    onClick = { deleteIssueId = null }
                ) {
                    Text("No")
                }
            },

            title = { Text("Delete Issue?") },

            text = {
                Text("Are you sure you want to permanently delete this issue?")
            }
        )
    }
}