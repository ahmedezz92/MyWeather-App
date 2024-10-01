package com.example.myweatherapp.data.remote.api

import com.example.myweatherapp.data.remote.model.ForecastResponse
import com.example.myweatherapp.data.remote.model.WeatherResponse
import com.example.myweatherapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String,
        @Query("q") query: String,
    ): Response<WeatherResponse>

    @GET("forecast.json")
    suspend fun getCityForecast(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("days") days: Int
    ): Response<ForecastResponse>
}
