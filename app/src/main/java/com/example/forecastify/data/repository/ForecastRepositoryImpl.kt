package com.example.forecastify.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastify.data.db.CurrentWeatherDao
import com.example.forecastify.data.db.unitlocalised.UnitSpecificEntry
import com.example.forecastify.data.network.WeatherNetworkDataSource
import com.example.forecastify.data.network.response.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : ForecastRepository {

    init {
        weatherNetworkDataSource.fetchedCurrentWeather.observeForever{ newCurrentWeather ->
            persistCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificEntry> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if(metric)
                currentWeatherDao.getWeatherMetric()
            else
                currentWeatherDao.getWeatherImperial()
        }
    }

    private suspend fun initWeatherData() {
        if (isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentWeather(
            "London",
            Locale.getDefault().language
        )
    }
    
    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val hourAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(hourAgo)
    }

    private fun persistCurrentWeather(fetchedWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO){
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
        }
    }
}