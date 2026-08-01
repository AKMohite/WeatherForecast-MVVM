package app.mak.atmosense.core.database.dao.api

import app.mak.atmosense.core.database.dao.CurrentWeatherEntity

interface CurrentWeatherDAO {
  suspend fun insert(currentWeather: CurrentWeatherEntity)
  suspend fun getByCityId(cityId: Long): CurrentWeatherEntity?
  suspend fun deleteByCityId(cityId: Long)
  suspend fun deleteAll()
}
