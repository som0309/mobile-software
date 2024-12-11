package com.example.mobilesoftware.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mobilesoftware.data.Meal
import com.example.mobilesoftware.data.MealManager
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun MealListScreen(date: Date, mealManager: MealManager, navController: NavController) {
    var selectedDate by remember { mutableStateOf(date) }
    var meals = mealManager.getAllMeals()
    val filteredMeals = meals.filter { it.date.isSameDay(selectedDate) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { selectedDate = selectedDate.previousDay() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "이전 날짜")
            }
            Text(
                text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate),
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = { selectedDate = selectedDate.nextDay() }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "다음 날짜")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredMeals.isNotEmpty()) {
            MealGrid(mealList = filteredMeals) { meal ->
                val mealType = meal.mealType
                val restaurant = meal.restaurant
                val foodImageUri = URLEncoder.encode(meal.foodImage?.toString() ?: "", StandardCharsets.UTF_8.toString())  // URI 인코딩
                val foodName = meal.foodName
                val foodReview = meal.foodReview
                val foodCost = meal.foodCost
                val foodCalorie = meal.foodCalorie
                navController.navigate("MealDetailPage/$mealType/$restaurant/$foodImageUri/$foodName/$foodReview/$foodCost/$foodCalorie")
            }
        } else {
            Text(
                text = "해당 날짜의 식단 정보가 없습니다.",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun MealGrid(mealList: List<Meal>, onMealClick: (Meal) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(mealList) { meal ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onMealClick(meal) },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(meal.foodImage),
                        contentDescription = "Meal Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = meal.foodName,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun MealDetailPage(
    mealType: String,
    restaurant: String,
    foodImageUri: String,
    foodName: String,
    foodReview: String,
    foodCost: Int,
    foodCalorie: Int
) {
    val foodImageUri = Uri.parse(foodImageUri)  // 디코딩된 URI를 Uri 객체로 변환

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 음식 사진
        Image(
            painter = rememberAsyncImagePainter(foodImageUri),
            contentDescription = "Meal Detail Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 음식 이름
        if (foodName != null) {
            Text(
                text = foodName,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // 식사 타입
        Text(
            text = "Meal Type: ${mealType}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 식당 종류
        Text(
            text = "Restaurant: ${restaurant}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 리뷰
        Text(
            text = "Review: ${foodReview}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 가격
        Text(
            text = "Cost: ${foodCost}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 칼로리
        Text(
            text = "Calories: ${foodCalorie} kcal",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

// 날짜 비교 함수
fun Date.isSameDay(other: Date): Boolean {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(this) == formatter.format(other)
}

// 날짜 이동 함수
fun Date.previousDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DATE, -1)
    return calendar.time
}

fun Date.nextDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DATE, 1)
    return calendar.time
}