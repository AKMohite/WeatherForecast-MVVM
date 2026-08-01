package app.mak.atmosense.core.database.dao.impl

import app.mak.atmosense.core.database.AtmosenseDatabase
import app.mak.atmosense.core.database.dao.CurrentWeatherEntity
import app.mak.atmosense.core.database.dao.api.CurrentWeatherDAO
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn

@ContributesBinding(scope = AppScope::class)
@SingleIn(AppScope::class)
class SqlDelightCurrentWeatherDAO(
  private val db: AtmosenseDatabase
) : CurrentWeatherDAO {

  private val query = db.current_weatherQueries

  override suspend fun insert(currentWeather: CurrentWeatherEntity) {
    query.insert(currentWeather)
  }

  override suspend fun getByCityId(cityId: Long): CurrentWeatherEntity? {
    return query.getByCityId(cityId)
      .executeAsOneOrNull()
  }

  override suspend fun deleteByCityId(cityId: Long) {
    query.deleteByCityId(cityId)
  }

  override suspend fun deleteAll() {
    query.deleteAll()
  }
}
