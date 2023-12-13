package com.example.dimass.api.service

import com.example.dimass.api.daily.DailyModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceDaily {
    @GET("mealplanner/generate")
    fun getDailyData(
        @Query("apiKey") apiKey : String,
        @Query("timeFrame") timeFrame : String,
        @Query("targetCalories") targetCalories : Int?
    ) : Call<DailyModel>
}