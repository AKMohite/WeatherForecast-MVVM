package com.example.forecastify.data.provider

import com.example.forecastify.data.db.entity.WeatherLocationEntry

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocationEntry): Boolean
    suspend fun getPreferredLocationString(): Pair<Double, Double>
}