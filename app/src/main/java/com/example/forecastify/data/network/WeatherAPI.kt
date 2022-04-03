package com.example.forecastify.data.network

import com.example.forecastify.data.network.response.CurrentWeatherResponse
import com.example.forecastify.data.network.response.FutureWeatherResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

//api.weatherstack.com/current?access_key=API_KEY&query=&language
interface WeatherAPI {

//    todo change default language we have to check

    @GET("current")
    fun getCurrentWeather(
        @Query("query") location: String
    ): Deferred<CurrentWeatherResponse>

    @GET("forecast")
    fun getFutureWeather(
        @Query("query") location: String,
        @Query("forecast_days") days: Int
    ): Deferred<FutureWeatherResponse>

}