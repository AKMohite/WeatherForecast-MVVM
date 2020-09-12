package com.example.forecastify.data.network

import androidx.lifecycle.LiveData
import com.example.forecastify.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {

    val fetchedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )
}