package com.example.forecastify.data.db.unitlocalised.current

interface UnitSpecificCurrentEntry {
    val temperature: Double
    val weatherDescriptions: List<String>
    val weatherIcons: List<String?>?
    val windDir: String
    val windSpeed: Double
    val precip: Double
    val feelslike: Double
    val visibility: Double
}