package com.example.forecastify.data.network.response


import com.example.forecastify.data.db.entity.CurrentWeatherEntry
import com.example.forecastify.data.db.entity.WeatherLocationEntry
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocationEntry,
    val request: Request
)

data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)