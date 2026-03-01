package com.example.samadhannepalapp.view

import com.example.samadhannepalapp.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samadhannepalapp.model.UserModel
import com.example.samadhannepalapp.viewmodel.UserViewModel

import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : ComponentActivity() {

    private val userViewModel = UserViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreenBody(userViewModel)
        }
    }
}

@Composable
fun RegisterScreenBody(userViewModel : UserViewModel) {

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity
    val listState = rememberLazyListState()

    var selectedRole by remember { mutableStateOf("user") }

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
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {

            item {

                Image(
                    painter = painterResource(R.drawable.signup),
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Sign Up",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "Create a new account",
                    style = TextStyle(
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("Full Name", color = Color.White) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFFFFA726),
                        unfocusedIndicatorColor = Color.DarkGray,
                        cursorColor = Color.White
                    )
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email", color = Color.White) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFFFFA726),
                        unfocusedIndicatorColor = Color.DarkGray,
                        cursorColor = Color.White
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password", color = Color.White) },
                    singleLine = true,
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
                                tint = Color.White
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFFFFA726),
                        unfocusedIndicatorColor = Color.DarkGray,
                        cursorColor = Color.White
                    )
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("Confirm Password", color = Color.White) },
                    singleLine = true,
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
                                tint = Color.White
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFFFFA726),
                        unfocusedIndicatorColor = Color.DarkGray,
                        cursorColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                //  Role selection
                Row {
                    RadioButton(
                        selected = selectedRole == "user",
                        onClick = { selectedRole = "user" }
                    )
                    Text("User", color = Color.White, modifier = Modifier.padding(end = 16.dp))
                    RadioButton(
                        selected = selectedRole == "authority",
                        onClick = { selectedRole = "authority" }
                    )
                    Text("Authority", color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (fullName.isEmpty() || email.isEmpty() ||
                            password.isEmpty() || confirmPassword.isEmpty()
                        ) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        } else if (password != confirmPassword) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        } else {
                            // Create Auth Account
                            userViewModel.register(email, password, selectedRole) { success, message ->
                                if (success) {
                                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                                    val user = UserModel(
                                        id = uid,
                                        firstName = fullName,
                                        email = email,
                                        role = selectedRole
                                    )
                                    // save to Realtime DB
                                    userViewModel.addUserToDatabase(uid, user) { dbSuccess, dbMessage ->
                                        if (dbSuccess) {
                                            Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                                            context.startActivity(Intent(context, LoginActivity::class.java))
                                            activity.finish()
                                        } else {
                                            Toast.makeText(context, dbMessage, Toast.LENGTH_SHORT).show()
                                        }
                                    }
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726))
                ) {
                    Text("Sign Up", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Already have an account? ", color = Color.White)
                    Text(
                        "Log In",
                        color = colorResource(R.color.darkyellow),
                        modifier = Modifier.clickable {
                            context.startActivity(Intent(context, LoginActivity::class.java))
                            activity.finish()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}


