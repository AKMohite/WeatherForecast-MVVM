package app.mak.atmosense.core.database.dao.api

import app.mak.atmosense.core.database.dao.ForecastWeatherEntity
import kotlinx.coroutines.flow.Flow

interface ForecastWeatherDAO {
  fun insert(forecastWeather: ForecastWeatherEntity)
  fun observeForecastByCityId(cityId: Long): Flow<List<ForecastWeatherEntity>>
  fun deleteByCityId(cityId: Long)
  fun deleteAll()
  fun insertAll(forecastEntities: List<ForecastWeatherEntity>)
}
