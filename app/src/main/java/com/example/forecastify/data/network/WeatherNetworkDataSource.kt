package com.example.forecastify.data.network

import androidx.lifecycle.LiveData
import com.example.forecastify.data.network.response.CurrentWeatherResponse
import com.example.forecastify.data.network.response.FutureWeatherResponse

interface WeatherNetworkDataSource {

    val fetchedCurrentWeather: LiveData<CurrentWeatherResponse>
    val fetchedFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        languageCode: String
    )

    suspend fun fetchFutureWeather(
        location: String,
        languageCode: String
    )
}