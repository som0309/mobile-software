package com.example.mobilesoftware.data

import android.net.Uri
import java.util.Date

data class Meal(
    val id: Int,
    val date: Date,
    val mealType: String,
    val restaurant: String,
    val foodImage: Uri?,
    val foodName: String,
    val foodReview: String,
    val foodCost: Int,
    val foodCalorie: Int
) {
    companion object {
        private var idCounter = 0 // id를 관리할 변수

        // 음식 이름에 따른 칼로리 정보를 미리 정의한 맵
        private val foodCalories = mapOf(
            "Apple" to 95,
            "Banana" to 105,
            "Burger" to 354,
            "Pizza" to 285,
            "Salad" to 150
        )

        fun createMeal(
            date: Date,
            mealType: String,
            restaurant: String,
            foodImage: Uri?,
            foodName: String,
            foodReview: String,
            foodCost: Int
        ): Meal {
            idCounter++ // 새로운 Meal이 생성될 때마다 id를 증가

            // foodName에 맞는 칼로리 값을 찾고, 없으면 기본값 0을 설정
            val calculatedCalorie = foodCalories[foodName] ?: 0

            return Meal(
                id = idCounter, // 생성된 id 값 넣기
                date = date,
                mealType = mealType,
                restaurant = restaurant,
                foodImage = foodImage,
                foodName = foodName,
                foodReview = foodReview,
                foodCost = foodCost,
                foodCalorie = calculatedCalorie
            )
        }
    }
}