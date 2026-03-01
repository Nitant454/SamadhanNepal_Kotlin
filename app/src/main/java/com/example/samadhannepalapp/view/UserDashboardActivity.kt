package com.example.samadhannepalapp.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.samadhannepalapp.viewmodel.UserIssueViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.samadhannepalapp.R
class UserDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CloudinaryManager.init(this)

        setContent {

            val context = LocalContext.current
            val userIssueViewModel: UserIssueViewModel = viewModel()

            var selectedTab by remember { mutableStateOf(0) }
            var showReportIssue by remember { mutableStateOf(false) }
            var showMyReports by remember { mutableStateOf(false) }
            var preselectedCategory by remember { mutableStateOf<String?>(null) }

            val tabs = listOf("Home", "Reports", "Categories", "Account")
            val icons = listOf(
                R.drawable.baseline_home_24,
                R.drawable.baseline_report_24,
                R.drawable.baseline_category_24,
                R.drawable.baseline_person_outline_24
            )

            var showEditProfile by remember { mutableStateOf(false) }
            var showChangePassword by remember { mutableStateOf(false) }

            Box(modifier = Modifier.fillMaxSize()) {

                Image(
                    painter = rememberAsyncImagePainter(R.drawable.registerpage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Scaffold(
                    containerColor = Color.Transparent,
                    bottomBar = {
                        NavigationBar(containerColor = Color(0xFF1E2F5A)) {
                            tabs.forEachIndexed { index, title ->
                                NavigationBarItem(
                                    selected = selectedTab == index,
                                    onClick = {
                                        selectedTab = index

                                        showEditProfile = false
                                        showChangePassword = false
                                        showReportIssue = false
                                        showMyReports = false
                                    },
                                    icon = {
                                        Icon(
                                            painter = rememberAsyncImagePainter(icons[index]),
                                            contentDescription = title,
                                            tint = if (selectedTab == index)
                                                Color(0xFFFFA726) else Color.Gray
                                        )
                                    },
                                    label = {
                                        Text(
                                            title,
                                            fontSize = 12.sp,
                                            color = if (selectedTab == index)
                                                Color(0xFFFFA726) else Color.Gray
                                        )
                                    }
                                )
                            }
                        }
                    }
                ) { paddingValues ->

                    //  MAIN CONTENT SWITCH
                    Box(modifier = Modifier.padding(paddingValues)) {

                        when {
                            // Show the Report Issue screen (when creating a new report)
                            showReportIssue -> {
                                ReportIssueScreen(
                                    viewModel = userIssueViewModel,
                                    prefilledCategory = preselectedCategory ?: "",
                                    onBackClick = {
                                        showReportIssue = false
                                        preselectedCategory = null
                                    }
                                )
                            }

                            // Show My Reports screen (when user clicks "My Reports" inside Home)
                            showMyReports -> {
                                MyReportedIssueScreen(
                                    viewModel = userIssueViewModel,
                                    onBackClick = { showMyReports = false },
                                    onReportClick = {} // Added empty lambda to satisfy required parameter
                                )
                            }

                            //  Edit Profile Screen
                            showEditProfile -> {
                                EditProfileScreen(
                                    onBackClick = { _ ->
                                        showEditProfile = false }
                                )
                            }

                            //  Change Password Screen
                            showChangePassword -> {
                                ChangePasswordScreen(
                                    onCancelClick = {
                                        showChangePassword = false
                                    },
                                    onUpdateClick = {_, _ ->
                                        showChangePassword = false
                                    }
                                )
                            }

                            // Default main tab content
                            else -> {
                                when (selectedTab) {
                                    0 -> UserHomeScreenActivity(
                                        viewModel = userIssueViewModel,
                                        onReportIssueClick = {
                                            preselectedCategory = null
                                            showReportIssue = true
                                        },
                                        onMyReportsClick = {
                                            showMyReports = true
                                        },
                                        onReportIssueClickWithCategory = { category ->
                                            preselectedCategory = category
                                            showReportIssue = true
                                        }
                                    )

                                    1 -> UserReportsScreen(
                                        viewModel = userIssueViewModel,
                                        onReportClick ={} // Added required lambda to fix error
                                    )

                                    2 -> UserCategoriesScreenActivity(
                                        onCategoryClick = { category ->
                                            preselectedCategory = category
                                            selectedTab = 0
                                            showReportIssue = true
                                        }
                                    )

                                    3 -> UserAccountScreenActivity(
                                        onEditProfileClick = {
                                            showEditProfile = true
                                        },
                                        onChangePasswordClick = {
                                            showChangePassword = true
                                        },
                                        onMyReportsClick = {
                                            selectedTab = 0
                                            showMyReports = true
                                        },
                                        onLogoutConfirmed = {
                                            FirebaseAuth.getInstance().signOut()
                                            Toast.makeText(
                                                context,
                                                "Logged out",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            context.startActivity(
                                                Intent(
                                                    context,
                                                    RegisterActivity::class.java
                                                )
                                            )
                                            finish()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
