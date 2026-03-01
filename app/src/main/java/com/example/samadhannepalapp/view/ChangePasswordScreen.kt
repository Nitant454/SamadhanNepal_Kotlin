package com.example.samadhannepalapp.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ChangePasswordScreen(
    onCancelClick: () -> Unit,
    onUpdateClick: (String, String) -> Unit
) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var currentVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B3D))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            "Change Password", fontSize = 22.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(30.dp))

        PasswordField(
            label = "Current Password",
            password = currentPassword,
            onPasswordChange = { currentPassword = it },
            visible = currentVisible,
            onToggleVisibility = { currentVisible = !currentVisible }
        )

        Spacer(modifier = Modifier.height(20.dp))

        PasswordField(
            label = "New Password",
            password = newPassword,
            onPasswordChange = { newPassword = it },
            visible = newVisible,
            onToggleVisibility = { newVisible = !newVisible }
        )

        Spacer(modifier = Modifier.height(20.dp))

        PasswordField(
            label = "Confirm New Password",
            password = confirmPassword,
            onPasswordChange = { confirmPassword = it },
            visible = confirmVisible,
            onToggleVisibility = { confirmVisible = !confirmVisible }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    if (newPassword != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    if (user != null && user.email != null) {

                        val credential = EmailAuthProvider
                            .getCredential(user.email!!, currentPassword)

                        user.reauthenticate(credential)
                            .addOnSuccessListener {

                                user.updatePassword(newPassword)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Password updated successfully",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        onUpdateClick(currentPassword, newPassword)
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            it.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Current password incorrect",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }

                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726))
            ) {
                Text("Update Password", color = Color.White)
            }
        }
    }
}

@Composable
fun PasswordField(
    label: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisibility: () -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(label, color = Color.White) },
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Toggle Password",
                    tint = Color.White
                )
            }
        },
    )
}