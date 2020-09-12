package com.example.forecastify.data.repository

import androidx.lifecycle.LiveData
import com.example.forecastify.data.db.unitlocalised.UnitSpecificEntry

interface ForecastRepository {

    suspend fun getCurrentWeather(metric: Boolean): LiveData<out UnitSpecificEntry>
}