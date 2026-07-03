package app.mak.nimbus.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeatherEntity(
    @PrimaryKey val cityId: String,
    val tempC: Double,
    val feelsLikeC: Double,
    val conditionCode: Int,
    val conditionText: String,
    val icon: String,
    val humidity: Int,
    val windSpeedKph: Double,
    val pressure: Int,
    val uvIndex: Double,
    val isDay: Boolean,
    val lastUpdated: Long,
)

@Entity(tableName = "hourly_forecast", primaryKeys = ["cityId", "dt"])
data class HourlyForecastEntity(
    val cityId: String,
    val dt: Long,
    val tempC: Double,
    val conditionCode: Int,
    val icon: String,
    val pop: Double,
    val lastUpdated: Long,
)

@Entity(tableName = "daily_forecast", primaryKeys = ["cityId", "dt"])
data class DailyForecastEntity(
    val cityId: String,
    val dt: Long,
    val tempMinC: Double,
    val tempMaxC: Double,
    val conditionCode: Int,
    val icon: String,
    val pop: Double,
    val lastUpdated: Long,
)

@Entity(tableName = "weather_alerts", primaryKeys = ["cityId", "start"])
data class WeatherAlertEntity(
    val cityId: String,
    val event: String,
    val description: String,
    val severity: String,
    val start: Long,
    val end: Long,
)