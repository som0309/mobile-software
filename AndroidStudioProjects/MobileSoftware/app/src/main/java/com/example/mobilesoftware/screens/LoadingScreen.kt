package com.example.mobilesoftware.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilesoftware.R
import com.example.mobilesoftware.data.MealManager
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(mealManager: MealManager, navController: NavController) {
    mealManager.generateMealData()
    val meals = mealManager.getAllMeals()


//    // meals 리스트의 내용을 하나씩 출력
//    for (meal in meals) {
//        Log.d("MealManager", "Meal: ${meal.date}, ${meal.mealType}, ${meal.mealType}, ${meal.restaurant}, ${meal.foodImage}, ${meal.foodName}, ${meal.foodReview}, ${meal.foodCost}")
//    }
    // 로딩 화면 UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFB469)), // 단색 배경 (연한 오렌지색)
        contentAlignment = Alignment.Center // 중앙 정렬
    ) {
        // 로고 이미지 표시
        Image(
            painter = painterResource(id = R.drawable.proj_logo),
            contentDescription = "앱 로고",
            modifier = Modifier.size(200.dp) // 로고 크기 설정
        )
    }

    // 로딩 화면 후 다음 화면으로 이동
    LaunchedEffect(Unit) {
        delay(2000L) // 2초 동안 로딩 유지
        navController.navigate("description") { // 경로 수정
            popUpTo("loading") { inclusive = true } // 로딩 화면 제거
        }
    }
}