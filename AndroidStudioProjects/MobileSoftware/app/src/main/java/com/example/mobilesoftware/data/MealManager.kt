package com.example.mobilesoftware.data

import android.health.connect.datatypes.MealType
import java.time.YearMonth
import java.util.Calendar
import java.util.Date

class MealManager {
    private var mealList = mutableListOf<Meal>()

    // 데이터 추가
    fun addMeal(meal: Meal) {
        mealList.add(meal)
    }

    // 모든 데이터 가져오기
    fun getAllMeals(): List<Meal> {
        return mealList
    }

    fun getTotalCaloriesByDate(selectedDate: Date): Int {
        val calendar = Calendar.getInstance()

        // 선택된 날짜의 연도, 월, 일 추출
        calendar.time = selectedDate
        val selectedYear = calendar.get(Calendar.YEAR)
        val selectedMonth = calendar.get(Calendar.MONTH)
        val selectedDay = calendar.get(Calendar.DAY_OF_MONTH)

        // 같은 날짜에 해당하는 Meal 객체 필터링
        val mealsOnDate = mealList.filter { meal ->
            calendar.time = meal.date
            val mealYear = calendar.get(Calendar.YEAR)
            val mealMonth = calendar.get(Calendar.MONTH)
            val mealDay = calendar.get(Calendar.DAY_OF_MONTH)

            mealYear == selectedYear && mealMonth == selectedMonth && mealDay == selectedDay
        }

        // 해당 날짜의 칼로리 합산
        return mealsOnDate.sumOf { it.foodCalorie }
    }

    fun getCaloriesByMonthAndType(yearMonth: YearMonth, mealType: String): List<Int> {
        // 해당 월의 첫날과 마지막 날 계산
        val calendar = Calendar.getInstance()
        calendar.set(yearMonth.year, yearMonth.monthValue - 1, 1) // 1일 설정
        val firstDayOfMonth = calendar.time

        calendar.set(
            yearMonth.year,
            yearMonth.monthValue - 1,
            yearMonth.lengthOfMonth()
        ) // 마지막 날 설정
        val lastDayOfMonth = calendar.time

        // mealType 조건에 따라 필터링 로직 결정
        val filteredMeals = mealList.filter { meal ->
            meal.date.after(firstDayOfMonth) && meal.date.before(lastDayOfMonth) || meal.date == firstDayOfMonth
        }.filter { meal ->
            mealType == "월별" || meal.mealType == mealType
        }

        // 해당 월의 일 수만큼 초기 리스트 생성 (모든 값을 0으로 초기화)
        val daysInMonth = yearMonth.lengthOfMonth()
        val caloriesPerDay = MutableList(daysInMonth) { 0 }

        // 데이터를 해당 날짜 인덱스에 매핑
        filteredMeals.forEach { meal ->
            calendar.time = meal.date
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            caloriesPerDay[dayOfMonth - 1] += meal.foodCalorie
        }

        return caloriesPerDay
    }

    fun getCostsByMonthAndType(yearMonth: YearMonth, mealType: String): List<Int> {
        // 해당 월의 첫날과 마지막 날 계산
        val calendar = Calendar.getInstance()
        calendar.set(yearMonth.year, yearMonth.monthValue - 1, 1) // 1일 설정
        val firstDayOfMonth = calendar.time

        calendar.set(
            yearMonth.year,
            yearMonth.monthValue - 1,
            yearMonth.lengthOfMonth()
        ) // 마지막 날 설정
        val lastDayOfMonth = calendar.time

        // mealType 조건에 따라 필터링 로직 결정
        val filteredMeals = mealList.filter { meal ->
            (meal.date.after(firstDayOfMonth) && meal.date.before(lastDayOfMonth) || meal.date == firstDayOfMonth) &&
                    (mealType.isEmpty() || meal.mealType == mealType)
        }

        // 해당 월의 일 수만큼 초기 리스트 생성 (모든 값을 0으로 초기화)
        val daysInMonth = yearMonth.lengthOfMonth()
        val costPerDay = MutableList(daysInMonth) { 0 }

        // 데이터를 해당 날짜 인덱스에 매핑
        filteredMeals.forEach { meal ->
            calendar.time = meal.date
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            costPerDay[dayOfMonth - 1] += meal.foodCost
        }

        return costPerDay
    }
}