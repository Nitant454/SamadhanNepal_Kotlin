package com.example.samadhannepalapp.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samadhannepalapp.R
import com.example.samadhannepalapp.repository.UserRepositoryImpl
import com.example.samadhannepalapp.viewmodel.UserViewModel
import androidx.compose.runtime.livedata.observeAsState
import kotlin.let
import com.google.firebase.auth.FirebaseAuth



class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginScreenBody()
        }
    }
}

@Composable
fun LoginScreenBody() {
    var userViewModel = remember{ UserViewModel(UserRepositoryImpl()) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity

//    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
//
//    val localEmail: String? = sharedPreferences.getString("email", "")
//    val localPassword: String? = sharedPreferences.getString("password", "")

//    val loginResult by userViewModel.loginResult.observeAsState()
//    val userProfile by userViewModel.userProfile.observeAsState()

    // Handle login result
//    LaunchedEffect(loginResult) {
//        loginResult?.let { (success, message) ->
//            if (!success) {
//                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//            } else {
//                // Fetch user profile after successful login
//                // Use the `id` returned by loginResult
//                userViewModel.getUSerById(message) // message contains the user ID
//            }
//        }
//    }
//
//    // Handle fetched user profile
//    LaunchedEffect(userProfile) {
//        userProfile?.let { user ->
//            Toast.makeText(context, "Welcome ${user.firstName}", Toast.LENGTH_SHORT).show()
//            val intent = Intent(context, DashboardActivity::class.java)
//            context.startActivity(intent)
//            activity.finish()
//        }
//    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(R.drawable.splashscreen),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo above Sign In
            Image(
                painter = painterResource(R.drawable.login),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                "Log In",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                "Welcome back! Please login to continue.",
                style = TextStyle(
                    color = Color.Black.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            )

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = if (passwordVisible)
                                painterResource(R.drawable.baseline_visibility_off_24) // your drawable for "hide"
                            else
                                painterResource(R.drawable.baseline_visibility_24), // your drawable for "show"
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer( modifier = Modifier.height(5.dp))

            Text(
                "Forgot Password?",
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {
                        val intent = Intent(context, ForgetPasswordActivity::class.java)
                        context.startActivity(intent)
                        // No need to finish() here, user may want to come back to login
                    }
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                    } else {
                        userViewModel.login(email, password) { success, message ->
                            if (success) {
                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, DashboardActivity::class.java)
                                context.startActivity(intent)
                                activity.finish()
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightblue))
            ) {
                Text("Login", fontSize = 20.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign up row
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Don't have an account? ", color = Color.Black)
                Text(
                    "Sign Up",
                    color = Color(0xFF1565C0),
                    modifier = Modifier.clickable {
                        val intent = Intent(context, RegisterActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginScreenBody()
}
