package com.example.myweatherapp.domain.usecase

import com.example.myweatherapp.base.BaseResult
import com.example.myweatherapp.data.remote.model.ForecastResponse
import com.example.myweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCityForecastUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend fun execute(cityName: String): Flow<BaseResult<ForecastResponse>> {
        return weatherRepository.getCityForecast(cityName = cityName)
    }
}