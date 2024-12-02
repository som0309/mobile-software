package com.example.proj_mainhome.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf


@Composable
fun Analysis1Screen() {
    // 임시 데이터: 연동 시 수정할 부분
    val caloriesPerDay = List(31) { (700..2500).random() } // 현재 월의 칼로리 데이터
    val totalCalories = caloriesPerDay.sum()
    val averageCalories = caloriesPerDay.average().toInt()
    val maxCalories = caloriesPerDay.maxOrNull() ?: 0
    val minCalories = caloriesPerDay.minOrNull() ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD1C1B1))
            .padding(16. dp)
            .verticalScroll(rememberScrollState()) // 스크롤 가능 설정
    ) {
        // 섹션 1: 달력
        Text(
            text = "칼로리 분석",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        CalendarSection(caloriesPerDay)

        Spacer(modifier = Modifier.height(16.dp))

        // 섹션 2: 그래프
        GraphSection(caloriesPerDay)

        Spacer(modifier = Modifier.height(16.dp))

        // 섹션 3: 분석
        AnalysisSection(averageCalories, maxCalories, minCalories)
    }
}

@Composable
fun CalendarSection(caloriesPerDay: List<Int>) {
    // 현재 월 상태를 관리하는 일반 변수
    val currentMonth = remember { mutableStateOf(YearMonth.now()) } // 상태를 관찰 가능하도록 변경
    val totalCalories = caloriesPerDay.sum()

    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 월 이름 및 화살표
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "<", // 이전 달 화살표
                    fontSize = 24.sp,
                    modifier = Modifier.clickable {
                        // 이전 달로 상태 변경
                        currentMonth.value = currentMonth.value.minusMonths(1)
                    }
                )
                Text(
                    text = "${currentMonth.value.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.value.year}",
                    fontSize = 24.sp,
                    color = Color.Black
                )
                Text(
                    text = ">", // 다음 달 화살표
                    fontSize = 24.sp,
                    modifier = Modifier.clickable {
                        // 다음 달로 상태 변경
                        currentMonth.value = currentMonth.value.plusMonths(1)
                    }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            // 총 칼로리 표시
            Text(
                text = "총 칼로리: $totalCalories kcal",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 26.dp)
            )

            // 달력 표시
            DynamicCalendar(
                currentMonth = currentMonth.value,
                caloriesPerDay = caloriesPerDay
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "(단위: Kcal)",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.End)
            )
        }
    }
}


@Composable
fun DynamicCalendar(currentMonth: YearMonth, caloriesPerDay: List<Int>) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val totalDaysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value % 7) + 1 // 일요일 기준 요일 계산

    // 색상 맵
    val calorieColors = mapOf(
        700..999 to Color(0xFFFFF9F4),
        1000..1499 to Color(0xFFFFEAD5),
        1500..1999 to Color(0xFFFFD7AE),
        2000..2500 to Color(0xFFFFB974)
    )

    fun getColorForCalorie(calorie: Int): Color {
        return calorieColors.entries.firstOrNull { calorie in it.key }?.value ?: Color.Gray
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // 요일 헤더 표시
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                Text(text = it, fontSize = 14.sp, textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // 날짜와 칼로리 데이터 생성
        val daysWithCalories = MutableList(firstDayOfWeek - 1) { null } + // 앞쪽 빈칸
                (1..totalDaysInMonth).map { day ->
                    day to caloriesPerDay.getOrNull(day - 1) // (날짜, 칼로리)
                }

        daysWithCalories.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                week.forEach { day ->
                    if (day != null) {
                        val calorie = day.second ?: 0
                        val backgroundColor = getColorForCalorie(calorie)

                        Box(
                            modifier = Modifier
                                .size(40.dp) // 날짜 셀 크기
                                .background(color = backgroundColor, shape = CircleShape), // 원형 배경
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // 날짜 표시
                                Text(
                                    text = "${day.first}", // 날짜
                                    fontSize = 12.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                                // 칼로리 표시
                                Text(
                                    text = "${calorie}", // 칼로리 숫자만
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        // 빈 셀
                        Box(modifier = Modifier.size(48.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}





@Composable
fun GraphSection(caloriesPerDay: List<Int>) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Month Calories",
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            // 간단한 막대 그래프
            BarGraph(caloriesPerDay)
        }
    }
}

@Composable
fun BarGraph(caloriesPerDay: List<Int>) {
    val maxCalories = caloriesPerDay.maxOrNull() ?: 1
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val barWidth = size.width / (caloriesPerDay.size * 2)
        caloriesPerDay.forEachIndexed { index, calories ->
            val barHeight = (calories.toFloat() / maxCalories) * size.height
            drawRect(
                color = Color(0xFFA87B18),
                topLeft = androidx.compose.ui.geometry.Offset(
                    x = index * 2 * barWidth,
                    y = size.height - barHeight
                ),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )
        }
    }
}

@Composable
fun AnalysisSection(average: Int, max: Int, min: Int) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp) // 박스 전체 여백
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), // 상하 여백 추가
            horizontalArrangement = Arrangement.SpaceEvenly, // 가로 간격 균등 배치
            verticalAlignment = Alignment.CenterVertically // 세로 중앙 정렬
        ) {
            // 평균 섹션
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "평균",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp)) // 제목과 값 간 간격
                Text(
                    text = "$average kcal",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }

            // 최고 섹션
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "최고",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp)) // 제목과 값 간 간격
                Text(
                    text = "$max kcal",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }

            // 최저 섹션
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "최저",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp)) // 제목과 값 간 간격
                Text(
                    text = "$min kcal",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        }
    }
}