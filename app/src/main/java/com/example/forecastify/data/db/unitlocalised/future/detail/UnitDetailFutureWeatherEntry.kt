package com.example.forecastify.data.db.unitlocalised.future.detail

import org.threeten.bp.LocalDate

interface UnitDetailFutureWeatherEntry {
    val date: LocalDate
    val maxtemp: Double
    val mintemp: Double
    val avgtemp: Double
    val conditionText: String
    val conditionIconUrl: String
    val maxWindSpeed: Double
    val precip: Double
    val avgVisibilityDistance: Double
    val uvIndex: Double
}