package com.example.myweatherapp.domain.repository

import com.example.myweatherapp.data.remote.model.WeatherResponse
import com.example.myweatherapp.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(city: String): Flow<BaseResult<WeatherResponse>>
    suspend fun getPopularMovies(): Flow<BaseResult<WeatherResponse>>
    suspend fun getNowPlayingMovies(): Flow<BaseResult<WeatherResponse>>
}