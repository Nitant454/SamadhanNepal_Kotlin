package com.example.samadhannepalapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samadhannepalapp.R


// Data model for categories

data class Category(
    val name: String,
    val iconRes: Int
)

// Sample categories list
val categories = listOf(
    Category("Road", R.drawable.road),
    Category("Garbage", R.drawable.garbage),
    Category("Water", R.drawable.water),
    Category("Electricity", R.drawable.electricity),
)


// Categories Screen
@Composable
fun UserCategoriesScreenActivity(
    onCategoryClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.registerpage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Foreground content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Categories",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color.White
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(category) { selectedCategory ->
                        onCategoryClick(selectedCategory) // Added: trigger dashboard callback
                    }
                }
            }
        }
    }
}


// Category Item
@Composable
fun CategoryItem(
    category: Category,
    onCategoryClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onCategoryClick(category.name) }, // Modified: call callback with category
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D47A1)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = category.iconRes),
                contentDescription = category.name,
                modifier = Modifier.size(55.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = category.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}
