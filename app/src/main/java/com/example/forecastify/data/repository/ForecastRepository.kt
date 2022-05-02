package com.example.forecastify.data.repository

import com.example.forecastify.data.db.entity.WeatherLocationEntry
import com.example.forecastify.data.db.unitlocalised.current.UnitSpecificCurrentEntry
import com.example.forecastify.data.db.unitlocalised.future.detail.UnitDetailFutureWeatherEntry
import com.example.forecastify.data.db.unitlocalised.future.list.UnitSpecificFutureEntry
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate

interface ForecastRepository {

    fun getCurrentWeather(metric: Boolean): Flow<UnitSpecificCurrentEntry>

    fun getFutureWeatherList(startDate: LocalDate, metric: Boolean): Flow<List<UnitSpecificFutureEntry>>

    fun getFutureWeatherByDate(date: LocalDate, metric: Boolean): Flow<UnitDetailFutureWeatherEntry>

    fun getWeatherLocation(): Flow<WeatherLocationEntry>
}