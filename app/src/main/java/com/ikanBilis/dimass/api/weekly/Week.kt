package com.ikanBilis.dimass.api.weekly

import com.ikanBilis.dimass.api.weekly.days.Friday
import com.ikanBilis.dimass.api.weekly.days.Monday
import com.ikanBilis.dimass.api.weekly.days.Saturday
import com.ikanBilis.dimass.api.weekly.days.Sunday
import com.ikanBilis.dimass.api.weekly.days.Thursday
import com.ikanBilis.dimass.api.weekly.days.Tuesday
import com.ikanBilis.dimass.api.weekly.days.Wednesday

data class Week(
    val friday: Friday,
    val monday: Monday,
    val saturday: Saturday,
    val sunday: Sunday,
    val thursday: Thursday,
    val tuesday: Tuesday,
    val wednesday: Wednesday
)