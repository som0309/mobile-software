package com.example.proj_mainhome.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proj_mainhome.R

@Composable
fun DescriptionScreen(navController: NavController) {
    // 앱 설명 페이지 리스트 (이미지 리소스들)
    val pages = listOf(
        R.drawable.ex1, // ex1.jpg
        R.drawable.ex2, // ex2.jpg
        R.drawable.ex3  // ex3.jpg
    )

    // 현재 보고 있는 페이지 상태
    var currentPage by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // 배경색 흰색
        contentAlignment = Alignment.Center
    ) {
        if (currentPage < pages.size) { // 아직 마지막 페이지가 아니라면
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 설명 페이지 이미지
                Image(
                    painter = painterResource(id = pages[currentPage]),
                    contentDescription = "설명 페이지 ${currentPage + 1}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 버튼: 다음 or 시작하기
                Button(onClick = {
                    if (currentPage < pages.size - 1) {
                        currentPage++ // 다음 페이지로 이동
                    } else {
                        navController.navigate("home") // 홈 화면으로 이동
                    }
                }) {
                    Text(if (currentPage < pages.size - 1) "다음" else "시작하기")
                }
            }
        }
    }
}
