package com.example.mobilesoftware.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mobilesoftware.R
import com.example.mobilesoftware.data.Meal
import com.example.mobilesoftware.data.MealManager
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun RestaurantButtons(
    date: Date,
    mealType: String,
    navController: NavController
) {
    val restaurants = listOf(
        Pair("상록원 1층", R.drawable.restaurant),
        Pair("상록원 2층", R.drawable.restaurant),
        Pair("상록원 3층", R.drawable.restaurant),
        Pair("남산학사", R.drawable.restaurant),
        Pair("고양학사", R.drawable.restaurant),
        Pair("카페", R.drawable.restaurant)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 48.dp, bottom = 48.dp)
    ) {
        // 3행 2열로 식당 버튼 배치
        for (row in restaurants.chunked(2)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                row.forEach {
                        restaurant ->
                    RestaurantButton(
                        date,
                        mealType,
                        restaurant = restaurant.first,
                        imageRes = restaurant.second,
                        navController = navController
                    )
                }
            }
        }
    }
}


@Composable
fun RestaurantButton(
    date: Date,
    mealType: String,
    restaurant: String,
    imageRes: Int,
    navController: NavController
)
{
    Box(
        modifier = Modifier
            .height(270.dp)
            .width(180.dp)
            .padding(8.dp) // 버튼 주변 여백
            .clip(RoundedCornerShape(16.dp)) // 모서리 둥글게
            .background(Color.LightGray) // 배경색 설정
            .clickable {
                // date를 "yyyy-MM-dd" 형식으로 변환
                val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(date)
                navController.navigate("mealInfo/$formattedDate/$mealType/$restaurant")
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.Center)
        ) {
            // 이미지 표시
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = restaurant,
                modifier = Modifier.size(140.dp) // 이미지 크기 조정
            )
            // 텍스트 표시
            Text(
                text = restaurant,
                color = Color.Black, // 텍스트 색상
                style = MaterialTheme.typography.titleLarge // 텍스트 스타일
            )
        }
    }
}

@Composable
fun MealInfo(
    date: Date,
    mealType: String,
    restaurant: String,
    mealManager: MealManager,
    navController: NavController
) {
    var foodImage by remember { mutableStateOf<Uri?>(null) }
    var foodName by remember { mutableStateOf("") }
    var foodReview by remember { mutableStateOf("") }
    var foodCost by remember { mutableStateOf("") }

    // 이미지 선택을 위한 Intent
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
                uri: Uri? -> foodImage = uri // 선택된 이미지 URI 저장
        }
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        // 음식 사진 선택 버튼
        Button(
            onClick = {
                launcher.launch("image/*") // 이미지 선택을 위한 갤러리 호출
            }
        ) {
            Text(text = "음식 사진 선택")
        }

        // 선택된 이미지 표시
        foodImage?.let {
                uri -> Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected Food Image",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // 음식 이름 입력 필드
        OutlinedTextField(
            value = foodName,
            onValueChange = { foodName = it },
            label = { Text("음식 이름") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 음식 소감 입력 필드
        OutlinedTextField(
            value = foodReview,
            onValueChange = { foodReview = it },
            label = { Text("음식 소감") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 음식 비용 입력 필드
        OutlinedTextField(
            value = foodCost,
            onValueChange = { foodCost = it },
            label = { Text("음식 비용") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 저장 버튼
        Button(
            onClick = {
                val meal = Meal.createMeal(
                    date = date,
                    mealType = mealType,
                    restaurant = restaurant,
                    foodImage = foodImage,
                    foodName = foodName,
                    foodReview = foodReview,
                    foodCost = foodCost.toInt()
                )
                mealManager.addMeal(meal) // 리스트에 저장

                // 필드 초기화
                foodImage = null
                foodName = ""
                foodReview = ""
                foodCost = ""

                navController.navigate("home")
            }
        ) {
            Text(text = "저장")
        }
    }
}