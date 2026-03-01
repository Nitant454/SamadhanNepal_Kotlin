package com.example.samadhannepalapp.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.example.samadhannepalapp.R

data class AccountOption(
    val name: String,
    val iconRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAccountScreenActivity(
    onEditProfileClick: () -> Unit,
    onLogoutConfirmed: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onMyReportsClick: () -> Unit // Added navigation for change password
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()

    var user by remember { mutableStateOf(auth.currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener {
            user = it.currentUser
        }

        auth.addAuthStateListener(listener)

        onDispose {
            auth.removeAuthStateListener(listener)
        }
    }

    val email = user?.email ?: "No Email"
    val displayName = user?.displayName ?: "Guest User"


    val context = LocalContext.current


    val accountOptions = listOf(
        AccountOption("My Reports", R.drawable.reports),
        AccountOption("Edit Profile", R.drawable.editprofile),
        AccountOption("Change Password", R.drawable.changepassword),
        AccountOption("Log Out", R.drawable.logout)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image (same as UserDashboardActivity)
        Image(
            painter = painterResource(R.drawable.registerpage),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.partners), // your logo drawable
                                contentDescription = "Logo",
                                tint = Color.Unspecified, // keep original logo colors
                                modifier = Modifier.size(30.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Samadhan Nepal", fontSize = 18.sp)
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Section
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            if (user?.photoUrl != null) {
                                //  This forces Coil to reload new image every time
                                val imageUrl = user?.photoUrl.toString() +
                                        "?t=" + System.currentTimeMillis()
                                Image(
                                    painter = rememberAsyncImagePainter(imageUrl),
                                    contentDescription = "Profile Photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_person_outline_24),
                                    contentDescription = "Default Avatar",
                                    tint = Color.DarkGray,
                                    modifier = Modifier.size(64.dp)
                                )
                            }
                        }


                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = displayName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = email,
                            fontSize = 14.sp,
                            color = Color.LightGray
                        )
                    }
                }

                // Account Options
                items(accountOptions.size) { index ->
                    val option = accountOptions[index]
                    AccountOptionCard(
                        option = option,
                        onClick = {
                            when (option.name) {
                                "My Reports" -> onMyReportsClick()
                                "Edit Profile" -> onEditProfileClick()
                                "Change Password" -> onChangePasswordClick()
                                "Log Out" -> showLogoutDialog = true
                            }
                        }
                    )
                }
            }

            // Logout Confirmation Dialog
            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Logout") },
                    text = { Text("Are you sure you want to logout?", color = Color.White) },
                    confirmButton = {
                        TextButton(onClick = {
                            showLogoutDialog = false
//                            onLogoutConfirmed()
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            currentUser?.delete()?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Account deleted successfully
                                    Toast.makeText(context, "Account deleted", Toast.LENGTH_SHORT).show()
                                    onLogoutConfirmed() // navigate to registration screen
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
                    containerColor = Color(0xFF2C3E70), // custom dark blue background for dialog
                    tonalElevation = 8.dp
                )
            }
        }
    }
}

@Composable
fun AccountOptionCard(
    option: AccountOption,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor =  Color(0xFF2C3E70) // lighter dark blue card over background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = option.iconRes),
                    contentDescription = option.name,
                    tint = Color(0xFFFFA726),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = option.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Arrow aligned at the end
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
                contentDescription = "Go",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
