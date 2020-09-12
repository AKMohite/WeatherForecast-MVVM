package com.example.forecastify.data.db.unitlocalised

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.example.forecastify.utils.RoomListStringConverter

class ImperialCurrentWeatherEntry(
    @ColumnInfo(name="temperature")
    override val temperature: Double,
    @ColumnInfo(name="weatherDescriptions")
    @TypeConverters(RoomListStringConverter::class)
    override val weatherDescriptions: List<String>,
    @ColumnInfo(name="weatherIcons")
    @TypeConverters(RoomListStringConverter::class)
    override val weatherIcons: List<String>,
    @ColumnInfo(name="windDir")
    override val windDir: String,
    @ColumnInfo(name="windSpeed")
    override val windSpeed: Double,
    @ColumnInfo(name="precip")
    override val precip: Double,
    @ColumnInfo(name="feelslike")
    override val feelslike: Double,
    @ColumnInfo(name="visibility")
    override val visibility: Double
) : UnitSpecificEntry{
}