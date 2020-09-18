package com.example.forecastify.data.db.unitlocalised.current

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.example.forecastify.utils.RoomListStringConverter

class MetricCurrentWeatherEntry(
    @ColumnInfo(name="temperatureC")
    override val temperature: Double,
    @ColumnInfo(name="weatherDescriptions")
    @TypeConverters(RoomListStringConverter::class)
    override val weatherDescriptions: List<String>,
    @ColumnInfo(name="weatherIcons")
    @TypeConverters(RoomListStringConverter::class)
    override val weatherIcons: List<String>,
    @ColumnInfo(name="windDir")
    override val windDir: String,
    @ColumnInfo(name="windSpeedKmph")
    override val windSpeed: Double,
    @ColumnInfo(name="precipMm")
    override val precip: Double,
    @ColumnInfo(name="feelslikeC")
    override val feelslike: Double,
    @ColumnInfo(name="visibilityKm")
    override val visibility: Double
): UnitSpecificCurrentEntry {
}