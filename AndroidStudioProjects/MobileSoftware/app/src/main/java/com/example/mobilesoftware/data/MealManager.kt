package com.example.mobilesoftware.data

import android.net.Uri
import com.example.mobilesoftware.data.Meal.Companion.createMeal
import java.time.YearMonth
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

class MealManager {
    private var mealList = mutableListOf<Meal>()

    private var isDataGenerated = false

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

    fun getCalorieByMonthAndType(yearMonth: YearMonth, mealType: String): List<Int> {
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

        // meal.date와 firstDayOfMonth, lastDayOfMonth의 시간 정보 무시하고 날짜만 비교
        val filteredMeals = mealList.filter { meal ->
            val mealDate = meal.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val startDate = firstDayOfMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val endDate = lastDayOfMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            (mealDate >= startDate && mealDate <= endDate) &&
                    (mealType == "월별" || meal.mealType == mealType)
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

        // meal.date와 firstDayOfMonth, lastDayOfMonth의 시간 정보 무시하고 날짜만 비교
        val filteredMeals = mealList.filter { meal ->
            val mealDate = meal.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val startDate = firstDayOfMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val endDate = lastDayOfMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            (mealDate >= startDate && mealDate <= endDate) &&
                    (mealType == "월별" || meal.mealType == mealType)
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

        // meal.date와 firstDayOfMonth, lastDayOfMonth의 시간 정보 무시하고 날짜만 비교
        val filteredMeals = mealList.filter { meal ->
            val mealDate = meal.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val startDate = firstDayOfMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val endDate = lastDayOfMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            (mealDate >= startDate && mealDate <= endDate) &&
                    (mealType == "월별" || meal.mealType == mealType)
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

    fun generateMealData() {
        if (isDataGenerated) {
            // 이미 호출된 경우 함수 실행 종료
            return
        }

        val foodNames1 = listOf(
            "계란라면", "치즈라면", "해장라면", "부대라면", "공기밥", "비비고찐만두", "잔치국수", "비빔국수",
            "라볶이", "떡순이", "삼겹살김치철판", "치즈불닭철판", "데리야끼치킨솥밥", "숯불삼겹솥밥", "콘치즈솥밥",
            "우삼겹솥밥", "꼬치어묵우동", "우동*돈가스set", "우동*알밥set", "왕새우튀김우동", "철판돈가스",
            "낙지삼겹솥밥", "돈코츠라멘"
        )
        val foodNames2 = listOf(
            "수육국밥", "순대국밥", "얼큰국밥", "통등심돈가스 떡볶이 그린샐러드",
            "양념치킨가스 감자튀김 그린샐러드", "추억의도시락"
        )
        val foodNames3 = listOf(
            "돈수육국밥", "불고기열무비빔밥", "돈갈비김치찌개", "치즈부대찌개", "게살새우볶음밥*짜장", "꼬치어묵국", "탕수육"
        )
        val foodNameG = listOf(
            "알리오올리오", "까르보나라", "스파이시까르보나라", "명란오일파스타", "명란크림파스타", "베이컨로제리조또",
            "베이컨김치볶음밥", "토마토쉬림프파스타", "쉬림프로제파스타", "짬뽕파스타"
        )
        val foodNamesS = listOf(
            "토스트/미니꿀호떡&딸기잼", "그린샐러드&드레싱", "시리얼&우유/주스팩", "고깃집볶음밥", "오징어덮밥",
            "다시마감잣국", "해물된장찌개", "오므라이스&소스", "치킨가스&케찹", "쇠고기콩나물밥&양념장"
        )
        val foodNamesC = listOf(
            "아메리카노", "카페라떼", "카푸치노", "카페모카", "바닐라라떼", "카라멜마끼야또",
            "카라멜모카", "화이트모카", "화이트카라멜모카", "에스프레소", "카페마끼야또",
            "카페콘파나", "도피오", "헤즐넛라떼", "그린티", "아이스그린티", "허브티",
            "아이스허브티", "홍차", "레몬차", "자몽차", "유자차", "카푸치노프라페", "카페모카프라페",
            "카라멜프라페", "초코민트프라페", "자바칩프라페", "그린티프라페", "오곡프라페",
            "청포도프라페", "유자프라페", "밀크쉐이크", "과일프라페", "밀크티", "그린티라떼",
            "오곡라떼", "고구마라떼", "홍삼라떼", "생과일주스", "블랙티라떼", "토피몬드라떼",
            "아이스티", "에이드", "초코라떼", "화이트초코라떼"
        )
        val allRestaurants = listOf(
            "상록원 1층", "상록원 2층", "상록원 3층", "고양학사", "남산학사", "카페"
        )

        // 날짜 범위 설정 (2024년 11월 1일부터 12월 14일까지)
        val calendar = Calendar.getInstance()
        calendar.set(2024, 10, 1)  // 2024년 11월 1일 (Calendar 월은 0부터 시작하므로 10월로 설정)
        val startDate = calendar.time  // Date 객체로 변환

        calendar.set(2024, 11, 13)  // 2024년 12월 15일
        val endDate = calendar.time  // Date 객체로 변환

        var currentDate = startDate

        // 날짜마다 반복해서 Meal 객체 생성
        while (currentDate.before(endDate)) {
            val mealTypes = listOf("조식", "중식", "석식", "간식 or 음료")

            // 각 식사 타입마다 반복
            mealTypes.forEach { mealType ->
                val randomRestaurant = if (mealType == "조식" || mealType == "중식" || mealType == "석식") {
                    "상록원 1층"
                } else {
                    "카페"
                }

                // 각 식사에 맞는 음식 리스트를 랜덤으로 선택
                val restaurantMeals = when (mealType) {
                    "조식", "중식", "석식" -> foodNames1
                    "간식 or 음료" -> foodNamesC
                    else -> foodNames2
                }

                val randomFood = restaurantMeals.random()

                // 더미 데이터 생성 (음식 리뷰와 가격은 임의로 설정)
                val foodReview = "맛있어요!"
                val foodCost = listOf(4000, 4500, 5000, 5500, 6000, 6500, 7000, 7500, 8000).random()
                val foodImage: Uri? = null // 이미지 URL은 임의로 설정

                mealList.add(
                    createMeal(
                        date = currentDate,
                        mealType = mealType,
                        restaurant = randomRestaurant,
                        foodImage = foodImage,
                        foodName = randomFood,
                        foodReview = foodReview,
                        foodCost = foodCost
                    )
                )
            }

            // 하루씩 증가
            currentDate = Date(currentDate.time + 1000 * 60 * 60 * 24)
        }

        // 2024년 12월 13일 9시로 설정
        calendar.set(2024, 11, 13, 9, 0, 0)
        // Date 객체로 변환
        val date1 = calendar.time

        mealList.add(
            createMeal(
                date = date1,
                mealType = "조식",
                restaurant = "상록원 2층",
                foodImage = Uri.parse("C:\\Users\\Win\\Downloads\\bab.jpg"),
                foodName = "수육국밥",
                foodReview = "뜨끈하고 너무 맛있다.",
                foodCost = 6500
            )
        )
        mealList.add(
            createMeal(
                date = date1,
                mealType = "중식",
                restaurant = "상록원 1층",
                foodImage = null,
                foodName = "삼겹살김치철판",
                foodReview = "든든하고 맛있다.",
                foodCost = 5500
            )
        )
        mealList.add(
            createMeal(
                date = date1,
                mealType = "석식",
                restaurant = "고양학사",
                foodImage = null,
                foodName = "명란크림파스타",
                foodReview = "고급 레스토랑의 파스타 맛이 난다.",
                foodCost = 8000
            )
        )
        mealList.add(
            createMeal(
                date = date1,
                mealType = "간식 or 음료",
                restaurant = "카페",
                foodImage = null,
                foodName = "아메리카노",
                foodReview = "시원하다.",
                foodCost = 2500
            )
        )

        // 2024년 12월 14일 9시로 설정
        calendar.set(2024, 11, 14, 9, 0, 0)
        // Date 객체로 변환
        val date2 = calendar.time

        mealList.add(
            createMeal(
                date = date2,
                mealType = "조식",
                restaurant = "남산학사",
                foodImage = null,
                foodName = "오므라이스&소스",
                foodReview = "간이 맞고 맛있다.",
                foodCost = 5500
            )
        )
        mealList.add(
            createMeal(
                date = date2,
                mealType = "중식",
                restaurant = "상록원 1층",
                foodImage = null,
                foodName = "치즈불닭철판",
                foodReview = "맵지만 맛있다.",
                foodCost = 5800
            )
        )
        mealList.add(
            createMeal(
                date = date2,
                mealType = "석식",
                restaurant = "상록원 3층",
                foodImage = null,
                foodName = "치킨마요덮밥",
                foodReview = "소스가 너무 맛있다.",
                foodCost = 5500
            )
        )
        mealList.add(
            createMeal(
                date = date2,
                mealType = "간식 or 음료",
                restaurant = "카페",
                foodImage = null,
                foodName = "카페라떼",
                foodReview = "부드럽고 맛있다.",
                foodCost = 4500
            )
        )


        // 첫 번째 호출 후에는 `isDataGenerated`를 true로 설정
        isDataGenerated = true
    }
}

