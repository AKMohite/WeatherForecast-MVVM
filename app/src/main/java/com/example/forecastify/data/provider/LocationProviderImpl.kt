package com.example.forecastify.data.provider

import com.example.forecastify.data.db.entity.WeatherLocationEntry

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocationEntry): Boolean {
        return true
    }

    override suspend fun getPreferredLocationString(): String {
        return "London"
    }
}