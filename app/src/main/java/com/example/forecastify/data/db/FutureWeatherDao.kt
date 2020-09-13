package com.example.forecastify.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.forecastify.data.db.entity.CURRENT_WEATHER_ID
import com.example.forecastify.data.db.entity.CurrentWeatherEntry
import com.example.forecastify.data.db.entity.FutureWeatherEntry
import com.example.forecastify.data.db.unitlocalised.current.ImperialCurrentWeatherEntry
import com.example.forecastify.data.db.unitlocalised.current.MetricCurrentWeatherEntry
import com.example.forecastify.data.db.unitlocalised.future.ImperialFutureWeatherEntry
import com.example.forecastify.data.db.unitlocalised.future.MetricFutureWeatherEntry
import org.threeten.bp.LocalDate

@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherEntry: FutureWeatherEntry)

    @Query("SELECT * FROM future_weather WHERE date(date) >= date(:startDate)")
    fun getWeatherForecastMetric(startDate: LocalDate): LiveData<List<MetricFutureWeatherEntry>>

    @Query("SELECT * FROM future_weather WHERE date(date) >= date(:startDate)")
    fun getWeatherForecastImperial(startDate: LocalDate): LiveData<List<ImperialFutureWeatherEntry>>

    @Query("select count(id) from future_weather where date(date) >= date(:startDate)")
    fun countFutureWeather(startDate: LocalDate): Int

    @Query("delete from future_weather where date(date) < date(:firstDateToKeep)")
    fun deleteOldEntries(firstDateToKeep: LocalDate)
}