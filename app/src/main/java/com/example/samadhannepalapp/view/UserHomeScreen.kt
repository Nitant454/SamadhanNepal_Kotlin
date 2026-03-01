package com.example.samadhannepalapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samadhannepalapp.viewmodel.UserIssueViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.samadhannepalapp.R

@Composable
fun UserHomeScreenActivity(
    viewModel: UserIssueViewModel,
    onReportIssueClick: () -> Unit,
    onMyReportsClick: () -> Unit,
    onReportIssueClickWithCategory: (String) -> Unit
) {
    val context = LocalContext.current

    // Observe issues from ViewModel
    val issuesState by viewModel.issues.observeAsState(emptyList())
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Fetch user's issues
    LaunchedEffect(Unit) {
        viewModel.getIssuesByUserId(currentUserId)
    }

    // Calculate dynamic status counts
    val pendingCount = issuesState.count { it.status == "Pending" }
    val inProgressCount = issuesState.count { it.status == "In Progress" }
    val resolvedCount = issuesState.count { it.status == "Resolved" }

    Box(modifier = Modifier.fillMaxSize()) {

        // Background Image
        Image(
            painter = painterResource(R.drawable.registerpage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(10.dp))

                // Top Logo + Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.partners),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SamadhanNepal",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Top Cards Row
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {

                    // Report Issue Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(160.dp)
                            .background(Color(0xFFFFA726), RoundedCornerShape(12.dp))
                            .clickable { onReportIssueClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(R.drawable.plus),
                                contentDescription = null,
                                modifier = Modifier.size(70.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "Report a\ncommunity issue",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // My Reports Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(160.dp)
                            .background(Color(0xFF3F51B5), RoundedCornerShape(12.dp))
                            .clickable { onMyReportsClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(R.drawable.reports),
                                contentDescription = null,
                                modifier = Modifier.size(70.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "My Reports",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
                Divider(color = Color.White.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(20.dp))

                // Issue Categories
                Text(
                    text = "Issue Categories",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Categories Row (Static 4 categories)
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        CategoryItem("Road", R.drawable.road) { category ->
                            onReportIssueClickWithCategory(category)
                        }

                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        CategoryItem("Water", R.drawable.water) { category ->
                            onReportIssueClickWithCategory(category)
                        }
                    }
                }


                Spacer(modifier = Modifier.height(30.dp))
                Divider(color = Color.White.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Status Counters
            item {
                Text(
                    text = "Authority Issue Resolution",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatusBox("Resolved: $resolvedCount", Color(0xFF4CAF50))
                    StatusBox("In Progress: $inProgressCount", Color(0xFFFFA726))
                    StatusBox("Pending: $pendingCount", Color(0xFFF44336))
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun CategoryItem(title: String, icon: Int, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick(title) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(icon), contentDescription = null, modifier = Modifier.size(55.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = title, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun StatusBox(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(6.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
