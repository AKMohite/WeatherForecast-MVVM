package app.mak.atmosense.core.data.mapper

import app.mak.atmosense.core.common.model.CityWeather
import app.mak.atmosense.core.common.model.ForecastSlot
import app.mak.atmosense.core.common.model.SearchCity
import app.mak.atmosense.core.common.model.WeatherCondition
import app.mak.atmosense.core.database.dao.CurrentWeatherEntity
import app.mak.atmosense.core.database.dao.ForecastWeatherEntity
import app.mak.atmosense.core.database.dao.GetCitiesWeather
import app.mak.atmosense.core.database.dao.ObserveWeatherByCity
import app.mak.atmosense.core.network.dto.CurrentWeatherDTO
import app.mak.atmosense.core.network.dto.HourlyDTO
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

internal fun CurrentWeatherEntity?.toWeatherCity(): CityWeather? {
  if (this == null) return null
  val primaryCondition = WeatherCondition(
    id = condition_id, main = condition_main,
    description = condition_description, iconCode = condition_icon_code.weatherImage(),
  )
  return CityWeather(
    cityId = city_id,
    cityName = "",
    countryCode = "",
    temperature = temperature,
    feelsLike = feels_like,
    humidity = humidity,
    pressure = pressure.toDouble(),
    windSpeed = wind_speed,
    windDegrees = wind_degrees,
    weatherIcon = condition_icon_code.weatherImage(),
    weatherDescription = condition_description,
    fetchedBefore = fetched_at.toString()
  )
}

internal fun List<GetCitiesWeather>.toWeatherCities(): List<CityWeather> {
  return map { weather ->
    weather.toWeatherCity()
  }
}

internal fun GetCitiesWeather.toWeatherCity(): CityWeather = CityWeather(
  cityId = city_id,
  cityName = name,
  countryCode = country_code,
  temperature = temperature,
  feelsLike = feels_like,
  humidity = humidity,
  pressure = pressure.toDouble(),
  windSpeed = wind_speed,
  windDegrees = wind_degrees,
  weatherIcon = condition_icon_code.weatherImage(),
  weatherDescription = condition_description,
  fetchedBefore = fetched_at.toString()
)

internal fun ObserveWeatherByCity?.toObserveCity(): CityWeather? {
  if (this == null) return null
  return CityWeather(
    cityId = city_id,
    cityName = name,
    countryCode = country_code,
    temperature = temperature,
    feelsLike = feels_like,
    humidity = humidity,
    pressure = pressure.toDouble(),
    windSpeed = wind_speed,
    windDegrees = wind_degrees,
    weatherIcon = condition_icon_code.weatherImage(),
    weatherDescription = condition_description,
    fetchedBefore = fetched_at.toString()
  )
}


internal fun List<HourlyDTO>.toForecast(cityId: Long): List<ForecastSlot> {
  return mapNotNull {
    it.domain(cityId)
  }
}

private fun HourlyDTO.domain(cityId: Long): ForecastSlot {
  val primaryCondition = weather?.firstOrNull()?.let { dTO ->
    WeatherCondition(
      id = dTO.id ?: 0, main = dTO.main.orEmpty(),
      description = dTO.description.orEmpty(), iconCode = dTO.iconCode.orEmpty(),
    )
  }
  return ForecastSlot(
    cityId = cityId,
    timestamp = Instant.fromEpochSeconds(dt ?: 0),
    temperature = main?.temperature ?: 0.0,
    condition = primaryCondition,
    precipitationProbability = pop ?: 0.0,
    windSpeed = wind?.speed ?: 0.0,
  )
}

internal fun ForecastSlot.toForecastEntity(now: Instant): ForecastWeatherEntity {
  return ForecastWeatherEntity(
    id = 0L,
    city_id = cityId,
    forecast_timestamp = timestamp,
    temperature = temperature,
    condition_id = condition?.id ?: 0,
    condition_main = condition?.main.orEmpty(),
    condition_description = condition?.description.orEmpty(),
    icon_code = condition?.iconCode.orEmpty(),
    precipitation_probability = precipitationProbability,
    wind_speed = windSpeed,
    fetched_at = now
  )
}

internal fun List<ForecastWeatherEntity>.toForecast(): List<ForecastSlot> {
  return map { forecast ->
    ForecastSlot(
      cityId = forecast.city_id,
      timestamp = forecast.forecast_timestamp,
      temperature = forecast.temperature,
      condition = WeatherCondition(
        id = forecast.condition_id,
        main = forecast.condition_main,
        description = forecast.condition_description,
        iconCode = forecast.icon_code
      ),
      precipitationProbability = forecast.precipitation_probability,
      windSpeed = forecast.wind_speed
    )
  }
}


