package com.example.samadhannepalapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samadhannepalapp.R



class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserDashboardBody()
        }
    }
}

@Composable
fun UserDashboardBody(){

    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("Home", "Reports", "Categories", "Account")
    val icons = listOf(
        R.drawable.baseline_home_24,
        R.drawable.baseline_report_24,
        R.drawable.baseline_category_24,
        R.drawable.baseline_person_outline_24,
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = colorResource(R.color.lightblue),
                contentColor = Color.Gray
            ) {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                                Image(
                                    painter = painterResource(id = icons[index]),
                                    contentDescription = title,
                                    modifier = Modifier.size(24.dp)
                                )

                        },
                        label = { Text(title, fontSize = 10.sp) },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> UserHomeScreen()
                1 -> UserReportScreen()
                2 -> UserCategoryScreen()
                3 -> UserAccountScreen()
                else -> UserHomeScreen()
            }
        }
    }
}


@Preview
@Composable
fun UserDashboardPreview(){
    UserDashboardBody()
}