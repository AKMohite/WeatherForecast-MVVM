package app.mak.atmosense.core.common.model

data class CityWeather(
  val cityId: Long,
  val cityName: String,
  val countryCode: String,
  val temperature: Double,
  val feelsLike: Double,
  val humidity: Long,
  val pressure: Double,
  val windSpeed: Double,
  val windDegrees: Long,
  val weatherIcon: String,
  val weatherDescription: String,
  val fetchedBefore: String
)
