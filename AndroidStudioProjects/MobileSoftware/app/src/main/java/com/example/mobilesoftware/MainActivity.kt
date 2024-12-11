package com.example.mobilesoftware

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobilesoftware.data.Meal
import com.example.mobilesoftware.data.MealManager
import com.example.mobilesoftware.screens.Analysis1Screen
//import com.example.mobilesoftware.screens.Analysis1Screen
import com.example.mobilesoftware.screens.Analysis2Screen
import com.example.mobilesoftware.screens.DescriptionScreen
import com.example.mobilesoftware.screens.LoadingScreen
import com.example.mobilesoftware.screens.MealDetailPage
import com.example.mobilesoftware.screens.MealInfo
import com.example.mobilesoftware.screens.MealListScreen
import com.example.mobilesoftware.screens.RestaurantButtons
import com.example.mobilesoftware.ui.theme.MobileSoftwareTheme
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileSoftwareTheme {
                val mealManager = remember { MealManager() }

                val navController = rememberNavController() // 네비게이션 컨트롤러 생성
                NavHost(
                    navController = navController,
                    startDestination = "loading"
                ) {
                    // 로딩 화면
                    composable("loading") {
                        LoadingScreen(navController) // 로딩 화면 호출
                    }
                    // 설명 화면
                    composable("description") {
                        DescriptionScreen(navController) // 설명 화면 호출
                    }
                    composable("home") {
                        HomeScreen(mealManager, navController) // HomeScreen 호출
                    }
                    composable("restaurantButtons/{date}/{mealType}") { backStackEntry ->
                        val date = backStackEntry.arguments?.getString("date") ?: ""
                        val mealType = backStackEntry.arguments?.getString("mealType") ?: ""
                        RestaurantButtons(
                            date = SimpleDateFormat("yyyy-MM-dd").parse(date) ?: Date(),
                            mealType = mealType,
                            navController = navController
                        )
                    }
                    composable(
                        route = "mealInfo/{date}/{mealType}/{restaurant}"
                    ) { backStackEntry ->
                        // backStackEntry에서 전달된 인자 값들을 받아옴
                        val dateString = backStackEntry.arguments?.getString("date") ?: "" // date 값 가져오기, 없으면 빈 문자열
                        val mealType = backStackEntry.arguments?.getString("mealType") ?: "" // mealType 값 가져오기, 없으면 빈 문자열
                        val restaurant = backStackEntry.arguments?.getString("restaurant") ?: "" // restaurant 값 가져오기, 없으면 빈 문자열

                        // MealInfo 컴포저블을 호출
                        MealInfo(
                            date = SimpleDateFormat("yyyy-MM-dd").parse(dateString) ?: Date(),
                            mealType = mealType,
                            restaurant = restaurant,
                            mealManager = mealManager, // MealManager 전달
                            navController = navController // NavController 전달
                        )
                    }
                    composable("MealListScreen/{date}") { backStackEntry ->
                        val dateString = backStackEntry.arguments?.getString("date") ?: ""
                        val date = SimpleDateFormat("yyyy-MM-dd").parse(dateString) ?: Date()

                        MealListScreen(
                            date = date,
                            mealManager = mealManager,
                            navController = navController
                        )
                    }
                    composable(
                        route = "MealDetailPage/{mealType}/{restaurant}/{foodImageUri}/{foodName}/{foodReview}/{foodCost}/{foodCalorie}"
                    ) { backStackEntry ->
                        val mealType = backStackEntry.arguments?.getString("mealType") ?: ""
                        val restaurant = backStackEntry.arguments?.getString("restaurant") ?: ""
                        val foodImageUri = backStackEntry.arguments?.getString("foodImageUri") ?: ""
                        val foodName = backStackEntry.arguments?.getString("foodName") ?: ""
                        val foodReview = backStackEntry.arguments?.getString("foodReview") ?: ""
                        val foodCost = backStackEntry.arguments?.getString("foodCost")?.toIntOrNull() ?: 0
                        val foodCalorie = backStackEntry.arguments?.getString("foodCalorie")?.toIntOrNull() ?: 0

                        MealDetailPage(
                            mealType = mealType,
                            restaurant = restaurant,
                            foodImageUri = foodImageUri,
                            foodName = foodName,
                            foodReview = foodReview,
                            foodCost = foodCost,
                            foodCalorie = foodCalorie
                        )
                    }
                    composable("analysis1Page") {
                        Analysis1Screen(mealManager) // 분석하기 1 페이지
                    }
                    composable("analysis2Page") {
                        Analysis2Screen(mealManager) // 분석하기 2 페이지
                    }
                }
            }
        }
    }
}