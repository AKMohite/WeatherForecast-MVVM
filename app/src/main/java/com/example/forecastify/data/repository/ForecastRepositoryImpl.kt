package com.example.forecastify.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastify.data.db.CurrentWeatherDao
import com.example.forecastify.data.db.FutureWeatherDao
import com.example.forecastify.data.db.WeatherLocationDao
import com.example.forecastify.data.db.entity.FutureWeatherEntry
import com.example.forecastify.data.db.entity.WeatherLocationEntry
import com.example.forecastify.data.db.unitlocalised.current.UnitSpecificCurrentEntry
import com.example.forecastify.data.db.unitlocalised.future.UnitSpecificFutureEntry
import com.example.forecastify.data.network.FORECAST_DAYS_COUNT
import com.example.forecastify.data.network.WeatherNetworkDataSource
import com.example.forecastify.data.network.response.CurrentWeatherResponse
import com.example.forecastify.data.network.response.FutureWeatherInfo
import com.example.forecastify.data.network.response.FutureWeatherResponse
import com.example.forecastify.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*
import kotlin.collections.ArrayList

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    init {
        weatherNetworkDataSource.apply {
            fetchedCurrentWeather.observeForever{ newCurrentWeather ->
                persistCurrentWeather(newCurrentWeather)
            }

            fetchedFutureWeather.observeForever{ newFutureWeather ->
                persistFutureWeather(newFutureWeather)
            }
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentEntry> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if(metric)
                currentWeatherDao.getWeatherMetric()
            else
                currentWeatherDao.getWeatherImperial()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocationEntry> {
        return withContext(Dispatchers.IO){
            return@withContext weatherLocationDao.getLocation()
        }
    }

    override suspend fun getFutureWeatherList(
        startDate: LocalDate,
        metric: Boolean
    ): LiveData<out List<UnitSpecificFutureEntry>> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            val count = futureWeatherDao.countFutureWeather(startDate)
            return@withContext if(metric)
                futureWeatherDao.getWeatherForecastMetric(startDate)
            else
                futureWeatherDao.getWeatherForecastImperial(startDate)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()

        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)){
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather()

        if (isFetchFutureNeeded())
            fetchFutureWeather()
    }

    private suspend fun fetchCurrentWeather(){
        weatherNetworkDataSource.fetchCurrentWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }
    
    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val hourAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(hourAgo)
    }

    private fun isFetchFutureNeeded(): Boolean {
        val today = LocalDate.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT
    }

    private suspend fun fetchFutureWeather() {
        weatherNetworkDataSource.fetchFutureWeather(
            locationProvider.getPreferredLocationString(),
            Locale.getDefault().language
        )
    }

    private fun persistCurrentWeather(fetchedWeather: CurrentWeatherResponse){
        GlobalScope.launch(Dispatchers.IO){
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private fun persistFutureWeather(fetchedWeather: FutureWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            val futureWeatherList = ArrayList(fetchedWeather.forecastContainer.values)
            futureWeatherList.forEachIndexed { index, futureWeatherInfo ->
                futureWeatherDao.insert(mapEntity(index, futureWeatherInfo))
            }
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private fun mapEntity(index: Int, it: FutureWeatherInfo): FutureWeatherEntry {
        return FutureWeatherEntry(
//            astro = it.astro,
            avgtemp = it.avgtemp,
            date = LocalDate.now().plusDays((index + 1).toLong()).toString(), // todo dummy date set
            maxtemp= it.maxtemp,
            mintemp= it.mintemp,
            sunhour= it.sunhour,
            totalsnow= it.totalsnow,
            uvIndex= it.uvIndex,
            conditionIconUrl = it?.hourly?.get(0)?.weatherIcons?.get(0),
            conditionText = it?.hourly?.get(0)?.weatherDescriptions?.joinToString(", ")
        )
    }

    private fun deleteOldForecastData() {
        val today = LocalDate.now()
        futureWeatherDao.deleteOldEntries(today)
    }
}