package com.example.forecastify.data.db

import android.content.Context
import androidx.room.*
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

    companion object{
        @Volatile private var instance: WeatherDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, WeatherDB::class.java, "forecast.db")
            .build()
    }
}