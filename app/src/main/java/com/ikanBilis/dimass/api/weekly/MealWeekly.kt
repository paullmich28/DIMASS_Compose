package com.ikanBilis.dimass.api.weekly

data class MealWeekly(
    val id: Int,
    val imageType: String,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceUrl: String,
    val title: String
)