package app.mak.atmosense.core.data.testing

import app.mak.atmosense.core.database.dao.CurrentWeatherEntity
import app.mak.atmosense.core.database.dao.GetCitiesWeather
import app.mak.atmosense.core.database.dao.ObserveWeatherByCity
import app.mak.atmosense.core.database.dao.api.CurrentWeatherDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeCurrentWeatherDAO(private val cityDAO: FakeCityDAO) : CurrentWeatherDAO {
  val currentWeather = MutableStateFlow<Map<Long, CurrentWeatherEntity>>(emptyMap())

  override fun insert(currentWeather: CurrentWeatherEntity) {
    this.currentWeather.value += (currentWeather.city_id to currentWeather)
  }

  override fun getByCityId(cityId: Long): CurrentWeatherEntity? = currentWeather.value[cityId]

  override fun deleteByCityId(cityId: Long) {
    this.currentWeather.value -= cityId
  }

  override fun deleteAll() {
    this.currentWeather.value = emptyMap()
  }

  override fun observeCitiesWeather(): Flow<List<GetCitiesWeather>> {
    return currentWeather.map { weatherMap ->
      weatherMap.values.mapNotNull { weather ->
        val city = cityDAO.getById(weather.city_id) ?: return@mapNotNull null
        GetCitiesWeather(
          country_code = city.country_code,
          name = city.name,
          latitude = city.latitude,
          longitude = city.longitude,
          city_id = weather.city_id,
          temperature = weather.temperature,
          feels_like = weather.feels_like,
          humidity = weather.humidity,
          pressure = weather.pressure,
          wind_speed = weather.wind_speed,
          wind_degrees = weather.wind_degrees,
          condition_id = weather.condition_id,
          condition_main = weather.condition_main,
          condition_description = weather.condition_description,
          condition_icon_code = weather.condition_icon_code,
          fetched_at = weather.fetched_at
        )
      }
    }
  }

  override fun observeCityCurrentWeather(cityId: Long): Flow<ObserveWeatherByCity?> {
    return currentWeather.map { weatherMap ->
      val weather = weatherMap[cityId] ?: return@map null
      val city = cityDAO.getById(cityId) ?: return@map null
      ObserveWeatherByCity(
        country_code = city.country_code,
        name = city.name,
        latitude = city.latitude,
        longitude = city.longitude,
        city_id = weather.city_id,
        temperature = weather.temperature,
        feels_like = weather.feels_like,
        humidity = weather.humidity,
        pressure = weather.pressure,
        wind_speed = weather.wind_speed,
        wind_degrees = weather.wind_degrees,
        condition_id = weather.condition_id,
        condition_main = weather.condition_main,
        condition_description = weather.condition_description,
        condition_icon_code = weather.condition_icon_code,
        fetched_at = weather.fetched_at
      )
    }
  }
}
