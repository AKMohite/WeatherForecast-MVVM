package com.example.forecastify.data.repository

import com.example.forecastify.data.db.OWLocalDataSource
import com.example.forecastify.data.db.entity.FutureWeatherEntry
import com.example.forecastify.data.db.entity.WeatherLocationEntry
import com.example.forecastify.data.db.unitlocalised.current.UnitSpecificCurrentEntry
import com.example.forecastify.data.db.unitlocalised.future.detail.UnitDetailFutureWeatherEntry
import com.example.forecastify.data.db.unitlocalised.future.list.UnitSpecificFutureEntry
import com.example.forecastify.data.network.FORECAST_DAYS_COUNT
import com.example.forecastify.data.network.IOWNetworkDataSource
import com.example.forecastify.data.network.response.CurrentWeatherDTO
import com.example.forecastify.data.network.response.FutureWeatherInfo
import com.example.forecastify.data.network.response.WeatherForecastDTO
import com.example.forecastify.data.provider.LocationProvider
import com.example.forecastify.domain.WeatherConstants.OW_FUTURE_DAY_COUNT
import com.example.forecastify.domain.WeatherConstants.OW_METRIC_TYPE
import com.example.forecastify.internal.UnitType
import com.example.forecastify.internal.convertMetricToImperial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ForecastRepositoryImpl @Inject constructor(
    private val networkDataSource: IOWNetworkDataSource,
    private val localDataSource: OWLocalDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    override fun getCurrentWeather(metric: Boolean): Flow<UnitSpecificCurrentEntry> {
        return flow {
            initWeatherData()
            if(metric)
                localDataSource.getMetricCurrentWeather()
            else
                localDataSource.getImperialCurrentWeather()
        }
    }

    override fun getWeatherLocation(): Flow<WeatherLocationEntry> {
        return flow {
            localDataSource.getLocation()
        }
    }

    override fun getFutureWeatherList(
        startDate: LocalDate,
        metric: Boolean
    ): Flow<List<UnitSpecificFutureEntry>> {
        return flow {
            initWeatherData()
//            val count = futureWeatherDao.countFutureWeather(startDate)
            if(metric)
                localDataSource.getWeatherForecastMetric(startDate)
            else
                localDataSource.getWeatherForecastImperial(startDate)
        }
    }

    override fun getFutureWeatherByDate(
        date: LocalDate,
        metric: Boolean
    ): Flow<UnitDetailFutureWeatherEntry> {
        return flow {
            initWeatherData()
            if(metric)
                localDataSource.getMetricDetailFutureWeather(date)
            else
                localDataSource.getImperialDetailFutureWeather(date)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = localDataSource.getLocationNonLive()

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
        val latLong = locationProvider.getPreferredLocationString()
        networkDataSource.getCurrentWeather(
            latLong.first,
            latLong.second,
            Locale.getDefault().language,
            OW_METRIC_TYPE
        )
    }
    
    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val hourAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(hourAgo)
    }

    private fun isFetchFutureNeeded(): Boolean {
        val today = LocalDate.now()
        val futureWeatherCount = localDataSource.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT
    }

    private suspend fun fetchFutureWeather() {
        val latLong = locationProvider.getPreferredLocationString()
        networkDataSource.getFutureWeather(
            latLong.first,
            latLong.second,
            OW_FUTURE_DAY_COUNT,
            Locale.getDefault().language,
            OW_METRIC_TYPE
        )
    }

    /*private suspend fun persistCurrentWeather(fetchedWeather: CurrentWeatherDTO){
        localDataSource.upsertCurrentWeather(fetchedWeather.currentWeather)
        localDataSource.upsertWeatherLocation(fetchedWeather.location)
    }

    private suspend fun persistFutureWeather(fetchedWeather: WeatherForecastDTO) {
        deleteOldForecastData()
        val futureWeatherList = ArrayList(fetchedWeather.forecastContainer.values)
        futureWeatherList.forEachIndexed { index, futureWeatherInfo ->
            localDataSource.insertForecast(mapEntity(index, futureWeatherInfo))
        }
        localDataSource.upsertWeatherLocation(fetchedWeather.location)
    }*/

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
        localDataSource.deleteOldForecastEntries(today)
    }
}