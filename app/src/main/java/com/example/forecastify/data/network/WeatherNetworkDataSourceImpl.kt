package com.example.forecastify.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecastify.data.WeatherAPI
import com.example.forecastify.data.network.response.CurrentWeatherResponse
import java.lang.Exception

class WeatherNetworkDataSourceImpl(
    private val apiWeatherService: WeatherAPI
) : WeatherNetworkDataSource {

    val _fetchedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()

    override val fetchedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _fetchedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchCurrentWeather = apiWeatherService
                .getCurrentWeather(location)
                .await()
            _fetchedCurrentWeather.postValue(fetchCurrentWeather)
        } catch (e: Exception){
            Log.e("NetworkSourceException", "Error: ${e.message}", e)
        }
    }
}