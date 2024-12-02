package com.example.proj_mainhome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proj_mainhome.screens.MealInputScreen
import com.example.proj_mainhome.screens.MealViewScreen
import com.example.proj_mainhome.screens.Analysis1Screen
import com.example.proj_mainhome.screens.Analysis2Screen
import com.example.proj_mainhome.ui.theme.Proj_mainhomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Proj_mainhomeTheme {
                val navController = rememberNavController() // 네비게이션 컨트롤러 생성
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(navController) // HomeScreen 호출
                    }
                    composable("mealInputPage/{mealTime}") { backStackEntry ->
                        val mealTime = backStackEntry.arguments?.getString("mealTime") ?: "전체"
                        MealInputScreen(mealTime) // MealInputScreen 호출
                    }
                    composable("mealViewPage") {
                        MealViewScreen() // 식사 보여주기 페이지
                    }
                    composable("analysis1Page") {
                        Analysis1Screen() // 분석하기 1 페이지
                    }
                    composable("analysis2Page") {
                        Analysis2Screen() // 분석하기 2 페이지
                    }
                }
            }
        }
    }
}
