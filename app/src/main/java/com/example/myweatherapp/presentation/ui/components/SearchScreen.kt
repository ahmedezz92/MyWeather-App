package com.example.myweatherapp.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myweatherapp.R
import com.example.myweatherapp.presentation.ui.screens.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    weatherViewModel: WeatherViewModel,
    navController: NavHostController,
) {
    val isLoading by weatherViewModel.isLoading.collectAsState()
    val isError by weatherViewModel.errorCode.collectAsState()

    val weatherState by weatherViewModel.currentCityWeather.collectAsState()

    isLoading.takeIf { it }?.let {
        LoadingState()
    }
    isError.takeIf { it != 200 }?.let {
        ErrorState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.label_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontStyle = FontStyle.Italic,
            )
        }
        SearchBar(
            onSearch = { city ->
                CoroutineScope(Dispatchers.IO).launch {
                    weatherViewModel.getCityCurrentWeather(city).collect { state ->
                        weatherViewModel.handleStateCityCurrentWeather(state)
                    }
                }

            }
        )

        weatherState?.let {
            WeatherRow(weather = it, onCityClick = { cityName ->
                navController.navigate("cityWeatherForecast/$cityName")
            })
        }

    }
}


