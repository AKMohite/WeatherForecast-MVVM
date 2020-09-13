package com.example.forecastify.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.forecastify.data.WeatherAPI
import com.example.forecastify.data.network.response.CurrentWeatherResponse
import com.example.forecastify.data.network.response.FutureWeatherResponse
import com.example.forecastify.internal.NoInternetConnectivityException
import java.lang.Exception


const val FORECAST_DAYS_COUNT = 7
class WeatherNetworkDataSourceImpl(
    private val apiWeatherService: WeatherAPI
) : WeatherNetworkDataSource {

    val _fetchedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()

    override val fetchedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _fetchedCurrentWeather

    private val _fetchedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val fetchedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _fetchedFutureWeather

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

    override suspend fun fetchFutureWeather(location: String, languageCode: String) {
        try {
            val fetchedFutureWeather = apiWeatherService
                .getFutureWeather(location, FORECAST_DAYS_COUNT)
                .await()
            _fetchedFutureWeather.postValue(fetchedFutureWeather)
        }
        catch (e: NoInternetConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}