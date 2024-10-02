package com.example.myweatherapp.presentation.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.data.remote.model.WeatherResponse
import com.example.myweatherapp.base.BaseResult
import com.example.myweatherapp.core.data.utils.ErrorResponse
import com.example.myweatherapp.domain.usecase.GetCityCurrentWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCityCurrentWeatherUseCase: GetCityCurrentWeatherUseCase,
) : ViewModel() {

    private val _currentWeatherState =
        MutableStateFlow<GetCityCurrentWeatherState>(GetCityCurrentWeatherState.IsLoading)
    val currentWeatherState: StateFlow<GetCityCurrentWeatherState> =
        _currentWeatherState.asStateFlow()

    private val _currentCityWeather = MutableStateFlow<WeatherResponse?>(null)
    val currentCityWeather: StateFlow<WeatherResponse?> = _currentCityWeather

    /*loading progress for loading state*/
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /*error messages*/
    private val _errorResponse = MutableStateFlow<ErrorResponse?>(null)
    val errorResponse: StateFlow<ErrorResponse?> = _errorResponse

    private var currentCity: String? = null


    private fun setLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }

    fun getCityCurrentWeather(cityName: String): Flow<GetCityCurrentWeatherState> = flow {
        getCityCurrentWeatherUseCase.execute(cityName)
            .onStart {
                setLoading()
            }
            .catch { exception ->
                hideLoading()
                Log.e("exp", exception.message.toString())

            }
            .collect { result ->
                hideLoading()
                when (result) {
                    is BaseResult.ErrorState -> {
                        val errorState =
                            GetCityCurrentWeatherState.Error(result.errorResponse!!)
                        _currentWeatherState.value = errorState
                        emit(errorState)
                    }

                    is BaseResult.DataState -> {
                        result.items?.let { data ->
                            val successState = GetCityCurrentWeatherState.Success(data)
                            _currentWeatherState.value = successState
                            _currentCityWeather.value = successState.data
                            currentCity = cityName
                            emit(successState)
                        }
                    }
                }
            }
    }

    fun handleStateCityCurrentWeather(state: GetCityCurrentWeatherState) {
        _currentWeatherState.value = state
        when (state) {
            is GetCityCurrentWeatherState.IsLoading -> {
                setLoading()
            }

            is GetCityCurrentWeatherState.Success -> {
                _errorResponse.value = null
                _currentWeatherState.value = GetCityCurrentWeatherState.Success(
                    state.data
                )
                _currentCityWeather.value = state.data // Update this line to set the current city weather
            }

            is GetCityCurrentWeatherState.Error -> {
                _errorResponse.value = state.errorResponse
            }
        }
    }

    fun clearError() {
        _errorResponse.value = null
    }

}

sealed class GetCityCurrentWeatherState {
    object IsLoading : GetCityCurrentWeatherState()
    data class Success(val data: WeatherResponse) : GetCityCurrentWeatherState()
    data class Error(val errorResponse: ErrorResponse) : GetCityCurrentWeatherState()
}
