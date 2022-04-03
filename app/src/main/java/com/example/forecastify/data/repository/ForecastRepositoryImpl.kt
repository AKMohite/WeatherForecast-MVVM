package com.example.forecastify.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastify.data.db.CurrentWeatherDao
import com.example.forecastify.data.db.FutureWeatherDao
import com.example.forecastify.data.db.WeatherDB
import com.example.forecastify.data.db.WeatherLocationDao
import com.example.forecastify.data.db.entity.FutureWeatherEntry
import com.example.forecastify.data.db.entity.WeatherLocationEntry
import com.example.forecastify.data.db.unitlocalised.current.UnitSpecificCurrentEntry
import com.example.forecastify.data.db.unitlocalised.future.detail.UnitDetailFutureWeatherEntry
import com.example.forecastify.data.db.unitlocalised.future.list.UnitSpecificFutureEntry
import com.example.forecastify.data.network.FORECAST_DAYS_COUNT
import com.example.forecastify.data.network.WeatherNetworkDataSource
import com.example.forecastify.data.network.response.CurrentWeatherResponse
import com.example.forecastify.data.network.response.FutureWeatherInfo
import com.example.forecastify.data.network.response.FutureWeatherResponse
import com.example.forecastify.data.provider.LocationProvider
import com.example.forecastify.internal.UnitType
import com.example.forecastify.internal.convertMetricToImperial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ForecastRepositoryImpl @Inject constructor(
    private val weatherDB: WeatherDB,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    private val currentWeatherDao: CurrentWeatherDao = weatherDB.currentWeatherDAO()
    private val futureWeatherDao: FutureWeatherDao = weatherDB.futureWeatherDAO()
    private val weatherLocationDao: WeatherLocationDao = weatherDB.weatherLocationDAO()

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
//            val count = futureWeatherDao.countFutureWeather(startDate)
            return@withContext if(metric)
                futureWeatherDao.getWeatherForecastMetric(startDate)
            else
                futureWeatherDao.getWeatherForecastImperial(startDate)
        }
    }

    override suspend fun getFutureWeatherByDate(
        date: LocalDate,
        metric: Boolean
    ): LiveData<out UnitDetailFutureWeatherEntry> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext if(metric)
                futureWeatherDao.getDetailedWeatherByDateMetric(date)
            else
                futureWeatherDao.getDetailedWeatherByDateImperial(date)
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
            weatherLocationDao.upsert(fetchedWeather.location) // todo uncomment this and mocked response for release version
        }
    }

    private fun mapEntity(index: Int, it: FutureWeatherInfo): FutureWeatherEntry {
        return FutureWeatherEntry(
//            astro = it.astro,
            avgtempC = it.avgtemp,
            avgtempF = it.avgtemp.convertMetricToImperial(UnitType.TEMPERATURE),
            date = LocalDate.now().plusDays((index + 1).toLong()).toString(), // todo dummy date set
            maxtempC= it.maxtemp,
            maxtempF= it.maxtemp.convertMetricToImperial(UnitType.TEMPERATURE),
            mintempC= it.mintemp,
            mintempF= it.mintemp.convertMetricToImperial(UnitType.TEMPERATURE),
            sunhour= it.sunhour,
            totalsnow= it.totalsnow,
            uvIndex= it.uvIndex,
            conditionIconUrl = it.hourly[0].weatherIcons[0],
            conditionText = it.hourly[0].weatherDescriptions.joinToString(", "),
            avgvisMilesKm = it.hourly[0].visibility,
            avgvisMilesMi = it.hourly[0].visibility.convertMetricToImperial(UnitType.VISIBILITY),
            maxWindSpeedKmph = it.hourly[0].windSpeed,
            maxWindSpeedMps = it.hourly[0].windSpeed.convertMetricToImperial(UnitType.WIND_SPEED),
            precipMm = it.hourly[0].precip,
            precipIn = it.hourly[0].precip.convertMetricToImperial(UnitType.PRECIPITATION)
        )
    }

    private fun deleteOldForecastData() {
        val today = LocalDate.now()
        futureWeatherDao.deleteOldEntries(today)
    }
}