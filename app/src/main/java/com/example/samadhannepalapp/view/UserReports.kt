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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.samadhannepalapp.model.UserIssueModel
import com.example.samadhannepalapp.viewmodel.UserIssueViewModel
import androidx.compose.foundation.lazy.items

@Composable
fun UserReportsScreen(
    viewModel: UserIssueViewModel,
    onReportClick: (UserIssueModel) -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.getAllIssues()
    }

    val reports by viewModel.issues.observeAsState(emptyList())

    var selectedTab by remember { mutableStateOf("All") }

    val filteredReports = when (selectedTab) {
        "Pending" -> reports.filter { it.status == "Pending" }
        "In Progress" -> reports.filter { it.status == "In Progress" }
        "Resolved" -> reports.filter { it.status == "Resolved" }
        else -> reports
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1B2A59), Color(0xFF0D1B3D))
                )
            )
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Screen title
            item {
                Text(
                    "Reports",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Filter tabs row
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("All", "Pending", "In Progress", "Resolved").forEach { tab ->
                        val count = when (tab) {
                            "All" -> reports.size
                            else -> reports.count { it.status == tab }
                        }
                        FilterTab("$tab${if (tab != "All") " $count" else ""}", selectedTab) {
                            selectedTab = tab
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Report cards
            if (filteredReports.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No reports found", color = Color.Gray)
                    }
                }
            } else {
                items(filteredReports) { report ->
                    ReportCard(report) { onReportClick(report) }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) } // for bottom nav spacing
        }
    }
}

@Composable
fun FilterTab(text: String, selectedTab: String, onClick: () -> Unit) {
    val isSelected = text.startsWith(selectedTab) || (selectedTab == "All" && text == "All")

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color(0xFFFFA500) else Color(0xFF2D3E75))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ReportCard(report: UserIssueModel, onClick: () -> Unit) {

    val statusColor = when (report.status) {
        "Pending" -> Color(0xFFD32F2F)
        "In Progress" -> Color(0xFFFFA726)
        "Resolved" -> Color(0xFF388E3C)
        else -> Color.Gray
    }

    val safeTimestamp = if (report.timestamp == 0L) System.currentTimeMillis() else report.timestamp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF142247)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Issue image
            if (!report.imageUrl.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(report.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(report.category, color = Color.White, fontWeight = FontWeight.Bold)
                Text(report.ward, color = Color.LightGray)
                Text(timeAgo(safeTimestamp), color = Color.Gray, fontSize = 12.sp)
            }

            Box(
                modifier = Modifier
                    .background(statusColor, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(report.status, color = Color.White)
            }
        }
    }
}

