package com.example.forecastify.ui.weather.future.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.forecastify.data.db.unitlocalised.future.detail.UnitDetailFutureWeatherEntry
import com.example.forecastify.data.provider.UnitProvider
import com.example.forecastify.data.repository.ForecastRepository
import com.example.forecastify.internal.lazyDeferred
import com.example.forecastify.ui.base.WeatherViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class FutureDetailWeatherViewModel @Inject constructor(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather: MutableLiveData<UnitDetailFutureWeatherEntry> by lazy { MutableLiveData() }

    fun getDetail(date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            weather.value = forecastRepository.getFutureWeatherByDate(date, super.isMetricUnit).value
        }
    }
}