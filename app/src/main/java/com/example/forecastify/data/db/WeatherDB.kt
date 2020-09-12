package com.example.forecastify.data.db

import android.content.Context
import androidx.room.*
import com.example.forecastify.data.db.entity.CurrentWeatherEntry
import com.example.forecastify.utils.RoomListStringConverter

@Database(
    entities = [CurrentWeatherEntry::class],
    version = 1
)
@TypeConverters(RoomListStringConverter::class)
abstract class WeatherDB: RoomDatabase() {

    abstract fun currentWeatherDAO(): CurrentWeatherDao

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