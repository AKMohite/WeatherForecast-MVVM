package app.mak.atmosense.core.database.dao.api

import app.mak.atmosense.core.database.dao.CurrentWeatherEntity

interface CurrentWeatherDAO {
  fun insert(currentWeather: CurrentWeatherEntity)
  fun getByCityId(cityId: Long): CurrentWeatherEntity?
  fun deleteByCityId(cityId: Long)
  fun deleteAll()
}
