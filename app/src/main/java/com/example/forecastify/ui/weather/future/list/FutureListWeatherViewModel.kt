package com.example.forecastify.ui.weather.future.list

import com.example.forecastify.data.provider.UnitProvider
import com.example.forecastify.data.repository.ForecastRepository
import com.example.forecastify.internal.lazyDeferred
import com.example.forecastify.ui.base.WeatherViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class FutureListWeatherViewModel @Inject constructor(
    private val forecastRepository: ForecastRepository,
    private val unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weatherEntries by lazyDeferred {
        forecastRepository.getFutureWeatherList(LocalDate.now(), super.isMetricUnit)
    }
}
