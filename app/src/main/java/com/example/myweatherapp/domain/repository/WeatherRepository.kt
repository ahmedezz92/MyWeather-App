package com.example.myweatherapp.domain.repository

import com.example.myweatherapp.data.remote.model.WeatherResponse
import com.example.myweatherapp.base.BaseResult
import com.example.myweatherapp.data.remote.model.ForecastResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(cityName: String): Flow<BaseResult<WeatherResponse>>
    suspend fun getCityForecast(cityName: String): Flow<BaseResult<ForecastResponse>>
}