package com.example.mobilesoftware.data

import android.net.Uri
import java.util.Date

data class Meal(
    val date: Date? = null,
    val mealType: String? = null,
    val restaurant: String? = null,
    val foodImage: Uri? = null,
    val foodName: String? = null,
    val foodReview: String? = null,
    val foodCost: Double? = null,
    val foodCalorie: Int? = null
)
