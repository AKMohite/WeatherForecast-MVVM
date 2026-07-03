package app.mak.nimbus.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import app.mak.nimbus.core.data.local.entity.CurrentWeatherEntity
import app.mak.nimbus.core.data.local.entity.DailyForecastEntity
import app.mak.nimbus.core.data.local.entity.HourlyForecastEntity
import app.mak.nimbus.core.data.local.entity.WeatherAlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM current_weather WHERE cityId = :cityId")
    fun getCurrentWeather(cityId: String): Flow<CurrentWeatherEntity?>

    @Query("SELECT * FROM hourly_forecast WHERE cityId = :cityId ORDER BY dt ASC")
    fun getHourlyForecast(cityId: String): Flow<List<HourlyForecastEntity>>

    @Query("SELECT * FROM daily_forecast WHERE cityId = :cityId ORDER BY dt ASC")
    fun getDailyForecast(cityId: String): Flow<List<DailyForecastEntity>>

    @Query("SELECT * FROM weather_alerts WHERE cityId = :cityId ORDER BY start ASC")
    fun getWeatherAlerts(cityId: String): Flow<List<WeatherAlertEntity>>

    @Upsert
    suspend fun upsertCurrentWeather(weather: CurrentWeatherEntity)

    @Upsert
    suspend fun upsertHourlyForecast(forecast: List<HourlyForecastEntity>)

    @Upsert
    suspend fun upsertDailyForecast(forecast: List<DailyForecastEntity>)

    @Upsert
    suspend fun upsertWeatherAlerts(alerts: List<WeatherAlertEntity>)

    @Transaction
    suspend fun deleteAndInsertWeather(
        cityId: String,
        current: CurrentWeatherEntity,
        hourly: List<HourlyForecastEntity>,
        daily: List<DailyForecastEntity>,
        alerts: List<WeatherAlertEntity>
    ) {
        deleteWeatherForCity(cityId)
        upsertCurrentWeather(current)
        upsertHourlyForecast(hourly)
        upsertDailyForecast(daily)
        upsertWeatherAlerts(alerts)
    }

    @Query("DELETE FROM current_weather WHERE cityId = :cityId")
    suspend fun deleteCurrentWeather(cityId: String)

    @Query("DELETE FROM hourly_forecast WHERE cityId = :cityId")
    suspend fun deleteHourlyForecast(cityId: String)

    @Query("DELETE FROM daily_forecast WHERE cityId = :cityId")
    suspend fun deleteDailyForecast(cityId: String)

    @Query("DELETE FROM weather_alerts WHERE cityId = :cityId")
    suspend fun deleteWeatherAlerts(cityId: String)

    @Transaction
    suspend fun deleteWeatherForCity(cityId: String) {
        deleteCurrentWeather(cityId)
        deleteHourlyForecast(cityId)
        deleteDailyForecast(cityId)
        deleteWeatherAlerts(cityId)
    }
}