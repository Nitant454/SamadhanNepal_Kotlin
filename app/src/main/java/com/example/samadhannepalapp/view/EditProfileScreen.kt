package com.example.samadhannepalapp.view

import androidx.compose.runtime.setValue

import com.example.samadhannepalapp.R
import android.widget.Toast
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import androidx.compose.ui.res.painterResource


@Composable
fun EditProfileScreen(
    onBackClick: (FirebaseUser) -> Unit
) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    var name by remember { mutableStateOf(TextFieldValue(currentUser?.displayName ?: "")) }
    var email by remember { mutableStateOf(TextFieldValue(currentUser?.email ?: "")) }
    var photoUri by remember { mutableStateOf<Uri?>(currentUser?.photoUrl) }

    // Launcher to pick image from gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            photoUri = uri
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(R.drawable.registerpage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Edit Profile",
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Profile photo with camera overlay
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(photoUri),
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
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.baseline_photo_camera_24),
                    contentDescription = "Change Photo",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xAA000000), CircleShape)
                        .padding(4.dp)
                        .clickable { imagePickerLauncher.launch("image/*") }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Name field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email field (read-only)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                textStyle = TextStyle(color = Color.White),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Save button
            Button(
                onClick = {

                    currentUser?.let{user ->
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name.text)
                            .setPhotoUri(photoUri)
                            .build()

                        user.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->

                                if (task.isSuccessful) {
                                    user.reload().addOnCompleteListener {
                                        Toast.makeText(
                                            context,
                                            "Profile Updated",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        onBackClick(user)
                                    }
                                } else {
                                    Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726))
            ) {
                Text("Save Changes", color = Color(0xFF1E2F5A))
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Cancel button
            OutlinedButton(
                onClick = { currentUser?.let { onBackClick(it) } }, // 🔹 Cancel just passes current user
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1976D2))
            ) {
                Text("Cancel")
            }
        }
    }
}