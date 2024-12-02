package com.example.mobilesoftware.data

import java.util.Date

class MealManager {
    var mealList = mutableListOf<Meal>()

    // 데이터 추가
    fun addMeal(meal: Meal) {
        mealList.add(meal)
    }

    // 모든 데이터 가져오기
    fun getAllMeals(): List<Meal> {
        return mealList
    }

    // 특정 데이터 삭제
    fun deleteMeal(meal: Meal) {
        mealList.remove(meal)
    }

    // 특정 날짜에 따른 데이터 검색
    fun findMealsByDate(date: Date): List<Meal> {
        return mealList.filter { it.date == date }
    }
}