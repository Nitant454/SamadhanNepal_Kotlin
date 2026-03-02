package com.example.samadhannepalapp.view

import com.example.samadhannepalapp.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samadhannepalapp.repository.UserRepositoryImpl
import com.example.samadhannepalapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : ComponentActivity() {

    private val userViewModel = UserViewModel(UserRepositoryImpl())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginScreenBody(userViewModel)
        }
    }
}

@Composable
fun LoginScreenBody(userViewModel : UserViewModel) {

//    val userViewModel = remember { UserViewModel(UserRepositoryImpl()) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity
    val listState = rememberLazyListState()
//    val auth = FirebaseAuth.getInstance()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(R.drawable.registerpage),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .imePadding(),   // Important for keyboard
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {

            item {

                Image(
                    painter = painterResource(R.drawable.key),
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    "Log In",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "Welcome back! Please login to continue.",
                    style = TextStyle(
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email", color = Color.White) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .testTag("emailField"),
                    textStyle = TextStyle(color = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(

                        //  Text color
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,

                        // Background (dark field)
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,

                        // orange border when selected
                        focusedIndicatorColor = Color(0xFFFFA726),

                        // Dark border when not selected
                        unfocusedIndicatorColor = Color.DarkGray,

                        // Cursor color
                        cursorColor = Color.White
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password", color = Color.White) },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White),
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = if (passwordVisible)
                                    painterResource(R.drawable.baseline_visibility_off_24)
                                else
                                    painterResource(R.drawable.baseline_visibility_24),
                                contentDescription = null,
                                tint = Color.White // make icon white for dark background
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .testTag("passwordField"),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(

                        //  Text color
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,

                        // Background (dark field)
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,

                        // orange border when selected
                        focusedIndicatorColor = Color(0xFFFFA726),

                        // Dark border when not selected
                        unfocusedIndicatorColor = Color.DarkGray,

                        // Cursor color
                        cursorColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        "Forgot Password?",
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable {
                                val intent = Intent(context, ForgetPasswordActivity::class.java)
                                context.startActivity(intent)
                            }
                            .padding(vertical = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (email.isEmpty() || password.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Please enter email and password",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            userViewModel.login(email, password) { success, message ->
                                if (success) {
                                    val uid =
                                        FirebaseAuth.getInstance().currentUser?.uid
                                            ?: ""
                                    userViewModel.getUserRole(uid) { roleSuccess, role ->
                                        if (roleSuccess) {
                                            when (role) {
                                                "authority" -> activity.startActivity(Intent(activity, AuthorityDashboardActivity::class.java))
                                                else -> activity.startActivity(Intent(activity, UserDashboardActivity::class.java))
                                            }
                                            activity.finish()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Failed to get role",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("loginButton"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFA726)
                    )
                ) {
                    Text("Login", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Don't have an account? ", color = Color.White)
                    Text(
                        "Sign Up",
                        color = Color(0xFFFFA726),
                        modifier = Modifier.clickable {
                            val intent = Intent(context, RegisterActivity::class.java)
                            context.startActivity(intent)
                            activity.finish()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}


