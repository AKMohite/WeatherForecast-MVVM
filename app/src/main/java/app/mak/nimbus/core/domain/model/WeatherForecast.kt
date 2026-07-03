package app.mak.nimbus.core.domain.model

data class WeatherForecast(
    val cityId: String,
    val current: CurrentWeather,
    val hourly: List<HourlyForecast> = emptyList(),
    val daily: List<DailyForecast> = emptyList(),
    val alerts: List<WeatherAlert> = emptyList(),
)

data class CurrentWeather(
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

data class HourlyForecast(
    val dt: Long,
    val tempC: Double,
    val conditionCode: Int,
    val icon: String,
    val pop: Double,
)

data class DailyForecast(
    val dt: Long,
    val tempMinC: Double,
    val tempMaxC: Double,
    val conditionCode: Int,
    val icon: String,
    val pop: Double,
)

data class WeatherAlert(
    val event: String,
    val description: String,
    val severity: String,
    val start: Long,
    val end: Long,
)

