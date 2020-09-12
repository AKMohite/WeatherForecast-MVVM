package com.example.forecastify.data.network.response


import com.example.forecastify.data.db.entity.CurrentWeatherEntry
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: Location,
    val request: Request
)

data class Location(
    val country: String,
    val lat: String,
    val localtime: String,
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Int,
    val lon: String,
    val name: String,
    val region: String,
    @SerializedName("timezone_id")
    val timezoneId: String,
    @SerializedName("utc_offset")
    val utcOffset: String
)

data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)