package com.example.myweatherapp.presentation.ui.screens

import androidx.lifecycle.ViewModel
import com.example.myweatherapp.data.remote.model.ResultResponse
import com.example.myweatherapp.data.remote.model.WeatherResponse
import com.example.myweatherapp.base.BaseResult
import com.example.myweatherapp.domain.usecase.GetCityCurrentWeather
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
    private val getCityCurrentWeather: GetCityCurrentWeather,
) : ViewModel() {

    private val _currentWeatherState =
        MutableStateFlow<GetWeatherState>(GetWeatherState.IsLoading)
    val currentWeatherState: StateFlow<GetWeatherState> = _currentWeatherState.asStateFlow()

    private val _currentCityWeather = MutableStateFlow<WeatherResponse?>(null)
    val currentCityWeather: StateFlow<WeatherResponse?> = _currentCityWeather

    var isLoaded: Boolean = false

    /*loading progress for loading state*/
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /*error messages*/
    private val _errorCode = MutableStateFlow(200)
    val errorCode: StateFlow<Int> = _errorCode

    private var currentCity: String? = null


    private fun setLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }

    fun getCityCurrentWeather(cityName: String): Flow<GetWeatherState> = flow {
        getCityCurrentWeather.execute(cityName)
            .onStart {
                setLoading()
            }
            .catch {
                hideLoading()
                emit(
                    GetWeatherState.Error(
                        errorCode = -1
                    )
                )
            }
            .collect { result ->
                hideLoading()
                when (result) {
                    is BaseResult.ErrorState -> {
                        val errorState =
                            GetWeatherState.Error(result.errorCode)
                        _currentWeatherState.value = errorState
                        emit(errorState)
                    }

                    is BaseResult.DataState -> {
                        result.items?.let { movies ->
                            val successState = GetWeatherState.Success(movies)
                            _currentWeatherState.value = successState
                            _currentCityWeather.value = successState.data
                            currentCity = cityName

                            emit(successState)
                        } ?: emit(GetWeatherState.Error(-1))
                    }
                }
            }
    }

    fun handleStateCityCurrentWeather(state: GetWeatherState) {
        _currentWeatherState.value = state
        when (state) {

            is GetWeatherState.IsLoading -> {
                setLoading()
            }

            is GetWeatherState.Success -> {
                _currentWeatherState.value = GetWeatherState.IsLoading
                _currentWeatherState.value = GetWeatherState.Success(
                    state.data
                )
                isLoaded = true
            }

            is GetWeatherState.Error -> {
                _errorCode.value = state.errorCode

            }

        }
    }

//    fun getMovieById(id: Int): ResultResponse? {
//        return _cityCurrentWeatherResponse.value. { it.id == id }
//    }


    sealed class GetWeatherState {
        object IsLoading : GetWeatherState()
        data class Success(val data: WeatherResponse) : GetWeatherState()
        data class Error(val errorCode: Int) : GetWeatherState()
    }
}