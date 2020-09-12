package com.example.forecastify.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.forecastify.data.provider.UnitProvider
import com.example.forecastify.data.repository.ForecastRepository
import com.example.forecastify.internal.UnitSystem
import com.example.forecastify.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem()
    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
}
