package app.mak.atmosense.core.database.dao.api

import app.mak.atmosense.core.database.dao.CurrentWeatherEntity
import app.mak.atmosense.core.database.dao.GetCitiesWeather
import kotlinx.coroutines.flow.Flow

interface CurrentWeatherDAO {
  fun insert(currentWeather: CurrentWeatherEntity)
  fun getByCityId(cityId: Long): CurrentWeatherEntity?
  fun deleteByCityId(cityId: Long)
  fun deleteAll()
  fun observeCitiesWeather(): Flow<List<GetCitiesWeather>>
}
