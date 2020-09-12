package com.example.forecastify.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.forecastify.data.repository.ForecastRepository
import com.example.forecastify.internal.UnitSystem
import com.example.forecastify.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val unitSystem = UnitSystem.METRIC // todo change from settings later
    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }
}
