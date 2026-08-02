package app.mak.atmosense.core.data.mapper

import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.SearchCity
import app.mak.atmosense.core.database.dao.CurrentWeatherEntity
import app.mak.atmosense.core.database.dao.GetCitiesWeather
import app.mak.atmosense.core.network.dto.CurrentWeatherDTO
import app.mak.atmosense.core.network.dto.LocationDTO
import kotlin.time.Instant


internal fun List<LocationDTO>.toSearchCities(): List<SearchCity> {
  return map { location ->
    SearchCity(
      country = location.country.orEmpty(),
      latitude = location.lat ?: 0.0,
      longitude = location.lon ?: 0.0,
      name = location.name.orEmpty(),
      state = location.state.orEmpty()
    )
  }
}

internal fun CurrentWeatherDTO.toCurrentWeatherEntity(now: Instant): CurrentWeatherEntity {
  val primaryCondition = weather?.firstOrNull() ?: error("No primary condition")
  return CurrentWeatherEntity(
    city_id = cityId,
    temperature = main?.temperature ?: 0.0,
    feels_like = main?.feelsLike ?: 0.0,
    humidity = main?.humidity ?: 0,
    pressure = main?.pressure ?: 0,
    wind_speed = wind?.speed ?: 0.0,
    wind_degrees = wind?.degrees ?: 0,
    condition_id = primaryCondition.id ?: 0,
    condition_main = primaryCondition.main.orEmpty(),
    condition_description = primaryCondition.description.orEmpty(),
    condition_icon_code = primaryCondition.iconCode.orEmpty(),
    fetched_at = now,
  )
}

internal fun String?.weatherImage() = "https://openweathermap.org/img/wn/$this@2x.png"

internal fun List<GetCitiesWeather>.toCityWeather(): List<CityWeather> {
  return map { weather ->
    CityWeather(
      cityId = weather.city_id,
      cityName = weather.name,
      countryCode = weather.country_code,
      temperature = weather.temperature,
      feelsLike = weather.feels_like,
      weatherIcon = weather.condition_icon_code.weatherImage(),
      weatherDescription = weather.condition_description,
      fetchedBefore = weather.fetched_at.toString()
    )
  }
}
