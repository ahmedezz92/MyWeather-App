package com.example.myweatherapp.data.repository

import com.example.myweatherapp.data.remote.model.WeatherResponse
import com.example.myweatherapp.domain.repository.WeatherRepository
import com.example.myweatherapp.base.BaseResult
import com.example.myweatherapp.core.data.utils.WrappedResponse
import com.example.myweatherapp.data.remote.api.WeatherApiService
import com.example.myweatherapp.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImp @Inject constructor(
    private val movieApiService: WeatherApiService,
) :
    WeatherRepository {
    override suspend fun getCurrentWeather(city: String): Flow<BaseResult<WeatherResponse>> {
        return flow {
            val response = movieApiService.getCurrentWeather(
                query = city,
                key = Constants.Authorization.API_KEY
            )
            if (response.isSuccessful) {
                val body = response.body()!!
                emit(BaseResult.DataState(body))
            } else {
                val errorBody = response.errorBody()?.charStream()
                val type = object : TypeToken<WrappedResponse<WeatherResponse>>() {}.type
                val errorResponse: WrappedResponse<WeatherResponse> =
                    Gson().fromJson(errorBody, type)
                emit(BaseResult.ErrorState(response.code()))
            }
        }
    }

    override suspend fun getPopularMovies(): Flow<BaseResult<WeatherResponse>> {
        return flow {
            val response = movieApiService.getPopularMovies()
            if (response.isSuccessful) {
                val body = response.body()!!
                emit(BaseResult.DataState(body))
            } else {
                val errorBody = response.errorBody()?.charStream()
                val type = object : TypeToken<WrappedResponse<WeatherResponse>>() {}.type
                val errorResponse =
                    Gson().fromJson<WrappedResponse<WeatherResponse>>(errorBody, type)
                emit(BaseResult.ErrorState(response.code()))
            }
        }
    }

    override suspend fun getNowPlayingMovies(): Flow<BaseResult<WeatherResponse>> {
        return flow {
            val response = movieApiService.getNowPlayingMovies()
            if (response.isSuccessful) {
                val body = response.body()!!
                emit(BaseResult.DataState(body))
            } else {
                val errorBody = response.errorBody()?.charStream()
                val type = object : TypeToken<WrappedResponse<WeatherResponse>>() {}.type
                val errorResponse =
                    Gson().fromJson<WrappedResponse<WeatherResponse>>(errorBody, type)
                emit(BaseResult.ErrorState(response.code()))
            }
        }
    }


}