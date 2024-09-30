package com.example.myweatherapp.data.remote.api

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

    @GET("movie/popular")
    suspend fun getPopularMovies(): Response<WeatherResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(): Response<WeatherResponse>
}
