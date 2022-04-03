package com.example.forecastify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.forecastify.data.db.entity.CurrentWeatherEntry
import com.example.forecastify.data.db.entity.FutureWeatherEntry
import com.example.forecastify.data.db.entity.WeatherLocationEntry
import com.example.forecastify.utils.LocalDateConverter
import com.example.forecastify.utils.RoomListStringConverter

@Database(
    entities = [CurrentWeatherEntry::class, FutureWeatherEntry::class, WeatherLocationEntry::class],
    version = 1
)
@TypeConverters(RoomListStringConverter::class, LocalDateConverter::class)
abstract class WeatherDB: RoomDatabase() {

    abstract fun currentWeatherDAO(): CurrentWeatherDao
    abstract fun futureWeatherDAO(): FutureWeatherDao
    abstract fun weatherLocationDAO(): WeatherLocationDao

}