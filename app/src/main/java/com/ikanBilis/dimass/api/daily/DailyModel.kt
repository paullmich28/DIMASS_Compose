package com.ikanBilis.dimass.api.daily

data class DailyModel(
    val meals: List<MealDaily>,
    val nutrients: NutrientsDaily
)