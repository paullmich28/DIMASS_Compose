package com.example.dimass.api.weekly

import com.example.dimass.api.weekly.days.Friday
import com.example.dimass.api.weekly.days.Monday
import com.example.dimass.api.weekly.days.Saturday
import com.example.dimass.api.weekly.days.Sunday
import com.example.dimass.api.weekly.days.Thursday
import com.example.dimass.api.weekly.days.Tuesday
import com.example.dimass.api.weekly.days.Wednesday

data class Week(
    val friday: Friday,
    val monday: Monday,
    val saturday: Saturday,
    val sunday: Sunday,
    val thursday: Thursday,
    val tuesday: Tuesday,
    val wednesday: Wednesday
)