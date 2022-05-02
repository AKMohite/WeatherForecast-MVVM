package com.example.forecastify.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecastify.data.db.entity.CURRENT_WEATHER_ID
import com.example.forecastify.data.db.entity.CurrentWeatherEntry
import com.example.forecastify.data.db.unitlocalised.current.ImperialCurrentWeatherEntry
import com.example.forecastify.data.db.unitlocalised.current.MetricCurrentWeatherEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query("SELECT * FROM current_weather WHERE id = $CURRENT_WEATHER_ID")
    fun getWeatherMetric(): Flow<MetricCurrentWeatherEntry>

    @Query("SELECT * FROM current_weather WHERE id = $CURRENT_WEATHER_ID")
    fun getWeatherImperial(): Flow<ImperialCurrentWeatherEntry>
}