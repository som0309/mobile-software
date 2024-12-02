package com.example.proj_mainhome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

@Composable
fun HomeScreen(navController: NavController = rememberNavController()) {
    val today = LocalDate.now()
    var startDate by remember { mutableStateOf(today.minusDays((today.dayOfWeek.value - 1).toLong())) }
    var selectedDate by remember { mutableStateOf(today) }
    var accumulatedDrag by remember { mutableStateOf(0f) } // 드래그 거리 상태

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9F4)) // 전체 배경 색
    ) {
        //로고 부분
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.proj_logo), // 로고 파일
                contentDescription = "앱 로고",
                modifier = Modifier.size(180.dp)
            )
        }

        // Spacer(modifier = Modifier.height(2.dp)) // 로고 아래 여백 추가

        //날짜 부분
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { _, dragAmount ->
                            accumulatedDrag += dragAmount.x // 드래그 거리 누적
                        },
                        onDragEnd = {
                            val dragThreshold = 50f // 드래그 거리 임계값
                            if (accumulatedDrag > dragThreshold) {
                                startDate = startDate.minusWeeks(1) // 왼쪽 스와이프
                            } else if (accumulatedDrag < -dragThreshold) {
                                startDate = startDate.plusWeeks(1) // 오른쪽 스와이프
                            }
                            accumulatedDrag = 0f // 드래그 거리 초기화
                        }
                    )
                },
            horizontalArrangement = Arrangement.Center // 날짜 가운데 정렬
        ) {
            for (i in 0 until 7) {
                val date = startDate.plusDays(i.toLong())
                val daysOfWeek = listOf("월", "화", "수", "목", "금", "토", "일")
                val dayOfWeek = daysOfWeek[date.dayOfWeek.value - 1] // 한국어 요일 리스트에서 가져옴
                val dayOfMonth = date.dayOfMonth

                Column(
                    modifier = Modifier
                        .padding(horizontal = 6.dp) // 날짜 간 간격 조정
                        .width(40.dp) // 날짜의 너비 고정
                        .clickable { selectedDate = date }
                        .background(
                            if (selectedDate == date) Color(0xFFD1C1B1) else Color.Transparent,
                            shape = CircleShape
                        )
                        .padding(9.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dayOfWeek,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dayOfMonth.toString(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically, // 세로 중앙 정렬
            modifier = Modifier.padding(16.dp) // 전체 패딩 설정
        ) {
            // "오늘은" 텍스트
            Text(
                text = "Today is",
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp)) // 텍스트 간 간격

            // 날짜 부분
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFD1C1B1), // 배경색
                        shape = RoundedCornerShape(8.dp) // 둥근 모서리
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp) // 내부 여백
            ) {
                Text(
                    text = "$selectedDate", // 날짜 표시
                    fontSize = 20.sp,
                    color = Color.White, // 텍스트 색상
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFD1C1B1),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .height(600.dp) // 위젯 영역 높이 조정
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 첫 번째 위젯: 식사 입력하기
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 8.dp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // 식사 입력 페이지로 이동 (빈 화면)
                            navController.navigate("mealInputPage/전체")
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "식사 입력하기", fontSize = 10.sp)

                        // 아침/점심/저녁 버튼
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 3.dp), // 입력하기 위젯 양 옆의 여백 설정
                            horizontalArrangement = Arrangement.spacedBy(4.dp) // 버튼 간의 간격 줄이기
                        ) {
                            ButtonItem(imageResId = R.drawable.apple_button, navController, "조식")
                            ButtonItem(imageResId = R.drawable.salad_button, navController, "중식")
                            ButtonItem(imageResId = R.drawable.chicken_button, navController, "석식")
                            ButtonItem(imageResId = R.drawable.coffee_button, navController, "간식/음료")
                        }
                    }
                }


                // 두 번째 위젯: 식사 보여주기
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 8.dp,
                    color = Color.White, // 배경색 하얀색
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable {
                            // 식사 보여주기 페이지로 이동
                            navController.navigate("mealViewPage")
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 25.dp, vertical = 8.dp), // 내부 여백 설정
                        verticalAlignment = Alignment.CenterVertically // 수직 가운데 정렬
                    ) {
                        // 왼쪽: 이미지
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.calories), // calories.png 이미지 사용
                            contentDescription = "칼로리 이미지",
                            modifier = Modifier
                                .size(100.dp) // 이미지 크기 조정
                                .padding(end = 25.dp) // 텍스트와 간격 설정
                        )

                        // 오른쪽: 텍스트
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "식사 보여주기", fontSize = 10.sp)
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(

                                text = "오늘 섭취한 총 칼로리",
                                fontSize = 16.sp,
                                color = Color.Black // 텍스트 색상
                            )
                            Spacer(modifier = Modifier.height(4.dp)) // 줄 간격
                            Text(
                                text = "2,500kcal", // 임의의 칼로리값
                                fontSize = 18.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp)) // 줄 간격
                            Text(
                                text = "클릭하여 오늘의 식단 보기",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }

                // 세 번째와 네 번째 위젯: 분석하기1, 분석하기2
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),

                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 8.dp,
                        color = Color.White, // 배경색 하얀색
                        modifier = Modifier
                            .weight(1f)
                            .height(160.dp)
                            .clickable {
                                // 분석하기 1 페이지로 이동
                                navController.navigate("analysis1Page")
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly // 상하 균등 간격
                        ) {
                            Text(
                                text = "식사 분석: 한달 칼로리 계산",
                                fontSize = 10.sp,
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            androidx.compose.foundation.Image(
                                painter = painterResource(id = R.drawable.calculator), // calculator.png 이미지
                                contentDescription = "분석하기1 이미지",
                                modifier = Modifier.size(80.dp) // 이미지 크기 조정
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "자세히 보기",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }

                    //분석하기2
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 8.dp,
                        color = Color.White, // 배경색 하얀색
                        modifier = Modifier
                            .weight(1f)
                            .height(160.dp)
                            .clickable {
                                // 분석하기 2 페이지로 이동
                                navController.navigate("analysis2Page")
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly // 상하 균등 간격
                        ) {
                            Text(
                                text = "식사 분석: 한달 비용 계산",
                                fontSize = 10.sp,
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            androidx.compose.foundation.Image(
                                painter = painterResource(id = R.drawable.budget), // budget.png 이미지
                                contentDescription = "분석하기2 이미지",
                                modifier = Modifier.size(80.dp) // 이미지 크기 조정
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "자세히 보기",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
    }
}
// 아침/점심/저녁 버튼 컴포저블
@Composable
fun ButtonItem(imageResId: Int, navController: NavController, mealTime: String) {
    Surface(
        shape = RoundedCornerShape(5.dp),

        color = Color.White,
        modifier = Modifier
            .clickable {
                // 해당 식사 시간으로 이동
                navController.navigate("mealInputPage/$mealTime")
            }
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .size(72.dp), // 버튼 크기 조정
            contentAlignment = Alignment.Center
        ) {
            // 이미지 표시
            androidx.compose.foundation.Image(
                painter = painterResource(id = imageResId),
                contentDescription = mealTime // 접근성 설명
            )
        }
    }
}



