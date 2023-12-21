package com.ikanBilis.dimass.api.weekly.days

import com.ikanBilis.dimass.api.weekly.MealWeekly
import com.ikanBilis.dimass.api.weekly.NutrientsWeekly

data class Monday(
    val meals: List<MealWeekly>,
    val nutrients: NutrientsWeekly
)