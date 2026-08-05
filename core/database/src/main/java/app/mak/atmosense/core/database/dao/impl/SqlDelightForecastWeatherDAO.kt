package app.mak.atmosense.core.database.dao.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.mak.atmosense.core.database.AtmosenseDatabase
import app.mak.atmosense.core.database.dao.ForecastWeatherEntity
import app.mak.atmosense.core.database.dao.api.ForecastWeatherDAO
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

@ContributesBinding(scope = AppScope::class)
@SingleIn(AppScope::class)
class SqlDelightForecastWeatherDAO(
  private val db: AtmosenseDatabase
) : ForecastWeatherDAO {

  private val query = db.forecast_weatherQueries

  override fun insert(forecastWeather: ForecastWeatherEntity) {
    query.insert(forecastWeather)
  }

  override fun insertAll(forecastEntities: List<ForecastWeatherEntity>) {
//    query.transaction {
    forecastEntities.forEach {
      query.insert(it)
    }
//    }
  }

  override fun observeForecastByCityId(cityId: Long): Flow<List<ForecastWeatherEntity>> {
    return query.getForecastByCityId(cityId)
      .asFlow()
      .mapToList(Dispatchers.IO)
  }

  override fun deleteByCityId(cityId: Long) {
    query.deleteByCityId(cityId)
  }

  override fun deleteAll() {
    query.deleteAll()
  }
}
