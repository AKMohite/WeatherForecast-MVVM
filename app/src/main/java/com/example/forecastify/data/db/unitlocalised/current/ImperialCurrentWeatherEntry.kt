package com.example.forecastify.data.db.unitlocalised.current

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.example.forecastify.utils.RoomListStringConverter

class ImperialCurrentWeatherEntry(
    @ColumnInfo(name="temperatureF")
    override val temperature: Double,
    @ColumnInfo(name="weatherDescriptions")
    @TypeConverters(RoomListStringConverter::class)
    override val weatherDescriptions: List<String>,
    @ColumnInfo(name="weatherIcons")
    @TypeConverters(RoomListStringConverter::class)
    override val weatherIcons: List<String>,
    @ColumnInfo(name="windDir")
    override val windDir: String,
    @ColumnInfo(name="windSpeedMps")
    override val windSpeed: Double,
    @ColumnInfo(name="precipIn")
    override val precip: Double,
    @ColumnInfo(name="feelslikeF")
    override val feelslike: Double,
    @ColumnInfo(name="visibilityMi")
    override val visibility: Double
) : UnitSpecificCurrentEntry {
}