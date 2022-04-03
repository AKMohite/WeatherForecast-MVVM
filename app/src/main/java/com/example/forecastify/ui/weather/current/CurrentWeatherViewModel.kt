package com.example.forecastify.ui.weather.current

import com.example.forecastify.data.provider.UnitProvider
import com.example.forecastify.data.repository.ForecastRepository
import com.example.forecastify.internal.lazyDeferred
import com.example.forecastify.ui.base.WeatherViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(super.isMetricUnit)
    }

}
