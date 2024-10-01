package com.example.myweatherapp.presentation.ui.components.forecast

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.base.BaseResult
import com.example.myweatherapp.core.data.utils.ErrorResponse
import com.example.myweatherapp.data.remote.model.ForecastDay
import com.example.myweatherapp.data.remote.model.ForecastResponse
import com.example.myweatherapp.domain.usecase.GetCityForecastUseCase
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
class CityForecastViewModel @Inject constructor(
    private val getCityForecastUseCase: GetCityForecastUseCase,
) : ViewModel() {

    private val _cityForecastState =
        MutableStateFlow<GetCityForecastState>(GetCityForecastState.IsLoading)
    val cityForecastState: StateFlow<GetCityForecastState> = _cityForecastState.asStateFlow()

    private val _cityForecastResponse = MutableStateFlow<List<ForecastDay>>(emptyList())
    val cityForecastList: StateFlow<List<ForecastDay>> = _cityForecastResponse

    var isLoaded: Boolean = false

    /*loading progress for loading state*/
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /*error messages*/
    private val _errorResponse = MutableStateFlow<ErrorResponse?>(null)
    val errorResponse: StateFlow<ErrorResponse?> = _errorResponse

    private var currentCity: String? = null
    fun getCity5daysForecast(cityName: String): Flow<GetCityForecastState> = flow {
        getCityForecastUseCase.execute(cityName)
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
                            GetCityForecastState.Error(result.errorResponse!!)
                        _cityForecastState.value = errorState
                        emit(errorState)
                    }

                    is BaseResult.DataState -> {
                        result.items?.let { data ->
                            val successState = GetCityForecastState.Success(data)
                            _cityForecastState.value = successState
                            _cityForecastResponse.value = successState.data.forecast.forecastday
                            currentCity = cityName

                            emit(successState)
                        }
                    }
                }
            }
    }

    fun handleStateCityCurrentWeather(state: GetCityForecastState) {
        _cityForecastState.value = state
        when (state) {
            is GetCityForecastState.IsLoading -> {
                setLoading()
            }

            is GetCityForecastState.Success -> {
                _cityForecastState.value = GetCityForecastState.IsLoading
                _cityForecastState.value = GetCityForecastState.Success(
                    state.data
                )
                isLoaded = true
            }

            is GetCityForecastState.Error -> {
                _errorResponse.value = state.errorResponse

            }
        }
    }

    private fun setLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }
}

sealed class GetCityForecastState {
    object IsLoading : GetCityForecastState()
    data class Success(val data: ForecastResponse) : GetCityForecastState()
    data class Error(val errorResponse: ErrorResponse) : GetCityForecastState()
}