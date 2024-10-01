package com.example.myweatherapp.presentation.ui.components.forecast

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myweatherapp.data.remote.model.ForecastDay

@Composable
fun ForecastList(forecastList: List<ForecastDay>) {
    LazyColumn(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
        items(forecastList) { item ->
            ForecastItem(item)
        }
    }
}