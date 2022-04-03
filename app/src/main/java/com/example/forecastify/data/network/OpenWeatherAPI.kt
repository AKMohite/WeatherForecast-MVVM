package com.example.forecastify.data.network

import com.example.forecastify.data.network.response.CurrentWeatherDTO
import com.example.forecastify.data.network.response.WeatherForecastDTO
import com.example.forecastify.domain.WeatherConstants.API_KEY
import com.example.forecastify.domain.WeatherConstants.QUERY_APP_ID
import com.example.forecastify.domain.WeatherConstants.QUERY_DAY_COUNT
import com.example.forecastify.domain.WeatherConstants.QUERY_LANGUAGE
import com.example.forecastify.domain.WeatherConstants.QUERY_LATITUDE
import com.example.forecastify.domain.WeatherConstants.QUERY_LONGITUDE
import com.example.forecastify.domain.WeatherConstants.QUERY_UNITS
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPI {

    @GET("current")
    suspend fun getCurrentWeather(
        @Query(QUERY_APP_ID) apiKey: String = API_KEY, // todo add api keys in interceptors
        @Query(QUERY_LATITUDE) latitude: Double,
        @Query(QUERY_LONGITUDE) longitude: Double,
        @Query(QUERY_LANGUAGE) language: String,
        @Query(QUERY_UNITS) unitSystem: String
    ): CurrentWeatherDTO

    @GET("forecast/daily")
    suspend fun getFutureWeather(
        @Query(QUERY_APP_ID) apiKey: String = API_KEY, // todo add api keys in interceptors
        @Query(QUERY_LATITUDE) latitude: Double,
        @Query(QUERY_LONGITUDE) longitude: Double,
        @Query(QUERY_DAY_COUNT) dayCount: Int,
        @Query(QUERY_LANGUAGE) language: String,
        @Query(QUERY_UNITS) unitSystem: String
    ): WeatherForecastDTO

}