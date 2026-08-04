package app.mak.atmosense.core.common.model

data class WeatherDetails(
  val currentWeather: CityWeather,
  val forecastWeather: List<ForecastSlot>
) {
  val lastSyncedAt: String = currentWeather.fetchedBefore
  val cityName: String = listOf(currentWeather.cityName, currentWeather.countryCode)
    .joinToString(", ")
}
