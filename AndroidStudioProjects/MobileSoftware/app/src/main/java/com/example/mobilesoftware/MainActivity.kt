package com.example.mobilesoftware

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import com.example.mobilesoftware.data.Meal
import com.example.mobilesoftware.ui.theme.MobileSoftwareTheme
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileSoftwareTheme {
                val mealList = remember { mutableStateListOf<Meal>() }
                val navController = rememberNavController()
//
//                MealInfo(Date(), "아침", "상록원", mealList)

                mealList.add(Meal.createMeal(Date(), "아침", "상록원 1층", null, "불닭", "맛있다.", 6000))

                NavHost(navController = navController, startDestination = "MealList") {
                    composable("MealList") {
                        MealListScreen(date = Date(), meals = mealList, navController = navController)
                    }
                    composable(
                        route = "MealDetailPage/{mealJson}",
                        arguments = listOf(navArgument("mealJson") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val mealJson = backStackEntry.arguments?.getString("mealJson") ?: ""
                        val meal = Gson().fromJson(mealJson, Meal::class.java)
                        MealDetailPage(meal = meal)
                    }
                }
            }
        }
    }
}

@Composable
fun RestaurantButtons(
    navController: NavController,
    date: Date,
    mealType: String
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
                        name = restaurant.first,
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
    name: String,
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
                navController.navigate("mealInfo/$date/$mealType/$name")
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
                contentDescription = name,
                modifier = Modifier.size(140.dp) // 이미지 크기 조정
            )
            // 텍스트 표시
            Text(
                text = name,
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
    mealList: MutableList<Meal>,
//    navController: NavController
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
                mealList.add(meal) // 리스트에 저장

                // 필드 초기화
                foodImage = null
                foodName = ""
                foodReview = ""
                foodCost = ""
            }
        ) {
            Text(text = "저장")
        }
    }
}

//@Composable
//fun MealListScreen(date: Date, meals: List<Meal>, navController: NavController) {
//    // 현재 날짜 상태 관리
//    var selectedDate by remember { mutableStateOf(date) }
//    val filteredMeals = meals.filter { it.date.isSameDay(selectedDate) }
//    var selectedMeal by remember { mutableStateOf<Meal?>(null) }
//
//    if (selectedMeal != null) {
//        // 세부 정보 페이지로 네비게이션
//        navController.navigate("MealDetailPage/${selectedMeal?.id}")
//    } else {
//        // 메인 화면
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                IconButton(onClick = { selectedDate = selectedDate.previousDay() }) {
//                    Icon(Icons.Default.ArrowBack, contentDescription = "이전 날짜")
//                }
//                Text(
//                    text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate),
//                    style = MaterialTheme.typography.headlineMedium
//                )
//                IconButton(onClick = { selectedDate = selectedDate.nextDay() }) {
//                    Icon(Icons.Default.ArrowForward, contentDescription = "다음 날짜")
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            if (filteredMeals.isNotEmpty()) {
//                MealGrid(mealList = filteredMeals) {
//                    meal -> selectedMeal = meal
//                }
//            } else {
//                Text(
//                    text = "해당 날짜의 식단 정보가 없습니다.",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun MealGrid(mealList: List<Meal>, onMealClick: (Meal) -> Unit) {
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2),
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        items(mealList) {
//            meal ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(1f)
//                    .clip(RoundedCornerShape(12.dp))
//                    .clickable { onMealClick(meal) },
//                shape = RoundedCornerShape(16.dp),
//                elevation = CardDefaults.cardElevation(4.dp)
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center,
//                    modifier = Modifier.padding(8.dp)
//                ) {
//                    Image(
//                        painter = rememberAsyncImagePainter(meal.foodImage),
//                        contentDescription = "Meal Image",
//                        modifier = Modifier
//                            .size(100.dp)
//                            .clip(CircleShape)
//                            .border(2.dp, Color.Gray, CircleShape),
//                        contentScale = ContentScale.Crop
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = meal.foodName,
//                        style = MaterialTheme.typography.titleSmall,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun MealDetailPage(meal: Meal) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Image(
//            painter = rememberAsyncImagePainter(meal.foodImage),
//            contentDescription = "Meal Detail Image",
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//                .clip(RoundedCornerShape(12.dp)),
//            contentScale = ContentScale.Crop
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(
//            text = meal.foodName,
//            style = MaterialTheme.typography.headlineLarge,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Text(
//            text = "Review: ${meal.foodReview}",
//            style = MaterialTheme.typography.bodyLarge,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//        Text(
//            text = "Cost: \$${meal.foodCost}",
//            style = MaterialTheme.typography.bodyLarge
//        )
//    }
//}

@Composable
fun MealListScreen(date: Date, meals: List<Meal>, navController: NavController) {
    var selectedDate by remember { mutableStateOf(date) }
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
                val mealJson = Uri.encode(Gson().toJson(meal))
                navController.navigate("MealDetailPage/$mealJson")
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
fun MealDetailPage(meal: Meal) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 음식 사진
        Image(
            painter = rememberAsyncImagePainter(meal.foodImage),
            contentDescription = "Meal Detail Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 음식 이름
        Text(
            text = meal.foodName,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 식사 타입
        Text(
            text = "Meal Type: ${meal.mealType}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 식당 종류
        Text(
            text = "Restaurant: ${meal.restaurant}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 리뷰
        Text(
            text = "Review: ${meal.foodReview}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 가격
        Text(
            text = "Cost: \$${meal.foodCost}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 칼로리
        Text(
            text = "Calories: ${meal.foodCalorie} kcal",
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