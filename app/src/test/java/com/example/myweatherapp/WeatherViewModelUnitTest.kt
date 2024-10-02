package com.example.myweatherapp

import app.cash.turbine.test
import com.example.myweatherapp.base.BaseResult
import com.example.myweatherapp.core.data.utils.ErrorResponse
import com.example.myweatherapp.data.remote.model.Condition
import com.example.myweatherapp.data.remote.model.Current
import com.example.myweatherapp.data.remote.model.Location
import com.example.myweatherapp.data.remote.model.WeatherResponse
import com.example.myweatherapp.domain.usecase.GetCityCurrentWeatherUseCase
import com.example.myweatherapp.presentation.ui.screens.GetCityCurrentWeatherState
import com.example.myweatherapp.presentation.ui.screens.WeatherViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherViewModelUnitTest {
    // Mock the use case dependency
    private val getCityCurrentWeatherUseCase: GetCityCurrentWeatherUseCase = mockk()

    // ViewModel to test
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        // Initialize the ViewModel with the mocked use case
        viewModel = WeatherViewModel(getCityCurrentWeatherUseCase)
    }

    @Test
    fun `when city weather is fetched successfully, should update state with success`() = runTest {
        // Given: Mock a successful response from the use case
        val mockWeatherResponse = WeatherResponse(
            Location(name = "London", country = "", region = ""),
            Current(
                humidity = 0,
                feelslike_c = 0.0,
                last_updated = "",
                temp_c = 0.0,
                condition = Condition(text = "", icon = ""),
                wind_kph = 0.0,
                wind_degree = 0
            )
        )
        coEvery { getCityCurrentWeatherUseCase.execute(any()) } returns flow {
            emit(BaseResult.DataState(mockWeatherResponse))
        }

        // When: Calling getCityCurrentWeather() on the ViewModel
        viewModel.getCityCurrentWeather("London").test {

            // Then: Verify success state is emitted
            val successState = awaitItem() as GetCityCurrentWeatherState.Success
            assertEquals(successState.data.location.name, "London")

            // Complete test
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when city weather fetch fails, should update state with error`() = runTest {
        // Given: Mock an error response from the use case
        val mockErrorResponse = ErrorResponse(code = 1006, message = "City not found")
        coEvery { getCityCurrentWeatherUseCase.execute(any()) } returns flow {
            emit(BaseResult.ErrorState(mockErrorResponse))
        }

        // When: Calling getCityCurrentWeather() on the ViewModel
        viewModel.getCityCurrentWeather("UnknownCity").test {
            // Then: Verify error state is emitted
            val errorState = awaitItem() as GetCityCurrentWeatherState.Error
            assertEquals(errorState.errorResponse.message, "City not found")

            // Complete test
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `test clearError should reset error state`() = runTest {
        // Given: Setting an initial error state
        val mockErrorResponse = ErrorResponse(code = 404, message = "Error")
        viewModel.handleStateCityCurrentWeather(GetCityCurrentWeatherState.Error(mockErrorResponse))

        // When: Calling clearError()
        viewModel.clearError()

        // Then: Assert that errorResponse is null
        assertEquals(viewModel.errorResponse.value, null)
    }

    @Test
    fun `handleStateCityCurrentWeather should update state correctly`() = runTest {
        // Given: Mock a success state
        val mockWeatherResponse = WeatherResponse(
            Location(name = "London", country = "", region = ""),
            Current(
                humidity = 0,
                feelslike_c = 0.0,
                last_updated = "",
                temp_c = 0.0,
                condition = Condition(text = "", icon = ""),
                wind_kph = 0.0,
                wind_degree = 0
            )
        )
        val successState = GetCityCurrentWeatherState.Success(mockWeatherResponse)

        // When: Handling success state
        viewModel.handleStateCityCurrentWeather(successState)

        // Then: Assert that the state is updated correctly
        assertEquals(viewModel.currentWeatherState.value, successState)
        assertEquals(viewModel.currentCityWeather.value?.location?.name, "London")
    }
}