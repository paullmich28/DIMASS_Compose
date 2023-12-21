package com.ikanBilis.dimass.api.service

import com.ikanBilis.dimass.api.weekly.WeeklyModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceWeekly {
    @GET("mealplanner/generate")
    fun getWeeklyData(
        @Query("apiKey") apiKey : String,
        @Query("timeFrame") timeFrame : String,
        @Query("targetCalories") targetCalories : Int?,
        @Query("diet") diet : String?
    ) : Call<WeeklyModel>
}