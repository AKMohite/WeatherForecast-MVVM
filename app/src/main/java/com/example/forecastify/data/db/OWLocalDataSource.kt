package com.example.forecastify.data.db

import com.example.forecastify.data.db.entity.CurrentWeatherEntry
import com.example.forecastify.data.db.entity.FutureWeatherEntry
import com.example.forecastify.data.db.entity.WeatherLocationEntry
import com.example.forecastify.data.db.unitlocalised.current.ImperialCurrentWeatherEntry
import com.example.forecastify.data.db.unitlocalised.current.MetricCurrentWeatherEntry
import com.example.forecastify.data.db.unitlocalised.future.detail.ImperialDetailFutureWeatherEntry
import com.example.forecastify.data.db.unitlocalised.future.detail.MetricDetailFutureWeatherEntry
import com.example.forecastify.data.db.unitlocalised.future.list.ImperialFutureWeatherEntry
import com.example.forecastify.data.db.unitlocalised.future.list.MetricFutureWeatherEntry
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

class OWLocalDataSource(
    private val db: WeatherDB
): IOWLocalDataSource {

    private val currentDao: CurrentWeatherDao = db.currentWeatherDAO()
    private val futureDao: FutureWeatherDao = db.futureWeatherDAO()
    private val locationDao: WeatherLocationDao = db.weatherLocationDAO()

    override fun upsertWeatherLocation(weatherLocation: WeatherLocationEntry) {
        return locationDao.upsert(weatherLocation)
    }

    override fun getLocation(): Flow<WeatherLocationEntry> {
        return locationDao.getLocation()
    }

    override fun getLocationNonLive(): WeatherLocationEntry? {
        return locationDao.getLocationNonLive()
    }

    override fun insertForecast(weatherEntry: FutureWeatherEntry) {
        return futureDao.insert(weatherEntry)
    }

    override fun getWeatherForecastMetric(startDate: LocalDate): Flow<List<MetricFutureWeatherEntry>> {
        return futureDao.getWeatherForecastMetric(startDate)
    }

    override fun getWeatherForecastImperial(startDate: LocalDate): Flow<List<ImperialFutureWeatherEntry>> {
        return futureDao.getWeatherForecastImperial(startDate)
    }

    override fun getMetricDetailFutureWeather(date: LocalDate): Flow<MetricDetailFutureWeatherEntry> {
        return futureDao.getDetailedWeatherByDateMetric(date)
    }

    override fun getImperialDetailFutureWeather(date: LocalDate): Flow<ImperialDetailFutureWeatherEntry> {
        return futureDao.getDetailedWeatherByDateImperial(date)
    }

    override fun countFutureWeather(startDate: LocalDate): Int {
        return futureDao.countFutureWeather(startDate)
    }

    override fun deleteOldForecastEntries(firstDateToKeep: LocalDate) {
        return futureDao.deleteOldEntries(firstDateToKeep)
    }

    override fun upsertCurrentWeather(weather: CurrentWeatherEntry) {
        return currentDao.upsert(weather)
    }

    override fun getMetricCurrentWeather(): Flow<MetricCurrentWeatherEntry> {
        return currentDao.getWeatherMetric()
    }

    override fun getImperialCurrentWeather(): Flow<ImperialCurrentWeatherEntry> {
        return currentDao.getWeatherImperial()
    }
}

interface IOWLocalDataSource {

    fun upsertWeatherLocation(weatherLocation: WeatherLocationEntry)
    fun getLocation(): Flow<WeatherLocationEntry>
    fun getLocationNonLive(): WeatherLocationEntry?
    fun insertForecast(weatherEntry: FutureWeatherEntry)
    fun getWeatherForecastMetric(startDate: LocalDate): Flow<List<MetricFutureWeatherEntry>>
    fun getWeatherForecastImperial(startDate: LocalDate): Flow<List<ImperialFutureWeatherEntry>>
    fun getMetricDetailFutureWeather(date: LocalDate): Flow<MetricDetailFutureWeatherEntry>
    fun getImperialDetailFutureWeather(date: LocalDate): Flow<ImperialDetailFutureWeatherEntry>
    fun countFutureWeather(startDate: LocalDate): Int
    fun deleteOldForecastEntries(firstDateToKeep: LocalDate)
    fun upsertCurrentWeather(weather: CurrentWeatherEntry)
    fun getMetricCurrentWeather(): Flow<MetricCurrentWeatherEntry>
    fun getImperialCurrentWeather(): Flow<ImperialCurrentWeatherEntry>
}