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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCityCurrentWeather: GetCityCurrentWeather,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _getMoviesState =
        MutableStateFlow<GetWeatherActivityState>(GetWeatherActivityState.Init)

    /*upcoming movies variables*/
    private val _cityCurrentWeatherState =
        MutableStateFlow<GetWeatherActivityState>(GetWeatherActivityState.Init)
    val cityCurrentWeatherState: StateFlow<GetWeatherActivityState> = _cityCurrentWeatherState

    private val _cityCurrentWeatherResponse = MutableStateFlow<WeatherResponse?>(null)
    val cityCurrentWeatherResponse: StateFlow<WeatherResponse?> = _cityCurrentWeatherResponse
    var isLoaded: Boolean = false

    /*loading progress for loading state*/
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /*error messages*/
    private val _errorCode = MutableStateFlow(200)
    val errorCode: StateFlow<Int> = _errorCode

    private fun setLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }

    fun getCityCurrentWeather(cityName: String): Flow<GetWeatherActivityState> = flow {
        getCityCurrentWeather.execute(cityName)
            .onStart {
                setLoading()
            }
            .catch {
                hideLoading()
                emit(
                    GetWeatherActivityState.Error(
                        errorCode = -1
                    )
                )
            }
            .collect { result ->
                hideLoading()
                when (result) {
                    is BaseResult.ErrorState -> {
                        val errorState =
                            GetWeatherActivityState.Error(result.errorCode)
                        _cityCurrentWeatherState.value = errorState
                        emit(errorState)
                    }

                    is BaseResult.DataState -> {
                        result.items?.let { movies ->
                            val successState = GetWeatherActivityState.Success(movies)
                            _cityCurrentWeatherState.value = successState
                            _cityCurrentWeatherResponse.value = successState.data
                            emit(successState)
                        } ?: emit(GetWeatherActivityState.Error(-1))
                    }
                }
            }
    }

    fun handleStateCityCurrentWeather(state: GetWeatherActivityState) {
        _cityCurrentWeatherState.value = state
        when (state) {
            is GetWeatherActivityState.Init -> Unit

            is GetWeatherActivityState.IsLoading -> {
                setLoading()
            }

            is GetWeatherActivityState.Success -> {
                _getMoviesState.value = GetWeatherActivityState.IsLoading(false)
                _cityCurrentWeatherState.value = GetWeatherActivityState.Success(
                    state.data
                )
                isLoaded = true
            }

            is GetWeatherActivityState.Error -> {
                _errorCode.value = state.errorCode

            }

            is GetWeatherActivityState.ShowToast -> {
                if (state.isConnectionError)
                    _errorCode.value = state.code
            }
        }
    }

    fun onQueryChanged(query: String) {
        _query.value = query
    }

//    fun getMovieById(id: Int): ResultResponse? {
//        return _cityCurrentWeatherResponse.value. { it.id == id }
//    }


    sealed class GetWeatherActivityState {
        object Init : GetWeatherActivityState()
        data class IsLoading(val isLoading: Boolean) : GetWeatherActivityState()
        data class ShowToast(val code: Int, val isConnectionError: Boolean) :
            GetWeatherActivityState()

        data class Success(val data: WeatherResponse) :
            GetWeatherActivityState()

        data class Error(val errorCode: Int) :
            GetWeatherActivityState()
    }
}