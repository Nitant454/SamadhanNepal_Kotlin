package com.example.samadhannepalapp.view

import com.example.samadhannepalapp.R
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.samadhannepalapp.model.UserIssueModel
import com.example.samadhannepalapp.viewmodel.UserIssueViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportIssueScreen(
    viewModel: UserIssueViewModel,
    prefilledCategory: String = "",
    onBackClick: () -> Unit
) {

    val context = LocalContext.current
    val operationStatus by viewModel.operationStatus.observeAsState() //MVVM Live Data obeserver

    val wardOptions = listOf("Ward 1", "Ward 2", "Ward 3", "Ward 4", "Ward 5")
    val categoryOptions = listOf("Road", "Garbage", "Water", "Electricity")

    var wardExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }

    var ward by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(prefilledCategory) }//prefilled from param
    var description by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> photoUri = uri }

    LaunchedEffect(operationStatus) {
        operationStatus?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    // Box to hold background image and content
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(R.drawable.registerpage), // put your image in res/drawable
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
//                    .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Top Bar with Back Arrow & Title
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Report Issue",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            //photo upload
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x99000000))
                        .clickable { photoPickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (photoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(photoUri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.CameraAlt, null,
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFFFFA726)
                        )
                    }
                }
            }

            item {
                ExposedDropdownMenuBox(
                    expanded = wardExpanded,
                    onExpandedChange = { wardExpanded = !wardExpanded }
                ) {
                    OutlinedTextField(
                        value = ward,
                        onValueChange = {  },
                        label = { Text("Ward", color = Color.White) },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = wardExpanded) },
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        modifier = Modifier.fillMaxWidth()
                            .menuAnchor(),
                    )
                    ExposedDropdownMenu(
                        expanded = wardExpanded,
                        onDismissRequest = { wardExpanded = false }
                    ) {
                        wardOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    ward = option
                                    wardExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Category Dropdown
            item {
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category", color = Color.White) },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        textStyle = LocalTextStyle.current.copy(color = Color.White),
                        modifier = Modifier.fillMaxWidth()
                            .menuAnchor(),
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categoryOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    category = option
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description", color = Color.White) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Button(
                    onClick = {

                        if (photoUri == null || ward.isBlank() || category.isBlank() || description.isBlank()) {
                            Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT)
                                .show()
                            return@Button
                        }

                        isUploading = true

                        //  Upload to Cloudinary
                        CloudinaryManager.uploadImage(
                            imageUri = photoUri!!,
                            onSuccess = { imageUrl ->

                                val issueId = FirebaseDatabase.getInstance()
                                    .getReference("UserIssues")
                                    .push().key!!

                                val model = UserIssueModel(
                                    id = issueId,
                                    category = category,
                                    description = description,
                                    ward = ward,
                                    status = "Pending",
                                    userId = FirebaseAuth.getInstance().currentUser!!.uid,
                                    timestamp = System.currentTimeMillis(),
                                    imageUrl = imageUrl //  REAL CLOUDINARY URL
                                )

                                viewModel.addIssue(issueId, model)

                                isUploading = false
                                ward = ""
                                category = ""
                                description = ""
                                photoUri = null

                                //  Go back to home screen after submit
                                onBackClick()
                            },
                            onError = { error ->
                                isUploading = false
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // taller button
                    shape = RoundedCornerShape(12.dp), // slightly smaller radius
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)), // orange
                    enabled = !isUploading
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Submit Report")
                    }
                }
            }
        }
    }
}