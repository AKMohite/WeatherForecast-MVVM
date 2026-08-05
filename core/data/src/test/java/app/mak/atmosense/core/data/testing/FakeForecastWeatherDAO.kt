package app.mak.atmosense.core.data.testing

import app.mak.atmosense.core.database.dao.ForecastWeatherEntity
import app.mak.atmosense.core.database.dao.api.ForecastWeatherDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeForecastWeatherDAO : ForecastWeatherDAO {
  private val forecastEntities = MutableStateFlow<List<ForecastWeatherEntity>>(emptyList())

  override fun insert(forecastWeather: ForecastWeatherEntity) {
    forecastEntities.value += forecastWeather
  }

  override fun observeForecastByCityId(cityId: Long): Flow<List<ForecastWeatherEntity>> {
    return forecastEntities.map { list -> list.filter { it.city_id == cityId } }
  }

  override fun deleteByCityId(cityId: Long) {
    forecastEntities.value = forecastEntities.value.filter { it.city_id != cityId }
  }

  override fun deleteAll() {
    forecastEntities.value = emptyList()
  }

  override fun insertAll(forecastEntities: List<ForecastWeatherEntity>) {
    this.forecastEntities.value += forecastEntities
  }
}
