package com.example.myweatherapp.data.repository

import androidx.compose.ui.res.stringResource
import com.example.myweatherapp.R
import com.example.myweatherapp.data.remote.model.WeatherResponse
import com.example.myweatherapp.domain.repository.WeatherRepository
import com.example.myweatherapp.base.BaseResult
import com.example.myweatherapp.core.data.utils.ErrorResponse
import com.example.myweatherapp.core.data.utils.WrappedErrorResponse
import com.example.myweatherapp.core.data.utils.WrappedResponse
import com.example.myweatherapp.data.local.WeatherDao
import com.example.myweatherapp.data.local.WeatherEntity
import com.example.myweatherapp.data.remote.api.WeatherApiService
import com.example.myweatherapp.data.remote.model.ForecastResponse
import com.example.myweatherapp.utils.Constants
import com.example.myweatherapp.utils.toCurrentWeather
import com.example.myweatherapp.utils.toWeatherEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImp @Inject constructor(
    private val movieApiService: WeatherApiService,
    private val weatherDao: WeatherDao
) :
    WeatherRepository {
    companion object {
        private const val CACHE_TIMEOUT = 30 * 60 * 1000 // 30 minutes in milliseconds
    }

    override suspend fun getCurrentWeather(cityName: String): Flow<BaseResult<WeatherResponse>> {
        return flow {
            try {
                val cachedWeather = weatherDao.getWeatherForCity(cityName)
                if (cachedWeather != null &&
                    System.currentTimeMillis() - cachedWeather.timestamp < CACHE_TIMEOUT
                ) {
                    // Return cached data if it's still valid
                    emit(BaseResult.DataState(cachedWeather.toCurrentWeather()))
                    return@flow
                }
                val response = movieApiService.getCurrentWeather(
                    query = cityName,
                    key = Constants.Authorization.API_KEY
                )
                if (response.isSuccessful) {
                    val body = response.body()!!
                    // Cache the new data
                    val weatherEntity = body.toWeatherEntity(cityName)
                    weatherDao.insertWeather(weatherEntity)
                    // Clean up old cache entries
                    weatherDao.deleteOldCache(System.currentTimeMillis() - CACHE_TIMEOUT)
                    emit(BaseResult.DataState(body))
                } else {
                    val errorBody = response.errorBody()?.charStream()
                    val type = object : TypeToken<WrappedErrorResponse>() {}.type
                    val errorResponse: WrappedErrorResponse =
                        Gson().fromJson(errorBody, type)
                    emit(BaseResult.ErrorState(errorResponse.errorResponse))
                }
            } catch (e: Exception) {
                // Handle exception, fallback to cached data if available
                val cachedWeather = weatherDao.getWeatherForCity(cityName)
                if (cachedWeather != null) {
                    // Emit cached data if available during error
                    emit(BaseResult.DataState(cachedWeather.toCurrentWeather()))
                } else {
                    // Emit error state if no cached data is available
                    emit(BaseResult.ErrorState(errorResponse = ErrorResponse(404, "Unknown error")))
                }
            }
        }

    }

    override suspend fun getCityForecast(cityName: String): Flow<BaseResult<ForecastResponse>> {
        return flow {
            val response = movieApiService.getCityForecast(
                query = cityName,
                key = Constants.Authorization.API_KEY,
                days = 5
            )
            if (response.isSuccessful) {
                val body = response.body()!!
                emit(BaseResult.DataState(body))
            } else {
                val errorBody = response.errorBody()?.charStream()
                val type = object : TypeToken<WrappedErrorResponse>() {}.type
                val errorResponse: WrappedErrorResponse =
                    Gson().fromJson(errorBody, type)
                emit(BaseResult.ErrorState(errorResponse.errorResponse))
            }
        }
    }

}
