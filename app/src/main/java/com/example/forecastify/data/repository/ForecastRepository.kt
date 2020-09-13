package com.example.forecastify.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastify.data.db.entity.WeatherLocationEntry
import com.example.forecastify.data.db.unitlocalised.current.UnitSpecificCurrentEntry
import com.example.forecastify.data.db.unitlocalised.future.detail.UnitDetailFutureWeatherEntry
import com.example.forecastify.data.db.unitlocalised.future.list.UnitSpecificFutureEntry
import org.threeten.bp.LocalDate

interface ForecastRepository {

    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificCurrentEntry>

    suspend fun getFutureWeatherList(startDate: LocalDate, metric: Boolean): LiveData<out List<UnitSpecificFutureEntry>>

    suspend fun getFutureWeatherByDate(date: LocalDate, metric: Boolean): LiveData<out UnitDetailFutureWeatherEntry>

    suspend fun getWeatherLocation(): LiveData<WeatherLocationEntry>
}