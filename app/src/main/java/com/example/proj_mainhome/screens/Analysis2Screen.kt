package com.example.proj_mainhome.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun Analysis2Screen() {
    // 임시 데이터
    val breakfastCosts = List(31) { (1000..5000).random() }
    val lunchCosts = List(31) { (7000..15000).random() }
    val dinnerCosts = List(31) { (10000..20000).random() }
    val snackCosts = List(31) { (500..2000).random() }
    // 날짜별 총 비용 계산
    val totalCostsPerDay = List(31) { day ->
        breakfastCosts[day] + lunchCosts[day] + dinnerCosts[day] + snackCosts[day]
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD1C1B1)) // 배경색
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // 스크롤 가능 설정
    ) {
        // 섹션 1: 달력
        Text(
            text = "비용 분석",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        CalendarSectionForCosts(totalCostsPerDay)

        Spacer(modifier = Modifier.height(16.dp))

        // 섹션 2: 그래프
        GraphSectionForCosts(totalCostsPerDay)

        Spacer(modifier = Modifier.height(16.dp))

        // 조식 박스
        MealAnalysisBox("조식", breakfastCosts)

        Spacer(modifier = Modifier.height(16.dp))

        // 중식 박스
        MealAnalysisBox("중식", lunchCosts)

        Spacer(modifier = Modifier.height(16.dp))

        // 석식 박스
        MealAnalysisBox("석식", dinnerCosts)

        Spacer(modifier = Modifier.height(16.dp))

        // 간식/음료 박스
        MealAnalysisBox("간식/음료", snackCosts)
    }
}

@Composable
fun CalendarSectionForCosts(costsPerDay: List<Int>) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val totalCosts = costsPerDay.sum()

    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "<",
                    fontSize = 24.sp,
                    modifier = Modifier.clickable { currentMonth = currentMonth.minusMonths(1) }
                )
                Text(
                    text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                    fontSize = 24.sp,
                    color = Color.Black
                )
                Text(
                    text = ">",
                    fontSize = 24.sp,
                    modifier = Modifier.clickable { currentMonth = currentMonth.plusMonths(1) }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            //총 비용 계산
            Text(
                text = "총 비용: $totalCosts 원",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 26.dp)
            )

            //달력 표시
            DynamicCalendarForCosts(currentMonth, costsPerDay)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "(단위: 원)",
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
fun DynamicCalendarForCosts(currentMonth: YearMonth, costsPerDay: List<Int>) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val totalDaysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value % 7) + 1

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                Text(text = it, fontSize = 14.sp, textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        val daysWithCosts = MutableList(firstDayOfWeek - 1) { null } +
                (1..totalDaysInMonth).map { day -> day to costsPerDay.getOrNull(day - 1) }

        daysWithCosts.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                week.forEach { day ->
                    if (day != null) {
                        val backgroundColor = when (day.second ?: 0) {
                            in 20000..24999 -> Color(0xFFFFF9F4)
                            in 25000..29999 -> Color(0xFFFFEAD5)
                            in 30000..34999 -> Color(0xFFFFD7AE)
                            else -> Color(0xFFFFB974)
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(backgroundColor, shape = RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${day.first}",
                                    fontSize = 12.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "${day.second ?: 0}",
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Box(modifier = Modifier.size(40.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun GraphSectionForCosts(costsPerDay: List<Int>) {
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
                text = "월별 비용 그래프",
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            BarGraphForCosts(costsPerDay)
        }
    }
}

@Composable
fun BarGraphForCosts(costsPerDay: List<Int>) {
    val maxCost = costsPerDay.maxOrNull() ?: 1
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val barWidth = size.width / (costsPerDay.size * 2)
        costsPerDay.forEachIndexed { index, cost ->
            val barHeight = (cost.toFloat() / maxCost) * size.height
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
fun MealAnalysisBox(mealType: String, costs: List<Int>) {
    val averageCost = costs.average().toInt()
    val maxCost = costs.maxOrNull() ?: 0
    val minCost = costs.minOrNull() ?: 0

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
                text = "$mealType 분석",
                fontSize = 13.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "평균", fontSize = 16.sp, color = Color.Black)
                    Text(text = "$averageCost 원", fontSize = 18.sp, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "최고", fontSize = 16.sp, color = Color.Black)
                    Text(text = "$maxCost 원", fontSize = 18.sp, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "최저", fontSize = 16.sp, color = Color.Black)
                    Text(text = "$minCost 원", fontSize = 18.sp, color = Color.Gray)
                }
            }
        }
    }
}
