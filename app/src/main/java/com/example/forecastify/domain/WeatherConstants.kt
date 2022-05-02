package com.example.forecastify.domain

import com.example.forecastify.BuildConfig

object WeatherConstants {
    const val API_KEY = BuildConfig.weatherApiKey
    const val API_BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val QUERY_APP_ID = "appid"
    const val QUERY_LATITUDE = "lat"
    const val QUERY_LONGITUDE = "lon"
    const val QUERY_LANGUAGE = "lang"
    const val QUERY_UNITS = "units"
    const val QUERY_DAY_COUNT = "cnt"
    const val OW_METRIC_TYPE = "metric"
    const val OW_FUTURE_DAY_COUNT = 14
}