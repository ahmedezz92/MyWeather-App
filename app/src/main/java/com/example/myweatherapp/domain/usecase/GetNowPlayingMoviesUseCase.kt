package com.example.myweatherapp.domain.usecase

import com.example.myweatherapp.data.remote.model.WeatherResponse
import com.example.myweatherapp.domain.repository.WeatherRepository
import com.example.myweatherapp.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend fun execute(): Flow<BaseResult<WeatherResponse>> {
        return weatherRepository.getNowPlayingMovies()
    }
}